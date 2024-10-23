// src/services/RoleService.js
import axios from 'axios';
import { BASE_API_URL } from './config';

const ROLE_API_BASE_URL = `${BASE_API_URL}/pcmsgs`;

class RoleService {

    getTagCounts(){
        return axios.get(`${ROLE_API_BASE_URL}/tag-counts`);
    }

    findAllPosts(){
        return axios.get(`${ROLE_API_BASE_URL}/findHotPosts`);
    }

    isPCMsgBelongToUser(pcmsgId,userId) {
        return axios.get(`${ROLE_API_BASE_URL}/${pcmsgId}/belongsTo/${userId}`);
    }

    findAllPostsPageable(currentPage,size) {
        return axios.get(`${ROLE_API_BASE_URL}/findAllPostsPageable?page=${currentPage}&size=${size}`);
    }

    // this maybe more important for moderator 
    getAllPostsByUserId(id) {
        return axios.get(`${ROLE_API_BASE_URL}/findAllPostsByUserId/${id}`);
    }

    findAllFollowingPostsAndNotDeletedByUserId(id){
        return axios.get(`${ROLE_API_BASE_URL}/findAllFollowingPostsAndNotDeletedByUserId/${id}`);
    }

    getSearchResult(keyword) {
        return axios.get(`${ROLE_API_BASE_URL}/searchBarResult?keyword=${keyword}`);
    }

    getPostById(postId) {
        return axios.get(`${ROLE_API_BASE_URL}/findPostDetailById/${postId}`);
    }

    getPostIdByCommentId(commentId) {
        return axios.get(`${ROLE_API_BASE_URL}/findPostIdByCommentId/${commentId}`);
    }

    getCountCommentsByPostId(postId){
        return axios.get(`${ROLE_API_BASE_URL}/countAllCommentsByPostId/${postId}`);
    }

    getCountLikesByPostId(postId){
        return axios.get(`${ROLE_API_BASE_URL}/countLikesByPCMsgId/${postId}`);
    }

    hasUserLikedPost(userId,pcmsgId){
        return axios.get(`${ROLE_API_BASE_URL}/${pcmsgId}/likes/${userId}`);
    }

    getChildrenByPCMId(sourceId){
        return axios.get(`${ROLE_API_BASE_URL}/${sourceId}/children`);
    }


    LikeOrUnlikePCMsg(userId,pcmsgId){
        return axios.put(`${ROLE_API_BASE_URL}/likeOrUnlikePCMsg/${userId}/${pcmsgId}`);
    }

    createPost(post) {
        return axios.post(`${ROLE_API_BASE_URL}/createPost`, post);
    }

    createComment(comment) {
        return axios.post(`${ROLE_API_BASE_URL}/createComment`, comment);
    }


    deletePost(postId){
        return axios.put(`${ROLE_API_BASE_URL}/delete/${postId}`, postId);
    }

    hidePost(postId){
        return axios.put(`${ROLE_API_BASE_URL}/hide/${postId}`, postId);
    }


    //getPageable() to be provided


    getTagById(Id){
        return axios.get(`${ROLE_API_BASE_URL}/getTag/${Id}`, Id);
    }

    updateTag(id, tagData) {
        return axios.put(`${ROLE_API_BASE_URL}/editTag/${id}`, tagData);
    }

}

export default new RoleService();
