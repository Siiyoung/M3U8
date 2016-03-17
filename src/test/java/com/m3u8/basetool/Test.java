package com.m3u8.basetool;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Test {
	public static void main(String args[]) throws Exception{
    	String[] strs={"#EXTINF:5,",
    	               "http://45.112.215.112/6773B1A88E4378191B3303600E/0300020A0056E115A559E32BEEFCF9CE1E29BC-6093-4713-9CF4-7624F873F81D.flv.ts?ts_start=11.1&ts_end=16.1&ts_seg_no=2&ts_keyframe=1",
    	               "#EXTINF:11.133,",
    	               "http://45.112.215.112/6773B1A88E4378191B3303600E/0300020A0056E115A559E32BEEFCF9CE1E29BC-6093-4713-9CF4-7624F873F81D.flv.ts?ts_start=16.1&ts_end=27.233&ts_seg_no=3&ts_keyframe=1",
    	               "#EXTINF:11.667,",
    	               "http://45.112.215.112/6773B1A88E4378191B3303600E/0300020A0056E115A559E32BEEFCF9CE1E29BC-6093-4713-9CF4-7624F873F81D.flv.ts?ts_start=27.233&ts_end=38.9&ts_seg_no=4&ts_keyframe=1",
    	               "#EXTINF:10.133,",
    	               "http://45.112.215.112/6773B1A88E4378191B3303600E/0300020A0056E115A559E32BEEFCF9CE1E29BC-6093-4713-9CF4-7624F873F81D.flv.ts?ts_start=38.9&ts_end=49.033&ts_seg_no=5&ts_keyframe=1",
    	               "#EXTINF:8.4,",
    	               "http://45.112.215.112/6773B1A88E4378191B3303600E/0300020A0056E115A559E32BEEFCF9CE1E29BC-6093-4713-9CF4-7624F873F81D.flv.ts?ts_start=49.033&ts_end=57.433&ts_seg_no=6&ts_keyframe=1"};
    	for(String s:solveDuration(strs)){
    		System.out.println(s);
    	}
    }
    public static List<String> solveDuration(String[] strs){     //姝ｅ垯鍖归厤EXT-X-TARGETDURATION: 鑾峰彇鍏跺��
		Pattern pattern=Pattern.compile("(?<=EXTINF:)[\\d\\.]+");
		List<String> results=new ArrayList<String>();
		for(String segList:strs){
			Matcher matcher=pattern.matcher(segList);
			if(matcher.find()){
				results.add(matcher.group());
			}
		
		}
		return results;
		}
	}


