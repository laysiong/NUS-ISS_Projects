package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.demo.controller.CheckThenBanUserController;
import com.example.demo.dto.UserDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.PCMsgDTO;
import com.example.demo.dto.PCMsgDetail;
import com.example.demo.interfacemethods.PCMsgInterface;
import com.example.demo.model.PCMsg;
import com.example.demo.model.User;
import com.example.demo.model.FollowList;
import com.example.demo.model.Label;
import com.example.demo.model.Like;
import com.example.demo.repository.FollowListRepository;
import com.example.demo.repository.LabelRepository;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.PCMsgRepository;
import com.example.demo.repository.UserRepository;


@Service
@Transactional(readOnly = true)
public class PCMsgService implements PCMsgInterface{

	@Autowired
	private PCMsgRepository pcmsgRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private LikeRepository likeRepository;
	
    @Autowired
    private LabelRepository labelRepository;
	
	@Autowired
    private FollowListRepository followListRepository;

	@Autowired
	private CheckThenBanUserController checkThenBanUserController;

	private void changePCMsgStatusById(Integer id, String operation) {
		try {
			PCMsg curPCMsg = findPCMsgById(id);
			curPCMsg.setStatus(operation);
			pcmsgRepository.save(curPCMsg);
		} catch (Exception e) {
			throw new RuntimeException("Failed to " + operation + " post/comment with ID: " + id, e);
		}
	}
	
	//Count Posts
	public Integer CountPosts() {
		return pcmsgRepository.countPosts();
	}
	
	//Count Comments
	public Integer CountComments() {
		return pcmsgRepository.countComments();
	}
	
	//get tags count
	public Map<String, Integer> getTagCounts() {
        List<Label> labels = labelRepository.findAll();
        List<PCMsg> pcMsgs = pcmsgRepository.findAll();
        Map<String, Integer> tagCounts = new HashMap<>();

        // Initialize all labels with zero count
        for (Label label : labels) {
            tagCounts.put(label.getLabel().toLowerCase(), 0);
        }

        for (PCMsg pcMsg : pcMsgs) {
            if (pcMsg.getTag() != null && pcMsg.getTag().getTag() != null) {
                String[] tags = pcMsg.getTag().getTag().split(",");
                boolean hasValidTag = false;
                for (String tag : tags) {
                    tag = tag.trim().toLowerCase();
                    if (!"none".equals(tag) && !tag.isEmpty()) {
                        tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);
                        hasValidTag = true;
                    }
                }
                if (!hasValidTag) {
                    tagCounts.put("undefined", tagCounts.getOrDefault("undefined", 0) + 1);
                }
            } else {
                tagCounts.put("undefined", tagCounts.getOrDefault("undefined", 0) + 1);
            }
        }
        
        return tagCounts;
    }
	
	// count all Comments
	private int countCommentsRecursively(Integer pcmsgId) {
        List<PCMsg> children = pcmsgRepository.findAllFirstLayerChildren(pcmsgId);
        int count = children.size();
        for (PCMsg child : children) {
            count += countCommentsRecursively(child.getId());
        }
        return count;
    }
	
	// update itself status and its children using recursion
	private void updateStatusOfPCMsgAndChildren(Integer pcmsgId, String operation) {
        List<PCMsg> children = pcmsgRepository.findAllFirstLayerChildren(pcmsgId);
        for (PCMsg child : children) {
        	updateStatusOfPCMsgAndChildren(child.getId(),operation);
        }
    	// actually not delete from database ,just change its status into delete
        changePCMsgStatusById(pcmsgId,operation);
    }
	
	private void upgradePCMsgStatusById (Integer id, String operation) {
		PCMsg curPCMsg = findPCMsgById(id);
		// if this PCMsg is a post then we set all of its children's status into delete/hide/show
		if (curPCMsg.getSourceId() == null) {
			updateStatusOfPCMsgAndChildren(id,operation);
		} else {
			// if this PCMsg is a comment then we set itself status into delete/hide/show
			// do nothing to its children
			changePCMsgStatusById(id,operation);
		}
	}

	private Pageable toPageable(Integer page, Integer size) {
		return PageRequest.of(page, size);
	}

	// transfer Page<PCMsg> to Page<PCMsgDTO>
	private Page<PCMsgDTO> transformToPCMsgDTOPage(Page<PCMsg> pcmsgPage, Pageable pageable) {
		List<PCMsgDTO> pcmsgDTOs = pcmsgPage.stream()
				.map(PCMsgDTO::new)
				.collect(Collectors.toList());
		return new PageImpl<>(pcmsgDTOs, pageable, pcmsgPage.getTotalElements());
	}
	
	@Override
	public List<PCMsg> findAllPosts() {
		//i edited this, previous it is return comment too.
		return pcmsgRepository.findAllPostsOnly();
	}
	
	@Override
	public List<PCMsg> findTop5Posts() {
		//i edited this, previous it is return comment too.
		return pcmsgRepository.findTop5BySourceIdIsNullOrderByTimeStampDesc();
	}

	@Override
	public List<PCMsg> findAllPCMsgDateDESC() {
		return pcmsgRepository.findAllPCMsgsOrderByDateDesc();
	}
	
	@Override
	public List<PCMsg> findAllPostsByUserId(Integer userId) {
		return pcmsgRepository.findAllPostsByUserIdByDateDesc(userId);
	}
	
	@Override
	public List<PCMsg> findAllFollowingPostsAndNotDeletedByUserId(Integer userId) {
		User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID:" + userId));
		
		List<FollowList> followings = user.getFollowings();
    	List<Integer> blockedUserIds = user.getBlockedUserIds();

        List<Integer> followingUserIds = followings.stream()
                .map(FollowList -> FollowList.getFollowedUser().getId())
                .collect(Collectors.toList());
        
        return pcmsgRepository.findAllFollowingPostsAndNotDeletedByUserId(followingUserIds, blockedUserIds, userId);
	}
	
	@Override
	public List<PCMsg> findAllHotPosts() {
		List<PCMsg> allPosts = findAllPosts();
		
		// Create a map to store post and its popularity score
        Map<PCMsg, Integer> postPopularityMap = new HashMap<>();

        for (PCMsg post : allPosts) {
            int likesCount = countLikesByPCMsgId(post.getId());
            int commentsCount = countTotalCommentsByPostId(post.getId());
            // can adjust the weight of likes and comments, i simply add them up
            int popularityScore = likesCount + commentsCount; 
            postPopularityMap.put(post, popularityScore);
        }

        // Sort posts by popularity score in descending order
        return postPopularityMap.entrySet().stream()
				.sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<PCMsg> findTop5HotPosts() {
	    return findAllHotPosts().stream().limit(5).collect(Collectors.toList());
	}
	
	@Override
	public List<User> findAllSearchFollowingUser(String keyword) {
		return userRepository.findAllFollowingByUserName(keyword);
	}

	@Override
	public List<User> findAllSearchUser(String keyword) {
		return userRepository.findAllByUserName(keyword);
	}

	@Override
	public List<PCMsg> findAllSearchPostsOrderByDateDESC(String keyword) {
		if (keyword.isBlank() || keyword.isEmpty()) {
			throw new RuntimeException("can not enter empty string!");
		}
		return pcmsgRepository.SearchPostsByContentAndUserNameOrderByDateDesc(keyword);
	}

	@Override
	public List<PCMsg> findAllFirstLayerChildren(Integer fatherId) {
		// make sure this post/comment exists
		findPCMsgById(fatherId);
		return pcmsgRepository.findAllFirstLayerChildren(fatherId);
	}

	// Pagination version of finAllPosts
	@Override
	public Page<PCMsgDTO> findAllPosts(Integer page, Integer size) {
		Pageable pageable = toPageable(page, size);
		Page<PCMsg> pcmsgPage = pcmsgRepository.findAllPostsOnly(pageable);
		return transformToPCMsgDTOPage(pcmsgPage, pageable);
	}

	@Override
	public Page<PCMsgDTO> findAllPostsByUserId(Integer userId, Integer page, Integer size) {
		Pageable pageable = toPageable(page, size);
		Page<PCMsg> pcmsgPage = pcmsgRepository.findAllPostsByUserIdByDateDesc(userId, pageable);
		return transformToPCMsgDTOPage(pcmsgPage, pageable);
	}

	@Override
	public Page<PCMsgDTO> findAllFollowingPostsAndNotDeletedByUserId(Integer userId, Integer page, Integer size) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with ID:" + userId));
		List<FollowList> followings = user.getFollowings();
		List<Integer> blockedUserIds = user.getBlockedUserIds();
		List<Integer> followingUserIds = followings.stream()
				.map(FollowList -> FollowList.getFollowedUser().getId())
				.collect(Collectors.toList());
		Pageable pageable = toPageable(page, size);
		Page<PCMsg> pcmsgPage = pcmsgRepository.findAllFollowingPostsAndNotDeletedByUserId(followingUserIds, blockedUserIds, userId, pageable);
		return transformToPCMsgDTOPage(pcmsgPage, pageable);
	}

	@Override
	public Page<UserDTO> findAllSearchFollowingUser(String keyword, Integer page, Integer size) {
		Pageable pageable = toPageable(page, size);
		Page<User> userPage = userRepository.findAllFollowingByUserName(keyword, pageable);
		List<UserDTO> userDTOs = userPage.stream()
				.map(UserDTO::new)
				.collect(Collectors.toList());
		return new PageImpl<>(userDTOs, pageable, userPage.getTotalElements());
	}

	@Override
	public Page<UserDTO> findAllSearchUser(String keyword, Integer page, Integer size) {
		Pageable pageable = toPageable(page, size);
		Page<User> userPage = userRepository.findAllByUserName(keyword, pageable);
		List<UserDTO> userDTOs = userPage.stream()
				.map(UserDTO::new)
				.collect(Collectors.toList());
		return new PageImpl<>(userDTOs, pageable, userPage.getTotalElements());
	}

	@Override
	public Page<PCMsgDTO> findAllSearchPostsOrderByDateDESC(String keyword, Integer page, Integer size) {
		if (keyword.isBlank() || keyword.isEmpty()) {
			throw new RuntimeException("Cannot enter empty string!");
		}
		Pageable pageable = toPageable(page, size);
		Page<PCMsg> pcmsgPage = pcmsgRepository.SearchPostsByContentAndUserNameOrderByDateDesc(keyword, pageable);
		return transformToPCMsgDTOPage(pcmsgPage, pageable);
	}

	@Override
	public PCMsg findPCMsgById(Integer id) {
		return pcmsgRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Post/Comment not found with ID:" + id));
	}

	@Override
	@Transactional(readOnly = false)
	public void updateLikeForPCMsgByIds(Integer userId, Integer pcmsgId) {
		Like curLike = likeRepository.hasWithUserAndPCMsg(userId,pcmsgId);
		if ( curLike != null) {
			// unlike
			likeRepository.delete(curLike);
		} else {
			// save
			Like newLike = new Like(userId,pcmsgId);
			likeRepository.save(newLike);
		}
	}

	@Override
	public Integer countLikesByPCMsgId(Integer id) {
		return likeRepository.countLikesByPCMsgId(id);
	}

	@Override
	public Integer countTotalCommentsByPostId(Integer postId) {
		// Make sure this post exists
        findPCMsgById(postId);
        // Recursively count comments
        return countCommentsRecursively(postId);
	}
	
	@Override
	public PCMsgDetail findPCMsgDetailById(Integer postId) {
		PCMsg findPost = findPCMsgById(postId);
		List<PCMsg> findComments = findAllFirstLayerChildren(postId);
		return new PCMsgDetail(findPost,findComments);
	}

	@Override
	public Integer findPostIdByCommentId(Integer commentId) {
		PCMsg comment = findPCMsgById(commentId);

		// if is post
		if (comment.getSourceId() == null) {
			return comment.getId();
		}

		// if comment find sourceId
		Integer parentCommentId = comment.getSourceId();
		while (parentCommentId != null) {
			PCMsg parentComment = findPCMsgById(parentCommentId);

			if (parentComment.getSourceId() == null) {
				return parentComment.getId();
			}

			parentCommentId = parentComment.getSourceId();
		}

		throw new EntityNotFoundException("PostId not found for the given commentId");
	}
	
	@Override
	public boolean hasUserLikedPost(int userId, int postId){
        return likeRepository.existsByUserIdAndPCMsgId(userId, postId);
	}

	@Override
	public boolean isPCMsgBelongToUser(int userId, Integer pcmsgId) {
		return pcmsgRepository.isPCMsgBelongToUser(userId, pcmsgId);
	}
	
	//Calculate the amount of score to be minus off after they posted.
    public void penalizeUserBasedOnTags(PCMsg post) {
		User user = userRepository.findById(post.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found with ID"));
		
        String[] tags = post.getTag().getTag().split(",");
        
        //Calculate the total penalty score
        int penalty = 0;
        double multipler = 1;
        
        if (post.getSourceId() == null) {
        	multipler = 0.5;
        }
        
        for (String tagName : tags) {
            if (!tagName.equalsIgnoreCase("none")) { // Ignore "none" tags
                Label label = labelRepository.findByLabel(tagName.trim());
                if (label != null) {
                    penalty += (int)(label.getPenaltyScore() * multipler);
                }
            }
        }
        
        if (user.getSocialScore() != null) {
        	//Ensure score does not go into negative.
            user.setSocialScore(Math.max(user.getSocialScore() - penalty,0));
            userRepository.save(user);
			// change user authorization according to the socialScore
			checkThenBanUserController.checkUserSocialScoreThenUpdateStatusAndAuth(user.getId());
        } else {
        	  System.out.println("User Social Score is null for User ID:" + user.getSocialScore());
        }
        //System.out.println("User Social Score is null for User ID:" + penalty);
    }
	
	@Override
	@Transactional(readOnly = false)
	public PCMsg savePCMsg(PCMsg pcmsg) {
		if (pcmsg.getTag().getTag() != null) {
			penalizeUserBasedOnTags(pcmsg);
		}
		return pcmsgRepository.save(pcmsg);
	}

	@Override
	@Transactional(readOnly = false)
	public PCMsg updatePCMsg(Integer id, PCMsg pcmsg) {
		PCMsg curPCMsg = findPCMsgById(id);
	    
	    curPCMsg.setImageUrl(pcmsg.getImageUrl());
	    curPCMsg.setContent(pcmsg.getContent());
	    curPCMsg.setSourceId(pcmsg.getSourceId());
	    curPCMsg.setTimeStamp(pcmsg.getTimeStamp());
	    curPCMsg.setVisibility(pcmsg.getVisibility());
	    curPCMsg.setStatus(pcmsg.getStatus());
	    curPCMsg.setUser(pcmsg.getUser());
	    curPCMsg.setTag(pcmsg.getTag());

	    return pcmsgRepository.save(curPCMsg);
	}

	@Override
	@Transactional(readOnly = false)
	public void updatePCMsgStatusById(Integer id, String status) {
		upgradePCMsgStatusById(id,status);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void showPCMsgById(Integer id) {
		upgradePCMsgStatusById(id,"show");
	}
	
	@Override
	@Transactional(readOnly = false)
	public void deletePCMsgById(Integer id) {
		upgradePCMsgStatusById(id,"delete");
	}

	@Override
	@Transactional(readOnly = false)
	public void hidePCMsgById(Integer id) {
		upgradePCMsgStatusById(id,"hide");
	}

}