package com.crawl.entity;

import java.sql.Timestamp;

/**
 * Proxyhost entity. @author MyEclipse Persistence Tools
 */

public class Proxyhost implements java.io.Serializable {

	// Fields

	private String id;
	private String hostIp;
	private Integer hostPort;
	private String source;
	private String country;
	private Integer useable;
	private Timestamp lastUsed;
	private Timestamp addTime;
	private Timestamp refreshTime;

	// Constructors

	/** default constructor */
	public Proxyhost() {
	}

	/** full constructor */
	public Proxyhost(String hostIp, Integer hostPort, String source,
			String country, Integer useable, Timestamp lastUsed,
			Timestamp addTime, Timestamp refreshTime) {
		this.hostIp = hostIp;
		this.hostPort = hostPort;
		this.source = source;
		this.country = country;
		this.useable = useable;
		this.lastUsed = lastUsed;
		this.addTime = addTime;
		this.refreshTime = refreshTime;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHostIp() {
		return this.hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public Integer getHostPort() {
		return this.hostPort;
	}

	public void setHostPort(Integer hostPort) {
		this.hostPort = hostPort;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getUseable() {
		return this.useable;
	}

	public void setUseable(Integer useable) {
		this.useable = useable;
	}

	public Timestamp getLastUsed() {
		return this.lastUsed;
	}

	public void setLastUsed(Timestamp lastUsed) {
		this.lastUsed = lastUsed;
	}

	public Timestamp getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Timestamp addTime) {
		this.addTime = addTime;
	}

	public Timestamp getRefreshTime() {
		return this.refreshTime;
	}

	public void setRefreshTime(Timestamp refreshTime) {
		this.refreshTime = refreshTime;
	}

}