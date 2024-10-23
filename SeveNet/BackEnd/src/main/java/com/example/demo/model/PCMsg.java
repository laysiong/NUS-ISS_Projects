package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class PCMsg {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	private String imageUrl;
	private String content;
	@JoinColumn(name="Source_id")
	private Integer sourceId;
	private String timeStamp;
	private Boolean visibility;
	private String status;
	
	@ManyToOne
	@JoinColumn(name="User_id")
    @JsonBackReference
	private User user;
	
	@OneToOne(mappedBy = "pcmsg", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "pcmsg-tag")
	private Tag tag;
	 
	public PCMsg() {}
	public PCMsg(String imageUrl,String content,String timeStamp,Boolean visibility,String status,Integer sourceId) {
		this.imageUrl=imageUrl;
		this.content=content;
		this.timeStamp=timeStamp;
		this.visibility=visibility;
		this.status=status;
		this.sourceId=sourceId;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Boolean getVisibility() {
		return visibility;
	}
	public void setVisibility(Boolean visibility) {
		this.visibility = visibility;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public User getuser_id() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public User getUser() {
		return user;
	}
	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}
	public Integer getSourceId() {
		return sourceId;
	}
	public Tag getTag() {
		return tag;
	}
	public void setTag(Tag tags) {
		this.tag = tags;
	}
	
//	@Override
//	public String toString() {
//		return "Post [id=" + id + ", imageUrl=" + imageUrl + ", content=" + content + ", timeStamp=" + timeStamp
//				+ ", visibility=" + visibility + ", status=" + status + ", likes=" + likes + ", comments=" + comments
//				+ ", user=" + user + ", tag=" + tag + "]";
//	}

}