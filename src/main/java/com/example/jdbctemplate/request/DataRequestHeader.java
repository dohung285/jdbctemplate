package com.example.jdbctemplate.request;

import java.util.Date;
import java.util.List;

public class DataRequestHeader {
	private String danhNghiepMST;

	private String loaiHoaDonMa;
	private String mauSo;
	private String kyHieu;
	private long maHoaDon;
	
	private Date ngayLap;
	private String vanChuyenSo;
	private Date vanChuyenNgayXuat;
	private String vanChuyenKhoXuat;
	private String vanChuyenKhoNhap;
	private String tongTienChuaVAT;
	private String tienThue;
	private String tongTienCoVAT;

	private List<DataRequestDetail> details;

	public DataRequestHeader() {
		super();
	}

	public DataRequestHeader(String danhNghiepMST, String loaiHoaDonMa, String mauSo, String kyHieu, long maHoaDon,
			Date ngayLap, String vanChuyenSo, Date vanChuyenNgayXuat, String vanChuyenKhoXuat, String vanChuyenKhoNhap,
			String tongTienChuaVAT, String tienThue, String tongTienCoVAT, List<DataRequestDetail> details) {
		super();
		this.danhNghiepMST = danhNghiepMST;
		this.loaiHoaDonMa = loaiHoaDonMa;
		this.mauSo = mauSo;
		this.kyHieu = kyHieu;
		this.maHoaDon = maHoaDon;
		this.ngayLap = ngayLap;
		this.vanChuyenSo = vanChuyenSo;
		this.vanChuyenNgayXuat = vanChuyenNgayXuat;
		this.vanChuyenKhoXuat = vanChuyenKhoXuat;
		this.vanChuyenKhoNhap = vanChuyenKhoNhap;
		this.tongTienChuaVAT = tongTienChuaVAT;
		this.tienThue = tienThue;
		this.tongTienCoVAT = tongTienCoVAT;
		this.details = details;
	}

	public String getDanhNghiepMST() {
		return danhNghiepMST;
	}

	public void setDanhNghiepMST(String danhNghiepMST) {
		this.danhNghiepMST = danhNghiepMST;
	}

	public String getLoaiHoaDonMa() {
		return loaiHoaDonMa;
	}

	public void setLoaiHoaDonMa(String loaiHoaDonMa) {
		this.loaiHoaDonMa = loaiHoaDonMa;
	}

	public String getMauSo() {
		return mauSo;
	}

	public void setMauSo(String mauSo) {
		this.mauSo = mauSo;
	}

	public String getKyHieu() {
		return kyHieu;
	}

	public void setKyHieu(String kyHieu) {
		this.kyHieu = kyHieu;
	}

	public long getMaHoaDon() {
		return maHoaDon;
	}

	public void setMaHoaDon(long maHoaDon) {
		this.maHoaDon = maHoaDon;
	}

	public Date getNgayLap() {
		return ngayLap;
	}

	public void setNgayLap(Date ngayLap) {
		this.ngayLap = ngayLap;
	}

	public String getVanChuyenSo() {
		return vanChuyenSo;
	}

	public void setVanChuyenSo(String vanChuyenSo) {
		this.vanChuyenSo = vanChuyenSo;
	}

	public Date getVanChuyenNgayXuat() {
		return vanChuyenNgayXuat;
	}

	public void setVanChuyenNgayXuat(Date vanChuyenNgayXuat) {
		this.vanChuyenNgayXuat = vanChuyenNgayXuat;
	}

	public String getVanChuyenKhoXuat() {
		return vanChuyenKhoXuat;
	}

	public void setVanChuyenKhoXuat(String vanChuyenKhoXuat) {
		this.vanChuyenKhoXuat = vanChuyenKhoXuat;
	}

	public String getVanChuyenKhoNhap() {
		return vanChuyenKhoNhap;
	}

	public void setVanChuyenKhoNhap(String vanChuyenKhoNhap) {
		this.vanChuyenKhoNhap = vanChuyenKhoNhap;
	}

	public String getTongTienChuaVAT() {
		return tongTienChuaVAT;
	}

	public void setTongTienChuaVAT(String tongTienChuaVAT) {
		this.tongTienChuaVAT = tongTienChuaVAT;
	}

	public String getTienThue() {
		return tienThue;
	}

	public void setTienThue(String tienThue) {
		this.tienThue = tienThue;
	}

	public String getTongTienCoVAT() {
		return tongTienCoVAT;
	}

	public void setTongTienCoVAT(String tongTienCoVAT) {
		this.tongTienCoVAT = tongTienCoVAT;
	}

	public List<DataRequestDetail> getDetails() {
		return details;
	}

	public void setDetails(List<DataRequestDetail> details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return "DataRequestHeader [danhNghiepMST=" + danhNghiepMST + ", loaiHoaDonMa=" + loaiHoaDonMa + ", mauSo="
				+ mauSo + ", kyHieu=" + kyHieu + ", maHoaDon=" + maHoaDon + ", ngayLap=" + ngayLap + ", vanChuyenSo="
				+ vanChuyenSo + ", vanChuyenNgayXuat=" + vanChuyenNgayXuat + ", vanChuyenKhoXuat=" + vanChuyenKhoXuat
				+ ", vanChuyenKhoNhap=" + vanChuyenKhoNhap + ", tongTienChuaVAT=" + tongTienChuaVAT + ", tienThue="
				+ tienThue + ", tongTienCoVAT=" + tongTienCoVAT + ", details=" + details + "]";
	}

}
