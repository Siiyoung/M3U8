package com.m3u8.test;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.testng.annotations.Test;

import com.m3u8.basetool.GetSegList;
import com.m3u8.basetool.GetTsSegNoRank;

public class TestBase {
	GetSegList getSegList = new GetSegList();
	GetTsSegNoRank getTsSegNoRank = new GetTsSegNoRank();
	
    @Test
    public void testResponse() throws Exception{    //测试m3u8接口的返回  	
    	getSegList.checkResponse();
    	System.out.println("接口成功调用！");
    }
    
    @Test
    public void testSeg_noRank() throws Throwable {   //测试ts_seg_no标签的排序	
    	ArrayList<Integer> segNoList = getSegList.getSegNoList();
    	
    	getTsSegNoRank.getSeg_noRank();
    }	
    
    @Test
    public void testSeg_noRepeat() throws Exception{  //测试ts_seg_no标签的重复性
    	getTsSegNoRank.getSeg_noRepeat();
    }
    
    @Test  
    public void testMaxTime() throws Exception{
    	getTsSegNoRank.compareFragmentMaxTime();
    }
  
    @Test
    public void testExtinfDataType() throws Exception{
    	getTsSegNoRank.judgeExtinfDataType();
    }
        
}
   
    