package com.m3u8.basetool;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GetSegList {
	String url = "http://pl.youku.com/playlist/m3u8?ids=%7B%22a1%22:%22366409371_mp4%22,%22a2%22:%22366139981_mp4%22,%22a3%22:%22371477645_mp4%22,%22a4%22:%22366465060_mp4%22,%22a5%22:%22374230008_mp4%22,%22a6%22:%22374237592_mp4%22,%22a7%22:%22374237290_mp4%22,%22v%22:%22373787369_flv%22%7D&ts=1458199645&ep=dSaREkiEX8gC4yXdiz8bNirldSRdXP0M8BuFiNFlAbgiTum8kDfZxp63T%2FtFEf8ddyEAFu%2F3rtDhakMTePJAqWgN102vPeXj%2BfXq%2Bq5UwJIDYRs%2Fcs7QtzyWRDP3I4lCcOPe&sid=745819962662312a671cb&token=3319&ctype=12&ev=1&oip=3702858343";
    getResponse getResponse = new getResponse();
    /*  
    public static void main(String args[]) throws Exception{  //调试用
    	getSegNoList();
    }*/
    
    public void checkResponse() throws Exception{
    	String response = getResponse.getResponseByGet(url);
    	String respCode = getResponse.getResponseByGet(url);
    	if((response!=null)&(respCode=="200")){
    		System.out.println("接口请求成功！");
    	}
    }
    public List<String> solveTs_Seg_No(String[] strs){    //正则匹配ts_seg_no=: 获取其值
		Pattern pattern=Pattern.compile("(?<=ts_seg_no=)\\d+");
		List<String> results=new ArrayList<String>();
		for(String segList:strs){
			Matcher matcher=pattern.matcher(segList);
			if(matcher.find()){
				results.add(matcher.group());
			}
		
		}
		return results;
	}
    
    public List<String> solveDuration(String[] strs){     //正则匹配EXT-X-TARGETDURATION: 获取其值
		Pattern pattern=Pattern.compile("(?<=EXT-X-TARGETDURATION:)\\d+");
		List<String> results=new ArrayList<String>();
		for(String segList:strs){
			Matcher matcher=pattern.matcher(segList);
			if(matcher.find()){
				results.add(matcher.group());
			}
		
		}
		return results;
	}
    
    public List<String> solveExtinf(String[] strs){    //正则匹配EXTINF: 获取其值
    	Pattern pattern=Pattern.compile("(?<=EXTINF:)[\\d\\.]+");
		List<String> results=new ArrayList<String>();
		int i = 0;
		for(String segList:strs){
			Matcher matcher=pattern.matcher(segList);
			if(matcher.find()){
				results.add(matcher.group());
				i++;
			}		
		}
		System.out.println("#EXTINF的数目:"+i+"#EXTINF的集合是:"+results);
    	return results;
    	
    }
    
    public List<String> solveExtVersion(String[] strs){     //正则匹配#EXT-X-TARGETDURATION:  获取其值
    	Pattern pattern=Pattern.compile("(?<=EXT-X-TARGETDURATION:)\\d+");
		List<String> results=new ArrayList<String>();
		for(String segList:strs){
			Matcher matcher=pattern.matcher(segList);
			if(matcher.find()){
				results.add(matcher.group());
			}
		
		}
		return results;  
    }
   
    public ArrayList<String> getExtDuration() throws Exception{  //获取duration的值
    	String response = getResponse.getResponseByGet(url);
    	ArrayList<String> extVersion = new ArrayList<String>();
    	String[] resp = response.split("\n");
		List<String> strs=solveDuration(resp);
				
		for(String s:strs){
			extVersion.add(s);
			//System.out.print(s+",");			
		}		
		return extVersion;    	
    }

    public ArrayList<Integer> getSegNoList() throws Exception{  // 获取ts_seg_no
    	String response = getResponse.getResponseByGet(url);
    	ArrayList<Integer> segNoList = new ArrayList<Integer>();
    	String[] resp = response.split("\n");
		List<String> strs=solveTs_Seg_No(resp);
		int i = 0;		
		for(String s:strs){			
			segNoList.add(Integer.parseInt(s));
			i++;
			//System.out.print(s+",");
		}
		System.out.println("ts_sg_no标签数目有："+i+"ts_seg_no标签的所有的值是："+segNoList);
		return segNoList;
		
    }	
       
    public ArrayList<String> getExtinf() throws Exception{  //获取extinf的值
    	String response = getResponse.getResponseByGet(url);
    	ArrayList<String> extinf = new ArrayList<String>();
    	String[] resp = response.split("\n");
		List<String> strs=solveExtinf(resp);
				
		for(String s:strs){
			extinf.add(s);
			//extinf.add(new BigDecimal(s).setScale(3,BigDecimal.ROUND_DOWN));
			//System.out.print(s+",");			
		}
		
		return extinf;
    
    	
    }
    
    public ArrayList<String> getExtVersion() throws Exception{
    	String response = getResponse.getResponseByGet(url);
    	ArrayList<String> extVersion = new ArrayList<String>();
    	String[] resp = response.split("\n");
		List<String> strs=solveExtinf(resp);
				
		for(String s:strs){
			extVersion.add(s);			
		}		
		return extVersion;    	
    }
}
	
		
