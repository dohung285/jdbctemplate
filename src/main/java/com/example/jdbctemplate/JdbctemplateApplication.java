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
import java.util.stream.Collectors;

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

	private static final String FILENAME = "C:/LogAppJDBCTemplate/maxOutNum.txt";
//	private static final String FILEmaHD = "C:/LogAppJDBCTemplate/maHoaDon.txt";

//	private static final String FILENAME = "maxOutNum.txt";
	private static final String FILEmaHD = "maHoaDon.txt";
	private static final String FILEtoken = "C:/LogAppJDBCTemplate/token.txt";

//	@Autowired
//	RestTemplate restTemplate;    

	public static void main(String[] args) {
		SpringApplication.run(JdbctemplateApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		myRunMethod();

	}

	// Auto callback once every 5 minutes = 5 * 60 * 1000 millis
	@Scheduled(fixedRate = 5000L)
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
				String strMaxOutNum = FileUtils.readFile(file);
				// System.out.println("LINE " + strMaxOutNum);
				long maxOldOutNum = Long.parseLong(strMaxOutNum);
				long maxNewOutNum = dataOutputRepository.getMaxSequence();
				System.out.println("OLD: " + maxOldOutNum + " NEW: " + maxNewOutNum);
				logger.info("***************: " + "OLD: " + maxOldOutNum + " NEW: " + maxNewOutNum);

				// Call API - BILL - lay dc listSource
				logger.info("***************: dataOutputRepository.getData(maxOldOutNum, maxNewOutNum). ");
				listSource = dataOutputRepository.getData(maxOldOutNum, maxNewOutNum);
			} // end else

			List<BigDecimal> listout = new ArrayList<BigDecimal>();
			BigDecimal outindex = new BigDecimal(0);
			for (DataOutput item : listSource) {
				if (item.getOutNum().compareTo(outindex) != 0) {
					listout.add(item.getOutNum());
				}
				outindex = item.getOutNum();
			}
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
				long maHD = Long.parseLong(FileUtils.readFile(fileMHD)) + 1;
				bodyRequest.setMaHoadon(maHD + ""); // doc tu file
				// ghi lai vao file gia tri vua su dung
				FileUtils.writeFile(fileMHD, false, maHD + "");

				bodyRequest.setNgaylap(convertStringToStringFormatDate(itemlistofoutnum.get(0).getDat())); // convert
				bodyRequest.setVanchuyenSo(itemlistofoutnum.get(0).getNumberContract());
				bodyRequest.setVanchuyenNgayxuat(convertStringToStringFormatDate(itemlistofoutnum.get(0).getDat()));
				bodyRequest.setVanchuyenKhoxuat(itemlistofoutnum.get(0).getReason());
				bodyRequest.setVanchuyenKhonhap(itemlistofoutnum.get(0).getForCompany());

				bodyRequest.setTongtienChuavat(0);
				bodyRequest.setTienthue(0);
				bodyRequest.setTongtienCovat(0);

				List<Dschitiet> lChitiet = new ArrayList<Dschitiet>();
				for (DataOutput itemfilter : itemlistofoutnum) {

					Dschitiet ct = new Dschitiet();
					ct.setTen(itemfilter.getRemar());
					ct.setDonvitinh(itemfilter.getUom());
					ct.setSoluong(itemfilter.getQty());

					lChitiet.add(ct);

				}
				bodyRequest.setDschitiet(lChitiet);

				String jsonbody = new Gson().toJson(bodyRequest);
				logger.info("***************: Thong tin gui di la:   " + jsonbody);
				File fileToken = new File(FILEtoken);
				if (!fileToken.exists()) {
					logger.info("***************: File token.txt chua ton tai");
					fileToken.createNewFile();
				}
				String result = Utils.connectServer(env.getProperty("urlGuiVaKyHoadonGocHSM"), jsonbody,
						" Bearer " + FileUtils.readFile(fileToken));
				logger.info("***************: Ket qua CALL API- BILL :  " + result);

				if (!isJSONValid(result)) {
					logger.error("***************: Loi khong dung dinh dang JSON - line 167 :  ");
					return;
				}

				JSONObject json = new JSONObject(result);
				JSONObject objectResult = new JSONObject();
				objectResult = json.getJSONObject("result");
				System.out.println(objectResult.getString("mauso"));
				System.out.println(objectResult.getString("kyhieu"));
				System.out.println(objectResult.getString("sohoadon"));
				System.out.println(objectResult.getString("ngayky"));

				long outNumLong = outNum.longValue();
				try {
					int x = dataOutputRepository.update4Filed(objectResult.getString("mauso"),
							objectResult.getString("kyhieu"), objectResult.getString("sohoadon"),
							new SimpleDateFormat("dd/MM/YYYY").parse(objectResult.getString("ngayky")), outNumLong);
					logger.info("***************: Ket qua UPDATE 4 truong la:   " + x);
				} catch (JSONException e) {
					logger.error("***************: Loi : " + e.getMessage() + "== " + e.getClass() + "line: 170 ");
					e.printStackTrace();
				} catch (ParseException e) {
					logger.error("***************: Loi : " + e.getMessage() + "== " + e.getClass() + "line: 173 ");
					e.printStackTrace();
				}
			}

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
}
