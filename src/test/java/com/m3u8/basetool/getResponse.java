package com.m3u8.basetool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class getResponse {
    public String getResponseByGet(String url) throws Exception {
    	URL obj = new URL(url);
    	HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    	con.setRequestMethod("GET");
    	
    	int responseCode = con.getResponseCode(); //获取接口返回code
    	String respCode = String.valueOf(responseCode);
    	if(responseCode==200){    		
    	}else{
    		System.out.println("接口请求失败，错误码是："+responseCode);
    	}
    	BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine).append("\n");
		}
		String response = responseBuffer.toString();
		in.close();
    	
		return response;
		
    }
    public Object getResponseByPost(String url) throws IOException{
    	URL obj = new URL(url);
    	HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    	con.setRequestMethod("POST");
    	
    	int responceCode = con.getResponseCode(); //获取接口返回code
    	
    	BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
    	
		return responceCode;
   
    }
}
