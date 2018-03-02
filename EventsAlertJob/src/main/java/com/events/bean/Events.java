package com.events.bean;

import java.io.Serializable;

import com.events.constants.ConfigConstants;

/**
 * POJO for Events bean class.
 * 
 * @author 
 */
public class Events implements Serializable, ConfigConstants {

	private static final long serialVersionUID = -3837189433743544369L;
	
	private String id;
	private String state;
	private String type;
	private String host;
	private long timestamp;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
