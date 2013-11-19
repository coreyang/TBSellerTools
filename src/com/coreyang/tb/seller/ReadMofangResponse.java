package com.coreyang.tb.seller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class ReadMofangResponse {
	public void readMofang(String file) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
		String temp = null;
		while((temp=br.readLine())!=null){
			System.out.println(temp);
		}
	}
	
	public static void main(String[] args) throws IOException {
		ReadMofangResponse rm = new ReadMofangResponse();
		String file = "D://tt.txt";
		rm.readMofang(file);
	}
}
