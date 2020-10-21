//package com.example.jdbctemplate.map;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import org.springframework.jdbc.core.RowMapper;
//
//import com.example.jdbctemplate.model.DataOutput;
//
//public class DataOutputRowMap implements RowMapper<DataOutput> {
//
//	@Override
//	public DataOutput mapRow(ResultSet rs, int rowNum) throws SQLException {
//		DataOutput dataOutput = new DataOutput();
//
//		dataOutput.setDat(rs.getNString("DAT"));
//
//		dataOutput.setOutNum(rs.getBigDecimal("OUT_NUM"));
//
//		dataOutput.setReason(rs.getNString("REASON"));
//
//		dataOutput.setForCompany(rs.getNString("NHAP_KHO")); //FOR_COMPANY
//
//		dataOutput.setUom(rs.getNString("UOM"));
//
//		dataOutput.setQty(rs.getBigDecimal("QTY"));
//
//		dataOutput.setRemar(rs.getNString("REMAR"));
//
//		dataOutput.setNumberContract(rs.getNString("NUMBER_CONTRACT"));
//
//		dataOutput.setAccFlag(rs.getNString("ACC_FLAG"));
//
//		dataOutput.setPrintFlag(rs.getNString("PRINT_FLAG"));
//		dataOutput.setSum(rs.getInt("SUM"));
//
//		return dataOutput;
//	}
//
//}
