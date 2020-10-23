package com.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

public class DataGridResult<T> implements Serializable {
	
	private static final long serialVersionUID = -3998961932030876845L;
	
	
	private Long total;
	private List<T> rows;
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	public DataGridResult(){}
	public DataGridResult(Long total,List<T> rows){
		this.total = total;
		this.rows = rows;
	}
	
}
