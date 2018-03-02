package com.events.bean;

import java.io.Serializable;

import com.events.constants.ConfigConstants;

/**
 * POJO for EventsLogger bean class.
 * 
 * @author
 */
public class EventsLogger implements Serializable, ConfigConstants {

	private static final long serialVersionUID = -3837189433743544369L;

	private String id;
	private String alert;
	private String type;
	private String host;
	private String duration;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
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

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

}
