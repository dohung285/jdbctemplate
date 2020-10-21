package com.example.jdbctemplate.model;

import java.math.BigDecimal;

public class DataOutput {

	private String dat;
	private BigDecimal outNum;
	private String reason;
	private String forCompany;
	private String uom;
	private Integer qty;
	private String remar;
	private String numberContract;
	private String accFlag;
	private String printFlag;
	private Integer sum;

	public DataOutput() {
		super();
	}
	
	

	public String getDat() {
		return dat;
	}

	public void setDat(String dat) {
		this.dat = dat;
	}

	public BigDecimal getOutNum() {
		return outNum;
	}

	public void setOutNum(BigDecimal outNum) {
		this.outNum = outNum;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getForCompany() {
		return forCompany;
	}

	public void setForCompany(String forCompany) {
		this.forCompany = forCompany;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public String getRemar() {
		return remar;
	}

	public void setRemar(String remar) {
		this.remar = remar;
	}

	public String getNumberContract() {
		return numberContract;
	}

	public void setNumberContract(String numberContract) {
		this.numberContract = numberContract;
	}

	public String getAccFlag() {
		return accFlag;
	}

	public void setAccFlag(String accFlag) {
		this.accFlag = accFlag;
	}

	public String getPrintFlag() {
		return printFlag;
	}

	public void setPrintFlag(String printFlag) {
		this.printFlag = printFlag;
	}

	public Integer getSum() {
		return sum;
	}

	public void setSum(Integer sum) {
		this.sum = sum;
	}

	@Override
	public String toString() {
		return "DataOutput [dat=" + dat + ", outNum=" + outNum + ", reason=" + reason + ", forCompany=" + forCompany
				+ ", uom=" + uom + ", qty=" + qty + ", remar=" + remar + ", numberContract=" + numberContract
				+ ", accFlag=" + accFlag + ", printFlag=" + printFlag + ", sum=" + sum + "]";
	}

	

}
