package com.example.jdbctemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.xml.bind.DatatypeConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;

import com.cyber.utils.FileUtils;
import com.cyber.utils.GlobalValue;
import com.cyber.utils.TokenUtils;
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

	@Value("${fileMaxOutNum}")
	private String FILENAME;

	@Value("${fileMaHD}")
	private String FILEmaHD;

	@Value("${urlGetToken}")
	private String urlToken;

	public static void main(String[] args) {
		SpringApplication.run(JdbctemplateApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		GlobalValue goGlobalValue = new GlobalValue();
//		goGlobalValue.setUrlToken(env.getProperty("urlGetToken")); // ,,
//		goGlobalValue.setPassword(env.getProperty("mstPassword"));
//		goGlobalValue.setUsername(env.getProperty("mstUsername"));
//		goGlobalValue.setDoanhnghiepMstStatic(env.getProperty("doanhnghiepMst"));
//
//		System.out.println("TOKEN: " + goGlobalValue.urlGetTokenStatic);
//
//		if (FileUtils.tokenWS == null || FileUtils.tokenWS.isEmpty()) {
//
//			logger.info("***************: Lan dau chay => getToken   ");
//			TokenUtils.getAccessToken(env.getProperty("urlGetToken"), env.getProperty("mstUsername"),
//					env.getProperty("mstPassword"), env.getProperty("doanhnghiepMst"));
//		}
//
//		System.out.println("doanhNghiepMST la: " + env.getProperty("doanhnghiepMst"));
//		System.out.println("loaihoadonMa la: " + env.getProperty("loaihoadonMa"));
//		System.out.println("mauso la: " + env.getProperty("mauso"));
//		System.out.println("kyhieu la: " + env.getProperty("kyhieu"));
//		System.out.println("timeCallback la: " + env.getProperty("timeCallback"));
//		System.out.println("API gửi la: " + env.getProperty("urlGuiVaKyHoadonGocHSM"));
//
//		System.out.println("fileMaxOutNum gửi la: " + FILENAME);
//		System.out.println("fileMaHD gửi la: " + FILEmaHD);

	}

	// Auto callback once every 5 minutes = 5 * 60 * 1000 millis
	@Scheduled(fixedRate = 30000L)
	private void myRunMethod() {
		GlobalValue goGlobalValue = new GlobalValue();
		goGlobalValue.setUrlToken(env.getProperty("urlGetToken"));
		goGlobalValue.setPassword(env.getProperty("mstPassword"));
		goGlobalValue.setUsername(env.getProperty("mstUsername"));
		goGlobalValue.setDoanhnghiepMstStatic(env.getProperty("doanhnghiepMst"));

		logger.info("*************************************: Bat dau chay app");

		if (FileUtils.tokenWS == null || FileUtils.tokenWS.isEmpty()) {

			logger.info("***************: Lan dau chay => getToken   ");
			TokenUtils.getAccessToken(env.getProperty("urlGetToken"), env.getProperty("mstUsername"),
					env.getProperty("mstPassword"), env.getProperty("doanhnghiepMst"));
		}

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
				logger.info(
						"***************: CALL API dataOutputRepository.getData(maxOutNum, 0L). lay duoc maxOutNum = "
								+ maxOutNum);
				listSource = dataOutputRepository.getData(maxOutNum, 0L);

			} else {
				logger.info("***************: File maxOutNum.txt da ton tai. ");

				long maxOutNum = dataOutputRepository.getMaxSequence();
				FileUtils.writeFile(file, false, String.valueOf(maxOutNum));
				// CALL API DE LAY DU LIEU
				logger.info(
						"***************: CALL API dataOutputRepository.getData(maxOutNum, 0L). lay duoc maxOutNum = "
								+ maxOutNum);
				listSource = dataOutputRepository.getData(maxOutNum, 0L);

			} // end else

			if (listSource.size() == 0) {
				logger.info(
						"***************: listSource == null. Khong co du lieu thoa man 4 dieu kien! (ACC_MODEL,ACC_SYMBOL,ACC_NUMBER,ACC_DATE) == NULL");
				return;
			}

			// List luu OutNum distinct
			List<BigDecimal> listout = new ArrayList<BigDecimal>();
			BigDecimal outindex = new BigDecimal(0);
			for (DataOutput item : listSource) {
				if (item.getOutNum().compareTo(outindex) != 0) {
					listout.add(item.getOutNum());
				}
				outindex = item.getOutNum();
			}
			// loai bo cac gia tri trung trong listout
			ArrayList<BigDecimal> listNewOut = removeDuplicates(listout);
			System.out.println("listNewOut: "+listNewOut);

			int indexListOutNum = 0;
//			for (BigDecimal itemoutnum : listout) {
			for (BigDecimal itemoutnum : listNewOut) {
				BigDecimal outNum = new BigDecimal(0);
				outNum = itemoutnum;

				List<DataOutput> itemlistofoutnum = listSource.stream()
						.filter(c -> c.getOutNum().compareTo(itemoutnum) == 0).collect(Collectors.toList());
				ReuqestObj bodyRequest = new ReuqestObj();

				if (env.getProperty("doanhnghiepMst") == null || env.getProperty("doanhnghiepMst").isEmpty()) {
					logger.info(
							"***************: Khong lay duoc thong tin trong file application.properties :  doanhnghiepMst");

				}
				if (env.getProperty("loaihoadonMa") == null || env.getProperty("loaihoadonMa").isEmpty()) {
					logger.info(
							"***************: Khong lay duoc thong tin trong file application.properties :  loaihoadonMa");

				}
				if (env.getProperty("mauso") == null || env.getProperty("mauso").isEmpty()) {
					logger.info("***************: Khong lay duoc thong tin trong file application.properties :  mauso");

				}
				if (env.getProperty("kyhieu") == null || env.getProperty("kyhieu").isEmpty()) {
					logger.info(
							"***************: Khong lay duoc thong tin trong file application.properties :  kyhieu");

				} else { // lấy các thông tin trong file properties
					bodyRequest.setDoanhnghiepMst(env.getProperty("doanhnghiepMst"));
					bodyRequest.setLoaihoadonMa(env.getProperty("loaihoadonMa"));
					bodyRequest.setMauso(env.getProperty("mauso"));
					bodyRequest.setKyhieu(env.getProperty("kyhieu"));
					bodyRequest.setMaHoadon(String.valueOf(listout.get(indexListOutNum)));

					if (Objects.isNull(itemlistofoutnum.get(0).getDat())
							|| itemlistofoutnum.get(0).getDat().isEmpty()) {
						logger.info("***************: DATA = NULL tai OUT_NUM := " + outNum);
					} else if (Objects.isNull(itemlistofoutnum.get(0).getNumberContract())
							|| itemlistofoutnum.get(0).getNumberContract().isEmpty()) {
						logger.info("***************: NUMBER_CONTRACT = NULL tai OUT_NUM := " + outNum);
					} else if (Objects.isNull(itemlistofoutnum.get(0).getDat())
							|| itemlistofoutnum.get(0).getDat().isEmpty()) {
						logger.info("***************: DATA = NULL tai OUT_NUM := " + outNum);
					} else if (Objects.isNull(itemlistofoutnum.get(0).getReason())
							|| itemlistofoutnum.get(0).getReason().isEmpty()) {
						logger.info("***************: RESON = NULL tai OUT_NUM := " + outNum);
					} else if (Objects.isNull(itemlistofoutnum.get(0).getForCompany())
							|| itemlistofoutnum.get(0).getForCompany().isEmpty()) {
						logger.info("***************: ForCompany = NULL tai OUT_NUM := " + outNum);
					} else {

						bodyRequest.setNgaylap(convertStringToStringFormatDate(itemlistofoutnum.get(0).getDat())); // convert
						bodyRequest.setVanchuyenNgayxuat(
								convertStringToStringFormatDate(itemlistofoutnum.get(0).getDat()));

						bodyRequest.setVanchuyen_lydo(itemlistofoutnum.get(0).getReason());
						bodyRequest.setVanchuyen_phuongthuc(itemlistofoutnum.get(0).getNumberCar());
						bodyRequest.setVanchuyenKhoxuat("Công ty TNHH Việt Nam SAMHO");
						bodyRequest.setVanchuyenKhonhap(itemlistofoutnum.get(0).getForCompany());
						bodyRequest.setVanchuyenSo(itemlistofoutnum.get(0).getNumberContract());

						bodyRequest.setTongtienChuavat(0);
						bodyRequest.setTienthue(0);
						bodyRequest.setTongtienCovat(0);

						List<Dschitiet> lChitiet = new ArrayList<Dschitiet>();
						int indexOfDetail = 1;
						for (DataOutput itemfilter : itemlistofoutnum) {

							Dschitiet ct = new Dschitiet();
							if (Objects.isNull(itemfilter.getRemar()) || Objects.isNull(itemfilter.getUom())
									|| Objects.isNull(itemfilter.getQty()) || itemfilter.getRemar().isEmpty()
									|| itemfilter.getUom().isEmpty()

							) {
								logger.info("***************:NULL o TB_OUT_N_DETAIL  tai OUT_NUM := " + outNum);

							} else {
								// set stt cho detail
								ct.setStt(indexOfDetail++);
								ct.setTen(itemfilter.getRemar());
								ct.setDonvitinh(itemfilter.getUom());
								ct.setSoluong(itemfilter.getQty());
								ct.setVanchuyen_loai(1);
							}

							lChitiet.add(ct);

						}
//						indexOfDetail = 1; //listout.get(indexListOutNum
						bodyRequest.setDschitiet(lChitiet);

						String jsonbody = new Gson().toJson(bodyRequest);
						logger.info("***************: Thong tin gui di la:   " + jsonbody);

						String result = Utils.connectServer(
								env.getProperty("urlGuiVaKyHoadonGocHSM"),
								jsonbody,
								" Bearer " + FileUtils.tokenWS,
								env.getProperty("urlGetToken")
								);
						logger.info("***************: Ket qua CALL API- BILL voi ma_hoadon=:  " + listout.get(indexListOutNum) + " ------  "
								+ result);
						if (!result.equals("3")) {
							JSONObject json = new JSONObject(result);
							JSONObject objectResult = new JSONObject();
							objectResult = json.getJSONObject("result");

							if (objectResult.get("mauso").equals(null)) {
								logger.error(
										"***************: NULL mauso ==> Khong CALL duoc API UPDATE 4 file bang Header ");
							} else if (objectResult.get("kyhieu").equals(null)) {
								logger.error(
										"***************: NULL kyhieu ==> Khong CALL duoc API UPDATE 4 file bang Header ");
							} else if (objectResult.get("sohoadon").equals(null)) {
								logger.error(
										"***************: NULL sohoadon ==> Khong CALL duoc API UPDATE 4 file bang Header ");
							} else if (objectResult.get("ngayky").equals(null)) {
								logger.error(
										"***************: NULL ngayky ==> Khong CALL duoc API UPDATE 4 file bang Header ");
							} else {

							//	long outNumLong = outNum.longValue();
								long outNumLong = listout.get(indexListOutNum).longValue();
								try {
									int x = dataOutputRepository.update4Filed(

											objectResult.getString("mauso"), objectResult.getString("kyhieu"),
											objectResult.getString("sohoadon"),
											new SimpleDateFormat("dd/MM/yyyy").parse(objectResult.getString("ngayky")),
											outNumLong);
									if (x > 0) {
										logger.info("***************: Ket qua UPDATE 4 truong la:   " + x
												+ " Thanh cong UPDATE!!");
									} else {
										logger.error(
												"***************: Ket qua UPDATE 4 truong la:   " + x + " That bai!!");
									}

								} catch (JSONException e) {
									logger.error("***************: Loi : " + e.getMessage() + "== " + e.getClass());
								} catch (ParseException e) {
									logger.error("***************: Loi : " + e.getMessage() + "== " + e.getClass());
								}
								logger.info("***************: Ket thuc :***********************");
								// Tang chi so cua indexOutNum len de ki lan tiep theo
								indexListOutNum++;
							}

						} // end if
					}

				} //// END lấy các thông tin trong file properties
			} // end for 197

		} catch (IOException e) {
			logger.error("***************: Loi : " + e.getMessage() + "== " + e.getClass() + "line: 179 ");
			e.printStackTrace();
		}

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

	// Function to remove duplicates from an ArrayList
	public static <T> ArrayList<T> removeDuplicates(List<T> list) {

		// Create a new ArrayList
		ArrayList<T> newList = new ArrayList<T>();

		// Traverse through the first list
		for (T element : list) {

			// If this element is not present in newList
			// then add it
			if (!newList.contains(element)) {

				newList.add(element);
			}
		}

		// return the new list
		return newList;
	}

}
