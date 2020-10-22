package com.example.jdbctemplate.request;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Dschitiet {

	@JsonProperty("ten")
	private String ten;
	@JsonProperty("donvitinh")
	private String donvitinh;
	@JsonProperty("soluong")
	private Integer soluong;

	@JsonProperty("vanchuyen_loai")
	private Integer vanchuyen_loai;

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("vanchuyen_loai")
	public Integer getVanchuyen_loai() {
		return vanchuyen_loai;
	}

	@JsonProperty("vanchuyen_loai")
	public void setVanchuyen_loai(Integer vanchuyen_loai) {
		this.vanchuyen_loai = vanchuyen_loai;
	}

	@JsonProperty("ten")
	public String getTen() {
		return ten;
	}

	@JsonProperty("ten")
	public void setTen(String ten) {
		this.ten = ten;
	}

	@JsonProperty("donvitinh")
	public String getDonvitinh() {
		return donvitinh;
	}

	@JsonProperty("donvitinh")
	public void setDonvitinh(String donvitinh) {
		this.donvitinh = donvitinh;
	}

	@JsonProperty("soluong")
	public Integer getSoluong() {
		return soluong;
	}

	@JsonProperty("soluong")
	public void setSoluong(Integer soluong) {
		this.soluong = soluong;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}