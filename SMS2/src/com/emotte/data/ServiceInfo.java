package com.emotte.data;

import java.io.Serializable;
import java.util.Date;

public class ServiceInfo implements Serializable {
	private static final long serialVersionUID = 8517633545835124349L;
	
	private long id;
	private String number;
	private String content;
	private Date createTime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	

}
