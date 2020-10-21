package com.example.jdbctemplate.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestAccount {

	@JsonProperty("doanhnghiep_mst")
	private String doanhnghiep_mst;

	@JsonProperty("username")
	private String username;

	@JsonProperty("password")
	private String password;

	public RequestAccount() {
		super();
	}

	public String getDoanhnghiepMst() {
		return doanhnghiep_mst;
	}

	public void setDoanhnghiepMst(String doanhnghiepMst) {
		this.doanhnghiep_mst = doanhnghiepMst;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
