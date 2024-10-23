package com.example.demo.dto;

import java.util.List;

import com.example.demo.model.PCMsg;

public class PCMsgDetail {
	private PCMsg pcmsg;
	private List<PCMsg> comments;
	public PCMsg getPcmsg() {
		return pcmsg;
	}
	public void setPcmsg(PCMsg pcmsg) {
		this.pcmsg = pcmsg;
	}
	public List<PCMsg> getComments() {
		return comments;
	}
	public void setComments(List<PCMsg> comments) {
		this.comments = comments;
	}
	
	public PCMsgDetail() {};
	public PCMsgDetail(PCMsg pcmsg,List<PCMsg> comments) {
		this.pcmsg = pcmsg;
		this.comments = comments;
	}	
}
