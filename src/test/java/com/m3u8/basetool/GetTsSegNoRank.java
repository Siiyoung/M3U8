package com.m3u8.basetool;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class GetTsSegNoRank {

	/**
	 * @param args
	 */

    
	public boolean getSeg_noRank() throws Exception{    //获取ts_seg_no标签的顺序排列
    	GetSegList getSegList = new GetSegList();
    	ArrayList<Integer> segList = getSegList.getSegNoList();
    	
		if(segList.get(0)!=0){
			throw new IOException("获取到的第一个ts_seg_no≠0！详情请看segList输出！");
		}
		for(int j=1;j<segList.size();j++){
			if(segList.get(j-1)>=segList.get(j)){
				if(segList.get(j)!=0){
					throw new IOException("获取到的正片或穿插广告的ts_seg_no≠0 or ts_seg_no排序有重复。详情请看segList输出！");
				}
			}else if((segList.get(j)-segList.get(j-1))!=1){
				throw new IOException("ts_seg_no排序有错误！详情请看segList输出！");
			}
			return true;
		}	
		
	return true;
    	
    }
    
	public boolean getSeg_noRepeat() throws Exception{  //获取ts_seg_no标签的重复性
		GetSegList getSegList = new GetSegList();
    	ArrayList<Integer> segList = getSegList.getSegNoList();
    	if((segList.get(0)==0)&(segList.get(1)==0)){
    		for(int i =2;i<segList.size();i++){
    			if(segList.get(i-1)==segList.get(i)){
    				throw new IOException("ts_seg_no排序重复！");
    			}
    		}
    	}else{
    		for(int i =1;i<segList.size();i++){
    			if(segList.get(i-1)==segList.get(i)){
    				throw new IOException("ts_seg_no排序重复！");
    			}
    		}
    	}
		return true;
		
	}
    
	public double getExtinfMaxTime() throws Exception {
		GetSegList getSegList = new GetSegList();
		ArrayList<String> extinf = getSegList.getExtinf();
		double a=Double.MIN_VALUE;
		int index=-1;
		for(int i =1;i<extinf.size();i++){
			if(Double.parseDouble(extinf.get(i))>a){
				a=Double.parseDouble(extinf.get(i));
			    index=i;		    
			}
		}
		System.out.println("#EXTINF中最大值是"+a+"， 在第"+ index +"个元素");
		return a;
	}
    
	public boolean compareFragmentMaxTime() throws Exception{
		GetSegList getSegList = new GetSegList();
		ArrayList<String> duration = getSegList.getExtDuration();
		getSegList.getExtinf();
		double b = Double.parseDouble(duration.get(0));
		double a = getExtinfMaxTime();
		System.out.println("#EXT-X-TARGETDURATION的值是："+b);		
		if(b!=a){
			throw new IOException("#EXT-X-TARGETDURATION的值不是最大值,接口返回有错");
		}
		return true;
		
	}
    
	public boolean judgeExtinfDataType() throws Exception{
		GetSegList getSegList = new GetSegList();
		ArrayList<String> extVersion = getSegList.getExtVersion();
		ArrayList<String> extinf = getSegList.getExtinf();
		if((Integer.parseInt(extVersion.get(0))==2)||(Integer.parseInt(extVersion.get(0))==3)){
			while((Integer.parseInt(extVersion.get(0))==2)){
				for(int i =0;i<extinf.size();i++){
					if(extinf.get(i).contains(".")){
						throw new IOException("当#EXT-X-VERSION为2时,#EXTINF不是整型的！");
					}
				}
			}
			while((Integer.parseInt(extVersion.get(0))==3)){
				for(int j=0;j<extinf.size();j++){
					if(extinf.get(j).contains(".")){
						return true;
					}else{
						throw new IOException("当#EXT-X-VERSION为3时,#EXTINF不是浮点型的！");
					}
				}
			}
		}		
		return true;
		
	}
	

}
