package com.example.jdbctemplate.request;

public class DataRequestDetail {
	private String ten;
	private String donViTinh;
	private long soLuong;

	public DataRequestDetail() {
		super();
	}

	public DataRequestDetail(String ten, String donViTinh, Integer soLuong) {
		super();
		this.ten = ten;
		this.donViTinh = donViTinh;
		this.soLuong = soLuong;
	}

	public String getTen() {
		return ten;
	}

	public void setTen(String ten) {
		this.ten = ten;
	}

	public String getDonViTinh() {
		return donViTinh;
	}

	public void setDonViTinh(String donViTinh) {
		this.donViTinh = donViTinh;
	}

	public long getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(long soLuong) {
		this.soLuong = soLuong;
	}

}
