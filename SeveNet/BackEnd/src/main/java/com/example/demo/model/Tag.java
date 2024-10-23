package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "P_C_Tag")
public class Tag {
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		private int id;
		
		private String tags;
		
		@OneToOne
	    @JoinColumn(name = "pcmsg_id")
	    @JsonBackReference(value = "pcmsg-tag")
	    private PCMsg pcmsg;

		private String remark;
		
	public Tag() {}
	public Tag(String tag,String remark) {
		this.tags=tag;
		this.remark=remark;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTag() {
		return tags;
	}
	public void setTag(String tags) {
		this.tags = tags;
	}
	
	public PCMsg getPCMsg() {
		return pcmsg;
	}
	public void setPCMsg(PCMsg pcmsg) {
		this.pcmsg = pcmsg;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "Tag [id=" + id + ", tag=" + tags + ", post/comment=" + pcmsg + ", remark=" + remark + "]";
	}


}