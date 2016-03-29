package com.m3u8.basetool;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GetTsSegNoRank {

	/**
	 * @author heyi
	 * @param args
	 */
	
	
	/**
	 *比较m3u8时长和playservice时长 
	 * @throws Exception 
	 * @author sunjiangbin
	 * */
	
	public boolean CompareM3uPs() throws Exception{
		String result = "";
		int num=0;
		List<Double> lp = new ArrayList<Double>();
		List<Double> lm = new ArrayList<Double>();
		GetSegList g=new GetSegList();
		lp = g.GetPstime();
		lm = g.Getm3u8Time();
		if(lp.size()==lm.size()){
			result=result+"共计"+lp.size()+"分片\r\n";
			for(int n=0;n<lp.size();n++){
				result=result+"ps和m3u8返回第"+(n+1)+"分片时长分别为"+lp.get(n)+"，"+lm.get(n)+"\r\n";
				if(lp.get(n)+0.2>lm.get(n)&lp.get(n)-0.2<lm.get(n)){
					result=result+"第"+(n+1)+"分片时长一致，验证ok\r\n";
					num++;
				}
				else
					result=result+"第"+(n+1)+"分片时长不等，请对比\r\n";
			}
			if(num==lp.size()){
				result="验证通过\r\n"+result;
				System.out.println(result);
				return true;
			}									
			else{
				result="验证错误\r\n"+result;	
				System.out.println(result);
				return false;
				//throw new IOException("验证错误\r\n"+result);								
			}					
		}
		else{
			result = "m3u8和ps分片数不等,m3u8分片数为"+lm.size()+"ps分片数为"+lp.size();
			System.out.println(result);
			return false;			
			//throw new IOException("m3u8和ps分片数不等,m3u8分片数为"+lm.size()+"ps分片数为"+lp.size());
		}
		
	}
	
	
	/**
	 *对比ts_start和ts_end的数值是否相等 
	 * @throws Exception 
	 * @author sunjiangbin
	 * */
	public boolean CompareTime() throws Exception {
		int num = 0;
		List<String> ts_stime=new ArrayList<String>(); 
		List<String> ts_etime=new ArrayList<String>();
		String errorinfo= "";
		GetSegList g=new GetSegList();
		String[] resp = g.geteveryurl();
		for(String str:resp){
			ts_stime=g.getvalue(str,"ts_start=", "&ts_end=");
			ts_etime=g.getvalue(str,"ts_end=", "&ts_seg_no=");		

			for(int n=1;n<ts_etime.size();n++){
				if(!(ts_stime.get(n).equals(ts_etime.get(n-1)))){
					num++;
					errorinfo=errorinfo+"存在不相等的结束时间和开始时间：开始时间为"+ts_stime.get(n)+"，但上一次结束时间为"+ts_etime.get(n-1)+"\r\n";					
				}
			}
		}
		
		if(num==0){
			System.out.println("顺序正确");
			return true;
		}			
		else
			System.out.println(errorinfo);
			throw new IOException(errorinfo);
	}
	
	
	/**
	 * 验证各标签唯一性
	 * @throws Exception
	 * @author sunjiangbin 
	 * */
	public boolean isOnlyValue() throws Exception{
		GetSegList g=new GetSegList();
		return g.isonlyvalue();
	}

    
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
