package com.cyber.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.font.CreatedFontTracker;

public class Utils {

	private static final Logger logger = LoggerFactory.getLogger(Utils.class);

	public static String SendDatahttps(String url, String inputsent, String auth) {
		try {
			X509TrustManager tm = new X509TrustManager() {
				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
						throws CertificateException {

				}

				public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
						throws CertificateException {
				}

				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
						throws CertificateException {
					// TODO Auto-generated method stub

				}

				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
						throws CertificateException {
					// TODO Auto-generated method stub

				}
			};
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLContext.setDefault(ctx);
			HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
			// conn.setSSLSocketFactory(ctx.getSocketFactory());
			conn.setHostnameVerifier(new HostnameVerifier() {

				@Override
				public boolean verify(String paramString, SSLSession paramSSLSession) {
					return true;
				}
			});
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			if (auth == null || auth.trim().equals("")) {

			} else {
				conn.addRequestProperty("Authorization", auth);
			}

			conn.connect();

			OutputStream os = conn.getOutputStream();
			try {
				os.write(inputsent.getBytes());
			} catch (IOException e) {
				logger.error("***************: Loi:  " + e.getMessage());
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

			os.flush();

//			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
//				return "1";
//			}
			if (conn.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
				TokenUtils.getAccessToken();
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			StringBuilder kq = new StringBuilder();

			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				// System.out.println("Data output: "+output);
				kq.append(output);
			}

			conn.disconnect();
			return kq.toString();
		} catch (Exception ex) {
			logger.error("***************: Loi:  " + ex.getMessage());
			System.out.println(ex.getMessage());
			return "2";
		}
	}

	public static String connectServer(String urlstr, String inputsent, String auth) {
		try {

			URL url = new URL(urlstr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			if (auth == null || auth.trim().equals("")) {

			} else {
				conn.addRequestProperty("Authorization", auth);
			}
			String input = inputsent;

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			if (conn.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
				TokenUtils.getAccessToken();
			}

//			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
//				System.out.println(conn.getResponseCode());
//				// return "1";
//			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			String output2 = "";
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {

				output2 += output;

			}

//            // tesst
//            System.out.println("output2: " + output2);

			conn.disconnect();
			return output2;

		} catch (MalformedURLException e) {
//			e.printStackTrace();
			logger.error("***************: Loi :   " + e.getMessage());
			return "2";
		} catch (IOException e) {
//			e.printStackTrace();
			logger.error("***************: Loi :   " + e.getMessage());
			return "3";
		}

	}
}
