package com.cyber.utils;

import java.io.File;
import java.io.IOException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.example.jdbctemplate.request.RequestAccount;
import com.google.gson.Gson;

public class TokenUtils {

	private static final Logger logger = LoggerFactory.getLogger(TokenUtils.class);

	@Autowired
	static Environment env;

	public static void getAccessToken() {

//		File file = new File("token.txt");
	//	File file = new File("C:/LogAppJDBCTemplate/token.txt");
//		if (!file.exists()) {
//			logger.info("***************: File token.txt chua ton tai");
//			try {
//				file.createNewFile();
//			} catch (IOException e) {
//				logger.error("***************: File token.txt tao loi");
//			}
//		}
		RequestAccount reqAccount = new RequestAccount();

		reqAccount.setDoanhnghiepMst("0300812669");
		reqAccount.setUsername("0300812669@kws");
		reqAccount.setPassword("12345678");

		String jsonBodyAccount = new Gson().toJson(reqAccount);
		logger.info("***************: Thong tin gui di de lay ACCESS_TOKEN :   " + jsonBodyAccount);
		String result = Utils.connectServer("http://hddtws.esamho.com/api/services/hddtws/Authentication/GetToken",
				jsonBodyAccount, null);

		JSONObject json = new JSONObject(result);
		JSONObject objectResult = new JSONObject();
		objectResult = json.getJSONObject("result");
		logger.info("***************: Lay duoc access_token: " + objectResult.getString("access_token"));
		System.out.println(objectResult.getString("access_token"));
		// Luu token vào file
		//FileUtils.writeFile(file, false, objectResult.getString("access_token"));
		FileUtils.tokenWS = objectResult.getString("access_token");
		logger.info("***************: Luu access_token vao file thanh cong ");

	}

}
