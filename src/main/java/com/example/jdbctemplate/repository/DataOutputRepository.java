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

	public int saveHeader(int i) {

		StringBuilder builders = new StringBuilder("");
		builders.append("	INSERT INTO MES.tb_out_header VALUES (	");
		builders.append("	    '20201021',							");
		builders.append("	   " + i + ",							");
		builders.append("	    'PC',							");
		builders.append("	    'GIA CÔNG (OUTSOURCING)',							");
		builders.append("	    'Công ty TNHH VN SamHo',							");
		builders.append("	    'OUTED',							");
		builders.append("	    'PC.LINH',							");
		builders.append("	    TO_DATE('2020/10/21 8:30:25', 'yyyy/mm/dd hh24:mi:ss'),							");
		builders.append("	    'Y',							");
		builders.append("	    'ACC.HANH',							");
		builders.append("	    TO_DATE('2020/10/21 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),							");
		builders.append("	    'SYSTEM    ',							");
		builders.append("	    '4464',							");
		builders.append("	    '',							");
		builders.append("	    '',							");
		builders.append("	    '',							");
		builders.append("	    '',							");
		builders.append("	    '',							");
		builders.append("	    'Y',							");
		builders.append("	    '61C-34132',							");
		builders.append("	    'SH-RT 1-2020'	)						");

		return jdbcTemplate.update(builders.toString());
	}

	public int saveDetail(int i, int indexHeader, int x, int y) {

		StringBuilder strBuilder = new StringBuilder("");
		strBuilder.append(
				"	INSERT INTO MES.tb_out_n_detail (OUT_LINE_NUM,OUT_NUM,OUT_NUM_DETAIL,ITEM_CODE,DESCRIPTION,UOM,QTY,EXCHANGE,REMAR,IN_ID,IN_DATE,PROG_ID,RETURN_QTY,RETURN_ID,RETURN_DATE,RENEW_ID,RENEW_DATE,ACC_FLAG,ACC_ID)  VALUES ( ");
		strBuilder.append("	        " + i + ",				");
		strBuilder.append("	        " + indexHeader + ",				");
		strBuilder.append("	        " + x + ",				");
		strBuilder.append("	        '',				");
		strBuilder.append("	        'M-Y6-TEST-01',				");
		strBuilder.append("	        'PRS',				");
		strBuilder.append("	        " + i * 10 + ",				");
		strBuilder.append("	        '',				");
		strBuilder.append("	        'REMAR-TEST-01',				");
		strBuilder.append("	        'PC.LINH',				");
		strBuilder.append("	        TO_DATE('2020/10/21 8:30:25', 'yyyy/mm/dd hh24:mi:ss'),				");
		strBuilder.append("	        'w_puor241r',				");
		strBuilder.append("	        " + 0 + ",				");
		strBuilder.append("	        '',				");
		strBuilder.append("	        TO_DATE('2020/10/21 8:30:25', 'yyyy/mm/dd hh24:mi:ss'),				");
		strBuilder.append("	        'ACC.HANH',				");
		strBuilder.append("	        TO_DATE('2020/10/21 8:30:25', 'yyyy/mm/dd hh24:mi:ss'),				");
		strBuilder.append("	        'Y',				");
		strBuilder.append("	        'ACC.HANH'				");
		strBuilder.append("	    )				");

		return jdbcTemplate.update(strBuilder.toString());
	}

	// Update 4 truong theo yeu cau
//	ACC_MODEL 	ACC_SYMBOL	ACC_NUMBER 	ACC_DATE
	public int update4Filed(String accModel, String accSymbol, String accNumberl, Date accDate, long outNum) {
		String sql = "UPDATE MES.tb_out_header SET acc_model=?, acc_symbol=?,acc_number=?,acc_date=? WHERE out_num=?";
		return jdbcTemplate.update(sql, new Object[] { accModel, accSymbol, accNumberl, accDate, outNum });
	}

	// get max value of sequence
	public Long getMaxSequence() {
		String sql = "SELECT max(out_num) from MES.TB_OUT_HEADER";
		return jdbcTemplate.queryForObject(sql, Long.class).longValue();
	}

	public List<DataOutput> getData(long maxOldOutNum, long maxNewOutNum) {

		StringBuilder builder = new StringBuilder("");

		builder.append("	select 	A.DAT						");
		builder.append("	        ,A.OUT_NUM							");
		builder.append("			, 'CONG TY TNHH VIEÄT NAM SAMHO'  AS XUAT_KHO					");
		builder.append("			,A.REASON					");
		builder.append("			,A.FOR_COMPANY 	");
		builder.append("			,A.NUMBER_CAR					");
		builder.append("			,A.NUMBER_CONTRACT					");
		builder.append("			,B.REMAR					");
		builder.append("			,B.UOM					");
		builder.append("			,A.ACC_FLAG					");
		builder.append("			,A.PRINT_FLAG					");
		builder.append("	        , SUM (b.qty)	as QTY						");
		builder.append("		from MES.TB_OUT_HEADER A, MES.TB_OUT_N_DETAIL B						");
		builder.append("	WHERE A.OUT_NUM = B.OUT_NUM							");
		builder.append("	AND A.ACC_FLAG = 'Y'							");
		builder.append("	AND A.PRINT_FLAG = 'Y'							");
		builder.append(" 	AND A.ACC_MODEL IS  NULL	");
		builder.append(" 	AND A.ACC_SYMBOL IS  NULL 	");
		builder.append(" 	AND A.ACC_NUMBER IS  NULL 	");
		builder.append(" 	AND A.ACC_DATE IS  NULL ");

		if (maxNewOutNum == 0L) {
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
		} else {
			builder.append(" AND A.OUT_NUM BETWEEN ? AND ? ");

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

			System.out.println(builder.toString());


//			String test = "SELECT\r\n" + "    a.dat,\r\n" + "    a.out_num,\r\n"
//					+ "    'CONG TY TNHH VIEÄT NAM SAMHO' AS xuat_kho,\r\n" + "    a.reason,\r\n"
//					+ "    a.for_company,\r\n" + "    a.number_car,\r\n" + "    a.number_contract,\r\n"
//					+ "    b.remar,\r\n" + "    b.uom,\r\n" + "    a.acc_flag,\r\n" + "    a.print_flag,\r\n"
//					+ "    SUM(b.qty) as qty\r\n" + "FROM\r\n" + "    tb_out_header     a,\r\n"
//					+ "    tb_out_n_detail   b\r\n" + "WHERE\r\n" + "    a.out_num = b.out_num\r\n"
//					+ "    AND a.acc_flag = 'Y'\r\n" + "    AND a.print_flag = 'Y'\r\n"
//					+ "    AND a.out_num BETWEEN 7092 AND 7092\r\n" + "GROUP BY\r\n" + "    a.dat,\r\n"
//					+ "    a.out_num,\r\n" + "    a.reason,\r\n" + "    a.for_company,\r\n" + "    a.number_car,\r\n"
//					+ "    a.number_contract,\r\n" + "    b.remar,\r\n" + "    b.uom,\r\n" + "    a.acc_flag,\r\n"
//					+ "    a.print_flag";

			// return jdbcTemplate.query(test.toString(), new
			// BeanPropertyRowMapper(DataOutput.class));
			// return jdbcTemplate.queryForList(builder.toString(), new
			// BeanPropertyRowMapper(DataOutput.class), new Object[] { maxOldOutNum,
			// maxNewOutNum });
			return jdbcTemplate.query(builder.toString(), new BeanPropertyRowMapper(DataOutput.class),
					new Object[] { maxOldOutNum, maxNewOutNum });
		}

//		List<DataOutput> query = jdbcTemplate.query(builder.toString(), new BeanPropertyRowMapper<DataOutput>(DataOutput.class), maxOutNum);
//	    return (List<DataOutput>) DataAccessUtils.uniqueResult(query);

	}


//	public int saveHeader(int i) {
//
//		StringBuilder builders = new StringBuilder("");
//		builders.append("	INSERT INTO tb_out_header VALUES (	");
//		builders.append("	    '20201021',							");
//		builders.append("	   " + i + ",							");
//		builders.append("	    'PC',							");
//		builders.append("	    'GIA CÔNG (OUTSOURCING)',							");
//		builders.append("	    'Công ty TNHH VN SamHo',							");
//		builders.append("	    'OUTED',							");
//		builders.append("	    'PC.LINH',							");
//		builders.append("	    TO_DATE('2020/10/21 8:30:25', 'yyyy/mm/dd hh24:mi:ss'),							");
//		builders.append("	    'Y',							");
//		builders.append("	    'ACC.HANH',							");
//		builders.append("	    TO_DATE('2020/10/21 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),							");
//		builders.append("	    'SYSTEM    ',							");
//		builders.append("	    '4464',							");
//		builders.append("	    '',							");
//		builders.append("	    '',							");
//		builders.append("	    '',							");
//		builders.append("	    '',							");
//		builders.append("	    '',							");
//		builders.append("	    'Y',							");
//		builders.append("	    '61C-34132',							");
//		builders.append("	    'SH-RT 1-2020'	)						");
//
//		return jdbcTemplate.update(builders.toString());
//	}
//
//	public int saveDetail(int i, int indexHeader, int x, int y) {
//
//		StringBuilder strBuilder = new StringBuilder("");
//		strBuilder.append(
//				"	INSERT INTO tb_out_n_detail (OUT_LINE_NUM,OUT_NUM,OUT_NUM_DETAIL,ITEM_CODE,DESCRIPTION,UOM,QTY,EXCHANGE,REMAR,IN_ID,IN_DATE,PROG_ID,RETURN_QTY,RETURN_ID,RETURN_DATE,RENEW_ID,RENEW_DATE,ACC_FLAG,ACC_ID)  VALUES ( ");
//		strBuilder.append("	        " + i + ",				");
//		strBuilder.append("	        " + indexHeader + ",				");
//		strBuilder.append("	        " + x + ",				");
//		strBuilder.append("	        '',				");
//		strBuilder.append("	        'M-Y6-TEST-01',				");
//		strBuilder.append("	        'PRS',				");
//		strBuilder.append("	        " + i * 10 + ",				");
//		strBuilder.append("	        '',				");
//		strBuilder.append("	        'REMAR-TEST-01',				");
//		strBuilder.append("	        'PC.LINH',				");
//		strBuilder.append("	        TO_DATE('2020/10/21 8:30:25', 'yyyy/mm/dd hh24:mi:ss'),				");
//		strBuilder.append("	        'w_puor241r',				");
//		strBuilder.append("	        " + 0 + ",				");
//		strBuilder.append("	        '',				");
//		strBuilder.append("	        TO_DATE('2020/10/21 8:30:25', 'yyyy/mm/dd hh24:mi:ss'),				");
//		strBuilder.append("	        'ACC.HANH',				");
//		strBuilder.append("	        TO_DATE('2020/10/21 8:30:25', 'yyyy/mm/dd hh24:mi:ss'),				");
//		strBuilder.append("	        'Y',				");
//		strBuilder.append("	        'ACC.HANH'				");
//		strBuilder.append("	    )				");
//
//		return jdbcTemplate.update(strBuilder.toString());
//	}
//
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
//
//		StringBuilder builder = new StringBuilder("");
//
//		builder.append("	select 	A.DAT						");
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
//
//		if (maxNewOutNum == 0L) {
//			builder.append("	AND A.OUT_NUM <= ?	");
//			builder.append("		GROUP BY 	");
//			builder.append("	         A.DAT	");
//			builder.append("	        ,A.OUT_NUM	");
//			builder.append("			,A.REASON	");
//			builder.append("			,A.FOR_COMPANY 	");
//			builder.append("			,A.NUMBER_CAR	");
//			builder.append("			,A.NUMBER_CONTRACT	");
//			builder.append("			,B.REMAR			");
//			builder.append("			,B.UOM				");
//			builder.append("	        ,A.ACC_FLAG			");
//			builder.append("			,A.PRINT_FLAG		");
//			return jdbcTemplate.query(builder.toString(), new BeanPropertyRowMapper(DataOutput.class),
//					new Object[] { maxOldOutNum });
//		} else {
//			builder.append(" AND A.OUT_NUM BETWEEN ? AND ? ");
//
//			builder.append("		GROUP BY 	");
//			builder.append("	         A.DAT	");
//			builder.append("	        ,A.OUT_NUM	");
//			builder.append("			,A.REASON	");
//			builder.append("			,A.FOR_COMPANY 	");
//			builder.append("			,A.NUMBER_CAR	");
//			builder.append("			,A.NUMBER_CONTRACT	");
//			builder.append("			,B.REMAR			");
//			builder.append("			,B.UOM				");
//			builder.append("	        ,A.ACC_FLAG			");
//			builder.append("			,A.PRINT_FLAG		");
//
//			System.out.println(builder.toString());
////			System.out.println("NOT NULL");
//
//
//			return jdbcTemplate.query(builder.toString(), new BeanPropertyRowMapper(DataOutput.class),
//					new Object[] { maxOldOutNum, maxNewOutNum });
//		}
//	}







}
