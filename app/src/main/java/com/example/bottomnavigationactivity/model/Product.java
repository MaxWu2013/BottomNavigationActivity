package com.example.bottomnavigationactivity.model;


import java.io.Serializable;

import android.content.Context;
import android.text.TextUtils;

import com.example.bottomnavigationactivity.R;
import com.example.bottomnavigationactivity.util.Constant;

public class Product implements Serializable{
	private static final long serialVersionUID = 4275760761989581050L;

	private String resourceId = null;
	private Field[] fields ;
	private Record[] records;
	private Links _links;
	private int limit ;
	private int total ;

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public Field[] getFields() {
		return fields;
	}

	public void setFields(Field[] fields) {
		this.fields = fields;
	}

	public Record[] getRecords() {
		return records;
	}

	public void setRecords(Record[] records) {
		this.records = records;
	}

	public Links get_links() {
		return _links;
	}

	public void set_links(Links _links) {
		this._links = _links;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}
