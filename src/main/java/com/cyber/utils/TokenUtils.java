package com.cyber.utils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import com.example.jdbctemplate.request.RequestAccount;
import com.google.gson.Gson;

public class TokenUtils {

	private static final Logger logger = LoggerFactory.getLogger(TokenUtils.class);


	public static void getAccessToken(String ...urlGetToken) {
		
		
		GlobalValue globalValue = new GlobalValue();
		globalValue.setUrlToken(urlGetToken[0]);
		globalValue.setUsername(urlGetToken[1]);
		globalValue.setPassword(urlGetToken[2]);
		globalValue.setDoanhnghiepMstStatic(urlGetToken[3]);

		System.out.println("TOKEN: " + globalValue.urlGetTokenStatic);

		RequestAccount reqAccount = new RequestAccount();

		reqAccount.setDoanhnghiepMst(globalValue.doanhnghiepMstStatic);
		reqAccount.setUsername(globalValue.usernameStatic);
		reqAccount.setPassword(globalValue.passwordStatic);

		String jsonBodyAccount = new Gson().toJson(reqAccount);
		logger.info("***************: Thong tin gui di de lay ACCESS_TOKEN :   " + jsonBodyAccount);
		if (globalValue.urlGetTokenStatic == null  || globalValue.urlGetTokenStatic.isEmpty()) {
			logger.info("***************: Khong lay duoc API Gettoken ");
			return;
		}
		String result = Utils.connectServer(globalValue.urlGetTokenStatic, jsonBodyAccount, null,urlGetToken[0]);

		JSONObject json = new JSONObject(result);
		JSONObject objectResult = new JSONObject();
		objectResult = json.getJSONObject("result");
		logger.info("***************: Lay duoc access_token: " + objectResult.getString("access_token"));
		System.out.println(objectResult.getString("access_token"));
		// Luu token vào file
		// FileUtils.writeFile(file, false, objectResult.getString("access_token"));
		FileUtils.tokenWS = objectResult.getString("access_token");
		logger.info("***************: Luu access_token vao bien static thanh cong ");

	}

}
