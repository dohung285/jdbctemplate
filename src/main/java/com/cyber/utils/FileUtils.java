package com.cyber.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jdbctemplate.JdbctemplateApplication;

public class FileUtils {

	private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

	// Ghi gile
	public static void writeFile(File file, boolean flag, String value) {
		logger.info("***************: CALL-METHOD ghi file ");
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(file, flag);
			BufferedWriter bw = new BufferedWriter(fileWriter);
			bw.write(value);
			bw.close();
		} catch (IOException e) {
			logger.error("***************: loi : line 30 " + e.getMessage() + "=====" + e.getClass());
			e.printStackTrace();
		}
	}

	// Doc File
	public static String readFile(File file) {
		logger.info("***************: CALL-METHOD doc file ");
		FileReader fileReader = null;
		String strMaxOutNum = null;
		try {
			fileReader = new FileReader(file);
			BufferedReader br = new BufferedReader(fileReader);
			// read line by line
			String line = null;

			while ((line = br.readLine()) != null) {
				if (line != null && line.length() > 0) {
					strMaxOutNum = line;
				}
			}
			br.close();

		} catch (FileNotFoundException e) {
			logger.error("***************: loi : line 54 " + e.getMessage() + "=====" + e.getClass());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("***************: loi : line 57 " + e.getMessage() + "=====" + e.getClass());
			e.printStackTrace();
		}
		return strMaxOutNum;
	}

}
