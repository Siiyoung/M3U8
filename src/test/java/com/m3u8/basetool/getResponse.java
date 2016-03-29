package com.m3u8.basetool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class getResponse {
	/*
	 public static void main(String args[]) throws Exception{  //调试用
		    String url = "http://pl.youku.com/playlist/m3u8?vid=21324343243543543534&ts=1458891649&ep=cSaREkGEV8gE5yrcjT8bZC3qJ3NdXP0M8xeDhtJjALgjT%2By8nD%2FUxZ62SfxLHv4RciEAGeLwq9nmaEMTePdGq2kP3SGpPuXp&sid=345889164293512318c4b&token=1729&ctype=12&ev=1&oip=3702858339";
	    	getResponse getResponse = new getResponse();
	    	System.out.println(getResponse.getResponseByGet(url));
	    }*/
    public String getResponseByGet(String url) throws Exception {
    	URL obj = new URL(url);
    	Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.10.69.164",80));
    	HttpURLConnection con = (HttpURLConnection) obj.openConnection(proxy);   	
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
