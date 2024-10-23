package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.example.demo.dto.PCMsgDTO;
import com.example.demo.dto.PCMsgDetail;
import com.example.demo.dto.SearchResult;
import com.example.demo.interfacemethods.PCMsgInterface;
import com.example.demo.interfacemethods.TagInterface;
import com.example.demo.model.PCMsg;
import com.example.demo.model.Tag;
import com.example.demo.model.User;
import com.example.demo.service.TaggingService;

@RestController
@RequestMapping("/api/pcmsgs")
public class PCMsgController {

    @Autowired
    private PCMsgInterface pcmsgService;

    @Autowired
    private TagInterface tagService;

    @Autowired
    private TaggingService taggingService;

    @Autowired
    @Qualifier("pagedResourcesAssembler")
    private PagedResourcesAssembler<PCMsgDTO> pagedResourcesAssembler;

    // get tags count
    @GetMapping("/tag-counts")
    public Map<String, Integer> getTagCounts() {
        return pcmsgService.getTagCounts();
    }

    // return all posts
    @GetMapping("/findAllPosts")
    public ResponseEntity<List<PCMsg>> getAllPosts() {
        List<PCMsg> postList = pcmsgService.findAllPosts();
        return ResponseEntity.ok(postList);
    }

    // return all posts order by like & comments
    @GetMapping("/findHotPosts")
    public ResponseEntity<List<PCMsg>> getHotPosts() {
        List<PCMsg> postList = pcmsgService.findAllHotPosts();
        return ResponseEntity.ok(postList);
    }

    // this is for the search result page, it will return three lists
    // 1st is the all_matched_following_user_list
    // 2nd is the all_matched_user_list
    // 3rd is the all_matched_post_list(post_user or post content)
    // TODO: Pagination version hasn't implemented yet
    @GetMapping("/searchBarResult")
    public ResponseEntity<SearchResult> getSearchBarResult(@RequestParam("keyword") String k) {
        List<User> all_matched_following_user_list = pcmsgService.findAllSearchFollowingUser(k);
        List<User> all_matched_user_list = pcmsgService.findAllSearchUser(k);
        List<PCMsg> all_matched_post_list = pcmsgService.findAllSearchPostsOrderByDateDESC(k);

        SearchResult searchResult = new SearchResult(all_matched_following_user_list, all_matched_user_list, all_matched_post_list);
        return ResponseEntity.ok(searchResult);
    }

    // find by UserId, it is for user that log in platform.
    @GetMapping("/findAllPostsByUserId/{id}")
    public ResponseEntity<List<PCMsg>> getAllPostsByUserId(@PathVariable("id") Integer id) {
        List<PCMsg> postList = pcmsgService.findAllPostsByUserId(id);
        return ResponseEntity.ok(postList);
    }

    // find all posts for this users' following and exclude blockList -Haven't Used
    @GetMapping("/findAllFollowingPostsByUserId/{id}")
    public ResponseEntity<List<PCMsg>> getAllFollowingPostsByUserId(@PathVariable("id") Integer id) {
        //List<PCMsg> postList = pcmsgService.findAllFollowingPostsByUserId(id);
        List<PCMsg> postList = pcmsgService.findAllPostsByUserId(id);

        return ResponseEntity.ok(postList);
    }

    // find all posts for this users'following, !blockList, his own post
    @GetMapping("/findAllFollowingPostsAndNotDeletedByUserId/{id}")
    public ResponseEntity<List<PCMsg>> findAllFollowingPostsAndNotDeletedByUserId(@PathVariable("id") Integer id) {
        //List<PCMsg> postList = pcmsgService.findAllFollowingPostsByUserId(id);
        List<PCMsg> postList = pcmsgService.findAllFollowingPostsAndNotDeletedByUserId(id);

        return ResponseEntity.ok(postList);
    }

    @GetMapping("/findPostDetailById/{id}")
    public ResponseEntity<PCMsgDetail> getPostById(@PathVariable("id") Integer id) {
        PCMsgDetail post = pcmsgService.findPCMsgDetailById(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/findPostIdByCommentId/{id}")
    public ResponseEntity<Integer> getPostIdByCommentId(@PathVariable("id") Integer id) {
        Integer postId = pcmsgService.findPostIdByCommentId(id);
        return ResponseEntity.ok(postId);
    }

    // for pagination
    @GetMapping("/findAllPostsPageable")
    public PagedModel<EntityModel<PCMsgDTO>> getAllPosts(@RequestParam(value = "page", defaultValue = "0") int page,
                                                         @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<PCMsgDTO> pcmsgPage = pcmsgService.findAllPosts(page, size);
        return pagedResourcesAssembler.toModel(pcmsgPage);
    }

    @GetMapping("/findAllPostsByUserIdPageable/{id}")
    public PagedModel<EntityModel<PCMsgDTO>> getAllPostsByUserId(@PathVariable("id") Integer id, @RequestParam(value = "page", defaultValue = "0") int page,
                                                                 @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<PCMsgDTO> postList = pcmsgService.findAllPostsByUserId(id, page, size);
        return pagedResourcesAssembler.toModel(postList);
    }

    @GetMapping("/findAllFollowingPostsAndNotDeletedByUserIdPageable/{id}")
    public PagedModel<EntityModel<PCMsgDTO>> findAllFollowingPostsAndNotDeletedByUserId(@PathVariable("id") Integer id,
                                                                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                                                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<PCMsgDTO> postList = pcmsgService.findAllFollowingPostsAndNotDeletedByUserId(id, page, size);
        return pagedResourcesAssembler.toModel(postList);
    }
	
    @GetMapping("/{id}/children")
    public ResponseEntity<List<PCMsg>> getChildrenByPCMId(@PathVariable Integer id) {
        List<PCMsg> children = pcmsgService.findAllFirstLayerChildren(id);
        return ResponseEntity.ok(children);
    }
    
    // countTotalCommentsByPostId including comment_comment
 	@GetMapping("/countAllCommentsByPostId/{postId}")
     public ResponseEntity<Integer> getTotalCommentsByPostId(@PathVariable("postId") Integer postId) {
         Integer totalComments = pcmsgService.countTotalCommentsByPostId(postId);
         return ResponseEntity.ok(totalComments);
     }
 	
 	// countLikesByPCMsgId all for this post/comment
 	@GetMapping("/countLikesByPCMsgId/{pcmsgId}")
    public ResponseEntity<Integer> getLikesByPCMsgId(@PathVariable("pcmsgId") Integer pcmsgId) {
        Integer totalLikes = pcmsgService.countLikesByPCMsgId(pcmsgId);
        return ResponseEntity.ok(totalLikes);
    }
 	
 	//hasUserLikePost
 	 @GetMapping("/{postId}/likes/{userId}")
     public ResponseEntity<Boolean> hasUserLikedPost(@PathVariable int postId, @PathVariable int userId) {
         boolean isLiked = pcmsgService.hasUserLikedPost(userId, postId);
         return new ResponseEntity<>(isLiked, HttpStatus.OK);
     }

    //isPCMsgBelongToUser
    @GetMapping("/{pcmsgId}/belongsTo/{userId}")
    public ResponseEntity<Boolean> isPCMsgBelongToUser(@PathVariable int pcmsgId, @PathVariable int userId) {
        boolean isBelong = pcmsgService.isPCMsgBelongToUser(userId, pcmsgId);
        return new ResponseEntity<>(isBelong, HttpStatus.OK);
    }
    
 	// TODO: post sourceId should be null
 	// and the tag logic haven't done
    @PostMapping(value = "/createPost", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PCMsg> savePost(@RequestBody PCMsg post) {
        processTagsAndLabels(post);
        PCMsg newPost = pcmsgService.savePCMsg(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPost);
    }
	
    // TODO: comment sourceId should be its upper layer id 
    // and the tag logic haven't done
	@PostMapping(value = "/createComment", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PCMsg> saveComment(@RequestBody PCMsg comment) {
        processTagsAndLabels(comment);
		PCMsg newComment = pcmsgService.savePCMsg(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
    }
	
	// can save new like also can delete existing like(unlike)
	@PutMapping("likeOrUnlikePCMsg/{userId}/{pcmsgId}")
    public ResponseEntity<Void> LikeOrUnlikePCMsg(@PathVariable("userId") Integer userId, @PathVariable("pcmsgId")Integer pcmsgId) {
		pcmsgService.updateLikeForPCMsgByIds(userId, pcmsgId);
		return ResponseEntity.noContent().build();
    }
	
	// update PCMsg
	@PutMapping("updatePCMsg/{pcmsgId}")
    public ResponseEntity<PCMsg> updatePCMsg(@PathVariable("pcmsgId")Integer pcmsgId, @RequestBody PCMsg newPCMsg) {
		PCMsg updatedPCMsg = pcmsgService.updatePCMsg(pcmsgId, newPCMsg);
		return ResponseEntity.status(HttpStatus.OK).body(updatedPCMsg);
    }
	
	// update PCMsg Status  
	// need to transfer status(operation) in the request
	@PutMapping("updatePCMsgStatus/{pcmsgId}")
    public ResponseEntity<Void> updatePCMsgStatus(@PathVariable("pcmsgId")Integer pcmsgId, @RequestParam("status") String status) {
		pcmsgService.updatePCMsgStatusById(pcmsgId, status);
		return ResponseEntity.noContent().build();
    }
	
	// hide post or comment
	@PutMapping("/show/{id}")
    public ResponseEntity<Void> showPCMsg(@PathVariable("id") Integer id) {
		pcmsgService.showPCMsgById(id);
        return ResponseEntity.noContent().build();
    }
	
	// hide post or comment
	@PutMapping("/hide/{id}")
    public ResponseEntity<Void> hidePCMsg(@PathVariable("id") Integer id) {
		pcmsgService.hidePCMsgById(id);
        return ResponseEntity.noContent().build();
    }
	
	// delete post or comment
	@PutMapping("/delete/{id}")
    public ResponseEntity<Void> deletePCMsg(@PathVariable("id") Integer id) {
		pcmsgService.deletePCMsgById(id);
        return ResponseEntity.noContent().build();
    }
	
	@GetMapping("/getTag/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable("id") Integer id) {
        Tag tag = tagService.getTagById(id);
        return ResponseEntity.ok(tag);
    }

	// edit tag list
    @PutMapping("/editTag/{id}")
    public ResponseEntity<Tag> editTag(@PathVariable("id") Integer id, @RequestBody Tag tagForm) {
        Tag updatedTag = tagService.updateReport(id, tagForm);
        return ResponseEntity.ok(updatedTag);
    }

    private void processTagsAndLabels(PCMsg pcmsg) {
        String combinedTags;
        String tags = null;
        String tagsString = null;

        
        try {
        	tags = taggingService.spamCheck(pcmsg.getContent());
        }catch (Exception e) {
        	//System.out.println("Spam failed");
        	tags = "spam-uncheck";
        }
        
        
        try {
        	//System.out.println("Own API");
            tagsString = taggingService.getTagsForText(pcmsg.getContent());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
        	
            if (e.getStatusCode().is5xxServerError() || e.getStatusCode().is4xxClientError()) {
                try {
                	//System.out.println("Hugger");
                    tagsString = taggingService.HugTagsForText(pcmsg.getContent());
                } catch (HttpClientErrorException | HttpServerErrorException ex) {
                	
                	//System.out.println("Fail-Safe");
                    tagsString = "untagged";
                }
            }
        } catch (Exception e) {
            // Handle any other exceptions that may occur
        	//System.out.println("Tagger failed");
            tagsString = "untagged";
        }        
        
        
        if ("spam".equals(tags)) {
            //send notifcation to inform user?
            //Use replaceAll to remove the trailing comma and space
            combinedTags = tagsString != null ? String.join(",", tags, tagsString).replaceAll(",\\s*$", "") : tags;
            pcmsg.setStatus("delete");
        }else if ("spam-uncheck".equals(tags)) {
        	
            combinedTags = tagsString != null ? String.join(",", tags, tagsString).replaceAll(",\\s*$", "") : tags;
        }else {
            combinedTags = tagsString;
        } 


        Tag tag = new Tag();
        tag.setTag(combinedTags);
        tag.setPCMsg(pcmsg);
        pcmsg.setTag(tag);
    }
}