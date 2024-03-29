package com.example.jdbctemplate.repository;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.jdbctemplate.model.DataOutput;

@Repository
public class DataOutputRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	
////	 Update 4 truong theo yeu cau
////	ACC_MODEL 	ACC_SYMBOL	ACC_NUMBER 	ACC_DATE
//	public int update4Filed(String accModel, String accSymbol, String accNumberl, Date accDate, long outNum) {
//		String sql = "UPDATE MES.tb_out_header SET acc_model=?, acc_symbol=?,acc_number=?,acc_date=? WHERE out_num=?";
//		return jdbcTemplate.update(sql, new Object[] { accModel, accSymbol, accNumberl, accDate, outNum });
//	}
////
//	// get max value of sequence
//	public Long getMaxSequence() {
//		String sql = "SELECT max(out_num) from MES.TB_OUT_HEADER";
//		return jdbcTemplate.queryForObject(sql, Long.class).longValue();
//	}
//
//	public List<DataOutput> getData(long maxOldOutNum, long maxNewOutNum) {
//
//		StringBuilder builder = new StringBuilder("");
//
//		builder.append("	select 	A.DAT						");
//		builder.append("	        ,A.OUT_NUM							");
//		builder.append("			, 'CÔNG TY TNHH VIỆT NAM SAMHO'  AS XUAT_KHO					");
//		builder.append("			,A.REASON					");
//		builder.append("			,A.FOR_COMPANY 	");
//		builder.append("			,A.NUMBER_CAR					");
//		builder.append("			,A.NUMBER_CONTRACT					");
//		builder.append("			,B.REMAR					");
//		builder.append("			,B.UOM					");
//		builder.append("			,A.ACC_FLAG					");
//		builder.append("			,A.PRINT_FLAG					");
//		builder.append("	        , SUM (b.qty)	as QTY						");
//		builder.append("		from MES.TB_OUT_HEADER A, MES.TB_OUT_N_DETAIL B						");
//		builder.append("	WHERE A.OUT_NUM = B.OUT_NUM							");
//		builder.append("	AND A.ACC_FLAG = 'Y'							");
//		builder.append("	AND A.PRINT_FLAG = 'Y'							");
//		builder.append(" 	AND A.ACC_MODEL IS  NULL	");
//		builder.append(" 	AND A.ACC_SYMBOL IS  NULL 	");
//		builder.append(" 	AND A.ACC_NUMBER IS  NULL 	");
//		builder.append(" 	AND A.ACC_DATE IS  NULL ");
//
//		builder.append("	AND A.OUT_NUM <= ?	");
//		builder.append("		GROUP BY 	");
//		builder.append("	         A.DAT	");
//		builder.append("	        ,A.OUT_NUM	");
//		builder.append("			,A.REASON	");
//		builder.append("			,A.FOR_COMPANY 	");
//		builder.append("			,A.NUMBER_CAR	");
//		builder.append("			,A.NUMBER_CONTRACT	");
//		builder.append("			,B.REMAR			");
//		builder.append("			,B.UOM				");
//		builder.append("	        ,A.ACC_FLAG			");
//		builder.append("			,A.PRINT_FLAG		");
//		return jdbcTemplate.query(builder.toString(), new BeanPropertyRowMapper(DataOutput.class),
//				new Object[] { maxOldOutNum });
//
//	}



	//	 Update 4 truong theo yeu cau
//	ACC_MODEL 	ACC_SYMBOL	ACC_NUMBER 	ACC_DATE
	public int update4Filed(String accModel, String accSymbol, String accNumberl, Date accDate, long outNum) {
		String sql = "UPDATE tb_out_header SET acc_model=?, acc_symbol=?,acc_number=?,acc_date=? WHERE out_num=?";
		return jdbcTemplate.update(sql, new Object[] { accModel, accSymbol, accNumberl, accDate, outNum });
	}
	//
	// get max value of sequence
	public Long getMaxSequence() {
		String sql = "SELECT max(out_num) from TB_OUT_HEADER";
		return jdbcTemplate.queryForObject(sql, Long.class).longValue();
	}

	public List<DataOutput> getData(long maxOldOutNum, long maxNewOutNum) {

		StringBuilder builder = new StringBuilder("");

		builder.append("	select 	A.DAT						");
		builder.append("	        ,B.OUT_LINE_NUM							");
		builder.append("	        ,A.OUT_NUM							");
		builder.append("			, 'CÔNG TY TNHH VIỆT NAM SAMHO'  AS XUAT_KHO					");
		builder.append("			,A.REASON					");
		builder.append("			,A.FOR_COMPANY 	");
		builder.append("			,A.NUMBER_CAR					");
		builder.append("			,A.NUMBER_CONTRACT					");
		builder.append("			,B.REMAR					");
		builder.append("			,B.UOM					");
		builder.append("			,A.ACC_FLAG					");
		builder.append("			,A.PRINT_FLAG					");
		builder.append("	        , SUM(b.qty)	as QTY						");
		builder.append("		from TB_OUT_HEADER A, TB_OUT_N_DETAIL B						");
		builder.append("	WHERE A.OUT_NUM = B.OUT_NUM							");
		builder.append("	AND A.ACC_FLAG = 'Y'							");
		builder.append("	AND A.PRINT_FLAG = 'Y'							");
		builder.append(" 	AND A.ACC_MODEL IS  NULL	");
		builder.append(" 	AND A.ACC_SYMBOL IS  NULL 	");
		builder.append(" 	AND A.ACC_NUMBER IS  NULL 	");
		builder.append(" 	AND A.ACC_DATE IS  NULL ");

		builder.append("	AND A.OUT_NUM <= ?	");
		builder.append("		GROUP BY 	");
		builder.append("	         A.DAT	");
		builder.append("	        ,A.OUT_NUM	");
		builder.append("			,A.REASON	");
		builder.append("			,A.FOR_COMPANY 	");
		builder.append("			,A.NUMBER_CAR	");
		builder.append("			,A.NUMBER_CONTRACT	");
		builder.append("			,B.REMAR			");
		builder.append("			,B.UOM				");
		builder.append("	        ,A.ACC_FLAG			");
		builder.append("			,A.PRINT_FLAG		");
		return jdbcTemplate.query(builder.toString(), new BeanPropertyRowMapper(DataOutput.class),
				new Object[] { maxOldOutNum });

	}

	
	
	
	
	
	
	




//	// Update 4 truong theo yeu cau
////	ACC_MODEL 	ACC_SYMBOL	ACC_NUMBER 	ACC_DATE
//	public int update4Filed(String accModel, String accSymbol, String accNumberl, Date accDate, long outNum) {
//		String sql = "UPDATE tb_out_header SET acc_model=?, acc_symbol=?,acc_number=?,acc_date=? WHERE out_num=?";
//		return jdbcTemplate.update(sql, new Object[] { accModel, accSymbol, accNumberl, accDate, outNum });
//	}
//
//	// get max value of sequence
//	public Long getMaxSequence() {
//		String sql = "SELECT max(out_num) from TB_OUT_HEADER";
//		return jdbcTemplate.queryForObject(sql, Long.class).longValue();
//	}
//
//	public List<DataOutput> getData(long maxOldOutNum, long maxNewOutNum) {
//		StringBuilder builder = new StringBuilder("");
//		builder.append("	select 	A.DAT						");
//		builder.append("	        ,B.OUT_LINE_NUM							");
//		builder.append("	        ,A.OUT_NUM							");
//		builder.append("			, 'CONG TY TNHH VIEÄT NAM SAMHO'  AS XUAT_KHO					");
//		builder.append("			,A.REASON					");
//		builder.append("			,A.FOR_COMPANY 	");
//		builder.append("			,A.NUMBER_CAR					");
//		builder.append("			,A.NUMBER_CONTRACT					");
//		builder.append("			,B.REMAR					");
//		builder.append("			,B.UOM					");
//		builder.append("			,A.ACC_FLAG					");
//		builder.append("			,A.PRINT_FLAG					");
//		builder.append("	        , SUM (b.qty)	as QTY						");
//		builder.append("		from TB_OUT_HEADER A, TB_OUT_N_DETAIL B						");
//		builder.append("	WHERE A.OUT_NUM = B.OUT_NUM							");
//		builder.append("	AND A.ACC_FLAG = 'Y'							");
//		builder.append("	AND A.PRINT_FLAG = 'Y'							");
//		builder.append(" 	AND A.ACC_MODEL IS  NULL	");
//		builder.append(" 	AND A.ACC_SYMBOL IS  NULL 	");
//		builder.append(" 	AND A.ACC_NUMBER IS  NULL 	");
//		builder.append(" 	AND A.ACC_DATE IS  NULL ");
//		builder.append("	AND A.OUT_NUM <= ? AND 	");
//		builder.append("		GROUP BY 	");
//		builder.append("	         A.DAT	");
//		builder.append("	        ,A.OUT_NUM	");
//		builder.append("			,A.REASON	");
//		builder.append("			,A.FOR_COMPANY 	");
//		builder.append("			,A.NUMBER_CAR	");
//		builder.append("			,A.NUMBER_CONTRACT	");
//		builder.append("			,B.REMAR			");
//		builder.append("			,B.UOM				");
//		builder.append("	        ,A.ACC_FLAG			");
//		builder.append("			,A.PRINT_FLAG		");
//		System.out.println("builder: "+builder.toString());
//		return jdbcTemplate.query(builder.toString(), new BeanPropertyRowMapper(DataOutput.class),
//				new Object[] { maxOldOutNum });
//	}







}
