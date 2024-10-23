package com.example.demo.dto;

import java.util.List;

import com.example.demo.model.PCMsg;
import com.example.demo.model.User;

public class SearchResult {
	private List<User> all_matched_following_user_list;
    private List<User> all_matched_user_list;
    private List<PCMsg> all_matched_post_list;
    
	public List<User> getAll_matched_following_user_list() {
		return all_matched_following_user_list;
	}
	public void setAll_matched_following_user_list(List<User> all_matched_following_user_list) {
		this.all_matched_following_user_list = all_matched_following_user_list;
	}
	public List<User> getAll_matched_user_list() {
		return all_matched_user_list;
	}
	public void setAll_matched_user_list(List<User> all_matched_user_list) {
		this.all_matched_user_list = all_matched_user_list;
	}
	public List<PCMsg> getAll_matched_post_list() {
		return all_matched_post_list;
	}
	public void setAll_matched_post_list(List<PCMsg> all_matched_post_list) {
		this.all_matched_post_list = all_matched_post_list;
	}
	
	public SearchResult() {};
	
	public SearchResult(List<User> all_matched_following_user_list, List<User> all_matched_user_list,
			List<PCMsg> all_matched_post_list) {
		this.all_matched_following_user_list = all_matched_following_user_list;
		this.all_matched_user_list = all_matched_user_list;
		this.all_matched_post_list = all_matched_post_list;
	}
}
