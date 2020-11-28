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


public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public static String connectServer(String urlstr, String inputsent, String auth,String urlToken) {
        try {
            logger.info(urlstr);
            logger.info(auth);
            logger.info(inputsent);
            URL url = new URL(urlstr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");

            conn.setRequestProperty("accept", "text/plain");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            if (auth == null || auth.trim().equals("")) {

            } else {
                logger.info("Auth:" + auth);
                conn.addRequestProperty("Authorization", auth);
            }
            String input = inputsent;

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                logger.error("***************: Loi :   " + 401);
                TokenUtils.getAccessToken(urlToken);
            }

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println(conn.getResponseCode());
                return "3";
            }

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
            e.printStackTrace();
            logger.error("***************: Loi :   " + e.getMessage());
            return "2";
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("***************: Loi :   " + e.getMessage());
            return "3";
        }

    }
}
