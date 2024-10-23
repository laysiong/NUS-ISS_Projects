package com.example.demo.dto;

import com.example.demo.model.PCMsg;
import com.example.demo.model.Tag;

public class PCMsgDTO {
	
	// just to check pagination
	// can add more information if required
    private Integer id;
    private String content;
	private UserDTOForPCMsg user;
	private Integer sourceId;
	private String timeStamp;
	private String status;
	private Tag tag;

    // Constructor, getters, and setters

    public PCMsgDTO(PCMsg pcmsg) {
        this.id = pcmsg.getId();
        this.content = pcmsg.getContent();
		this.user = new UserDTOForPCMsg(pcmsg.getUser());
		this.status = pcmsg.getStatus();
		this.tag = pcmsg.getTag();
		this.sourceId = pcmsg.getSourceId();
		this.timeStamp = pcmsg.getTimeStamp();
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public UserDTOForPCMsg getUser() {
		return user;
	}

	public void setUser(UserDTOForPCMsg user) {
		this.user = user;
	}

	public Integer getSourceId() {
		return sourceId;
	}

	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}
}
