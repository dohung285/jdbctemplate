package com.example.jdbctemplate;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.bind.DatatypeConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;

import com.cyber.utils.FileUtils;
import com.cyber.utils.Utils;
import com.example.jdbctemplate.model.DataOutput;
import com.example.jdbctemplate.repository.DataOutputRepository;
import com.example.jdbctemplate.request.Dschitiet;
import com.example.jdbctemplate.request.ReuqestObj;
import com.google.gson.Gson;

@SpringBootApplication
public class JdbctemplateApplication extends SpringBootServletInitializer implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(JdbctemplateApplication.class);

	@Autowired
	DataOutputRepository dataOutputRepository;

	@Autowired
	Environment env;

	private static final String FILENAME = "D:/maxOutNum.txt";
//	private static final String FILEmaHD = "C:/LogAppJDBCTemplate/maHoaDon.txt";

//	private static final String FILENAME = "maxOutNum.txt";
	private static final String FILEmaHD = "maHoaDon.txt";
	private static final String FILEtoken = "D:/token.txt";

//	@Autowired
//	RestTemplate restTemplate;    

	public static void main(String[] args) {
		SpringApplication.run(JdbctemplateApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		myRunMethod();
		
		//saveDataIntoDatabase();

	}
	
	// Auto callback once every 5 minutes = 5 * 60 * 1000 millis
	@Scheduled(fixedRate = 30000L)
	private void myRunMethod() {
		logger.info("*************************************: Bat dau chay app");

		// lan dau tien chay ghi gia tri ra file
		File file = new File(FILENAME);
		List<DataOutput> listSource = null;

		try {

			if (!file.exists()) {
				logger.info("***************: File maxOutNum.txt chua ton tai. line: 74 ");
				file.createNewFile();
				long maxOutNum = dataOutputRepository.getMaxSequence();
				FileUtils.writeFile(file, false, String.valueOf(maxOutNum));
				// CALL API DE LAY DU LIEU
				logger.info("***************: CALL API dataOutputRepository.getData(maxOutNum, 0L). ");
				listSource = dataOutputRepository.getData(maxOutNum, 0L);

			} else {
				logger.info("***************: File maxOutNum.txt da ton tai. ");
				//String strMaxOutNum = FileUtils.readFile(file);
				String strMaxOutNum = org.apache.commons.io.FileUtils.readFileToString(file);
				long maxOldOutNum = 0;
				try {
					 maxOldOutNum = Long.parseLong(strMaxOutNum);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				long maxNewOutNum = dataOutputRepository.getMaxSequence();
				logger.info("***************: " + "OLD: " + maxOldOutNum + " NEW: " + maxNewOutNum);

				// Call API - BILL - lay dc listSource
				logger.info("***************: dataOutputRepository.getData(maxOldOutNum, maxNewOutNum). ");
				listSource = dataOutputRepository.getData(maxOldOutNum, maxNewOutNum);
			} // end else

			// List luu OutNum distinct
			List<BigDecimal> listout = new ArrayList<BigDecimal>();
			BigDecimal outindex = new BigDecimal(0);
			for (DataOutput item : listSource) {
				if (item.getOutNum().compareTo(outindex) != 0) {
					listout.add(item.getOutNum());
				}
				outindex = item.getOutNum();
			}
			int indexListOutNum = 0;
			for (BigDecimal itemoutnum : listout) {
				BigDecimal outNum = new BigDecimal(0);
				outNum = itemoutnum;

				List<DataOutput> itemlistofoutnum = listSource.stream()
						.filter(c -> c.getOutNum().compareTo(itemoutnum) == 0).collect(Collectors.toList());
				ReuqestObj bodyRequest = new ReuqestObj();

				bodyRequest.setDoanhnghiepMst("0303030303");
				bodyRequest.setLoaihoadonMa("03XKNB");
				bodyRequest.setMauso("03XKNB0/001");
				bodyRequest.setKyhieu("QQ/20E");

				File fileMHD = new File(FILEmaHD);
				// long maHD = Long.parseLong(FileUtils.readFile(fileMHD)) + 1;
				// bodyRequest.setMaHoadon(maHD + ""); // doc tu file
				bodyRequest.setMaHoadon(String.valueOf(listout.get(indexListOutNum)));
				// ghi lai vao file gia tri vua su dung
				FileUtils.writeFile(fileMHD, false, String.valueOf(listout.get(indexListOutNum)));

				if (Objects.isNull(itemlistofoutnum.get(0).getDat()) || itemlistofoutnum.get(0).getDat().isEmpty()) {
					logger.info("***************: DATA = NULL tai OUT_NUM := " + outNum);
				} else if (Objects.isNull(itemlistofoutnum.get(0).getNumberContract()) || itemlistofoutnum.get(0).getNumberContract().isEmpty()) {
					logger.info("***************: NUMBER_CONTRACT = NULL tai OUT_NUM := " + outNum);
				} else if (Objects.isNull(itemlistofoutnum.get(0).getDat()) || itemlistofoutnum.get(0).getDat().isEmpty()) {
					logger.info("***************: DATA = NULL tai OUT_NUM := " + outNum);
				} else if (Objects.isNull(itemlistofoutnum.get(0).getReason()) || itemlistofoutnum.get(0).getReason().isEmpty()) {
					logger.info("***************: RESON = NULL tai OUT_NUM := " + outNum);
				} else if (Objects.isNull(itemlistofoutnum.get(0).getForCompany()) || itemlistofoutnum.get(0).getForCompany().isEmpty()) {
					logger.info("***************: ForCompany = NULL tai OUT_NUM := " + outNum);
				} else {
					bodyRequest.setNgaylap(convertStringToStringFormatDate(itemlistofoutnum.get(0).getDat())); // convert
					bodyRequest.setVanchuyen_giaohang(itemlistofoutnum.get(0).getNumberContract());
					bodyRequest.setVanchuyenNgayxuat(convertStringToStringFormatDate(itemlistofoutnum.get(0).getDat()));
					bodyRequest.setVanchuyenKhoxuat(DatatypeConverter.printBase64Binary(itemlistofoutnum.get(0).getReason().getBytes()));
					bodyRequest.setVanchuyenKhonhap(DatatypeConverter.printBase64Binary(itemlistofoutnum.get(0).getForCompany().getBytes()));

					bodyRequest.setTongtienChuavat(0);
					bodyRequest.setTienthue(0);
					bodyRequest.setTongtienCovat(0);

					List<Dschitiet> lChitiet = new ArrayList<Dschitiet>();
					for (DataOutput itemfilter : itemlistofoutnum) {

						Dschitiet ct = new Dschitiet();
						if (
								Objects.isNull(itemfilter.getRemar()) || 
								Objects.isNull(itemfilter.getUom()) || 
								Objects.isNull(itemfilter.getQty()) ||
								itemfilter.getRemar().isEmpty() ||
								itemfilter.getUom().isEmpty()
								
							) {
							logger.info("***************:NULL o TB_OUT_N_DETAIL  tai OUT_NUM := " + outNum);
							return;

						} else {
							ct.setTen(itemfilter.getRemar());
							ct.setDonvitinh(itemfilter.getUom());
							ct.setSoluong(itemfilter.getQty());
							ct.setVanchuyen_loai(1);
						}

						lChitiet.add(ct);

					}
					bodyRequest.setDschitiet(lChitiet);

					String jsonbody = new Gson().toJson(bodyRequest);
					logger.info("***************: Thong tin gui di la:   "
							+ DatatypeConverter.printBase64Binary(jsonbody.getBytes()));
					File fileToken = new File(FILEtoken);
					if (!fileToken.exists()) {
						logger.info("***************: File token.txt chua ton tai");
						fileToken.createNewFile();
					}
					String result = Utils.connectServer(env.getProperty("urlGuiVaKyHoadonGocHSM"), jsonbody,
							" Bearer " + FileUtils.tokenWS);
					logger.info("***************: Ket qua CALL API- BILL :  " + result);

					if (!result.equals("3")) {

						JSONObject json = new JSONObject(result);
						JSONObject objectResult = new JSONObject();
						objectResult = json.getJSONObject("result");

						if (objectResult.get("mauso").equals(null) || objectResult.get("kyhieu").equals(null)
								|| objectResult.get("sohoadon").equals(null)
								|| objectResult.get("ngayky").equals(null)) {
							logger.error("***************: NULL line-204 ");
						} else {

							long outNumLong = outNum.longValue();
							try {
								int x = dataOutputRepository.update4Filed(
										DatatypeConverter.printBase64Binary(objectResult.optString(objectResult.getString("mauso"), "").getBytes()),
										DatatypeConverter.printBase64Binary(objectResult.optString(objectResult.getString("kyhieu"), "").getBytes()),
										objectResult.optString(objectResult.getString("sohoadon"), ""),
										new SimpleDateFormat("dd/MM/YYYY")
												.parse(objectResult.optString(objectResult.getString("ngayky"), "")),
										outNumLong);
								if (x > 0) {
									logger.info(
											"***************: Ket qua UPDATE 4 truong la:   " + x + " Thanh cong!!");
								} else {
									logger.error("***************: Ket qua UPDATE 4 truong la:   " + x + " That bai!!");
								}

							} catch (JSONException e) {
								logger.error("***************: Loi : " + e.getMessage() + "== " + e.getClass()
										+ "line: 170 ");
							} catch (ParseException e) {
								logger.error("***************: Loi : " + e.getMessage() + "== " + e.getClass()
										+ "line: 173 ");
							}
							logger.info("***************: Ket thuc :***********************");
							// Tang chi so cua indexOutNum len de ki lan tiep theo
							indexListOutNum++;
						}

					} // end if

				}

			}

		} catch (IOException e) {
			logger.error("***************: Loi : " + e.getMessage() + "== " + e.getClass() + "line: 179 ");
			e.printStackTrace();
		}

	}

	private void saveDataIntoDatabase() {
		// dataOutputRepository.saveDetail(2, 2, 2, 2);
		for (int i = 20030; i <= 20040; i++) {
			dataOutputRepository.saveHeader(i);

			for (int j = 1; j < 10; j++) {
				int x = j;
				int y = j;
				dataOutputRepository.saveDetail(j, i, x, y);
			}

		}
		System.out.println("DONE!");

	}

	public boolean isJSONValid(String test) {
		try {
			new JSONObject(test);
		} catch (JSONException ex) {
			try {
				new JSONArray(test);
			} catch (JSONException ex1) {
				return false;
			}
		}
		return true;
	}

	// Chuyen dinh dang chuyen 20201010 ==> 2020 - 10 -10
	private String convertStringToStringFormatDate(String dat) {

		String formattedDate = null;
		try {
			DateFormat originalFormat = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
			DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = originalFormat.parse(dat);
			formattedDate = targetFormat.format(date);
		} catch (ParseException e) {
			logger.error("***************: Loi : " + e.getMessage() + "== " + e.getClass() + "line: 208 ");
			e.printStackTrace();
		}
		return formattedDate;
	}
}
