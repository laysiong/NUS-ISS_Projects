package com.example.demo.interfacemethods;

import java.util.List;
import java.util.Map;

import com.example.demo.dto.UserDTO;
import org.springframework.data.domain.Page;

import com.example.demo.model.PCMsg;
import com.example.demo.model.User;
import com.example.demo.dto.PCMsgDTO;
import com.example.demo.dto.PCMsgDetail;

public interface PCMsgInterface {

	Integer CountPosts();
	Integer CountComments();

	List<PCMsg> findAllPCMsgDateDESC();
	//Top 5 post
	List<PCMsg> findTop5Posts();
	// return whatever posts in our database
	List<PCMsg> findAllPosts();
	// return all posts of the specific user with userId
	List<PCMsg> findAllPostsByUserId(Integer userId);
	// return all FollowingPost, Not Deleted, Include own Post
	List<PCMsg> findAllFollowingPostsAndNotDeletedByUserId(Integer userId);
	// return hot posts in our database
	// according to the amount of comments and likes
	List<PCMsg> findAllHotPosts();
	List<PCMsg> findTop5HotPosts();
	
	Map<String, Integer> getTagCounts();
	
	// the next three interface/service are for the search bar use case
	// return related search following users by user_name
	List<User> findAllSearchFollowingUser(String keyword);
	// return related search users by user_name
	List<User> findAllSearchUser(String keyword);
	// return related search posts by post content or post user_name
	List<PCMsg> findAllSearchPostsOrderByDateDESC(String keyword);
	
	// return all children(just the first layer) with fatherId/its own id
	List<PCMsg> findAllFirstLayerChildren(Integer fatherId);
	PCMsg findPCMsgById (Integer id);
	// add/delete like to post or comment by its id
	void updateLikeForPCMsgByIds(Integer UserId, Integer PCMsgId);
	// Find post or comment by its id and count its total likes
	Integer countLikesByPCMsgId(Integer id);
	// Count Comments by PostId ,take comments_comments into count
	Integer countTotalCommentsByPostId(Integer postId);
	// Return Post & its first layer comments by postId
	PCMsgDetail findPCMsgDetailById (Integer postId);
	// Return Post by its commentId or comment-commentId
	Integer findPostIdByCommentId (Integer commentId);
	
	// Return If User like the post
	boolean hasUserLikedPost(int userId, int postId);
	// Return if this comment belongs to this user
	boolean isPCMsgBelongToUser(int userId, Integer pcmsgId);

	// Pagination
	Page<PCMsgDTO> findAllPosts(Integer page, Integer size);
	Page<PCMsgDTO> findAllPostsByUserId(Integer userId,Integer page, Integer size);
	Page<PCMsgDTO> findAllFollowingPostsAndNotDeletedByUserId(Integer userId,Integer page, Integer size);
	// TODO: for User pagination, these 3 below is for the search bar result page, it is triggered by click of 'show more' button
	Page<UserDTO> findAllSearchFollowingUser(String keyword, Integer page, Integer size);
	Page<UserDTO> findAllSearchUser(String keyword, Integer page, Integer size);
	Page<PCMsgDTO> findAllSearchPostsOrderByDateDESC(String keyword,Integer page, Integer size);

	// CRUD
	PCMsg savePCMsg (PCMsg pcmsg);
	PCMsg updatePCMsg (Integer id, PCMsg pcmsg);
	void updatePCMsgStatusById(Integer id,String status);
	void showPCMsgById(Integer id);
	void deletePCMsgById(Integer id);
	void hidePCMsgById(Integer id);
}