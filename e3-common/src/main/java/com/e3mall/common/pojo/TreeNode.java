package com.e3mall.common.pojo;

import java.io.Serializable;

/**
 * 
 *  因为页面树形结构是异步获取字节点数据，所以无需添加子节点数据List
 *
 */

public class TreeNode implements Serializable {

	private static final long serialVersionUID = 4716599607769905179L;

	private long id;
	private String text;
	private String state;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
}
