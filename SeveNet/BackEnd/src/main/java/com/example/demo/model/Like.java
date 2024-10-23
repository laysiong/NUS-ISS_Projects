package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="pcmsg_like")
public class Like {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer UserId;

	private Integer PCMsgId;
	
	public Like() {}
	
	public Like(Integer UserId, Integer PCMsgId) {
		this.UserId=UserId;
		this.PCMsgId = PCMsgId;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getUserId() {
		return UserId;
	}
	public void setUserId(Integer UserId) {
		this.UserId = UserId;
	}
	public Integer getPCMsgId() {
		return PCMsgId;
	}
	public void setPCMsgId(Integer PCMsgId) {
		this.PCMsgId = PCMsgId;
	}
	
	@Override
	public String toString() {
		return "Like [id=" + id +", UserId=" + UserId + ", PCMsgId=" + PCMsgId + "]";
	}


}