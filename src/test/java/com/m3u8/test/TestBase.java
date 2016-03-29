package com.m3u8.test;

import org.testng.annotations.Test;

import com.m3u8.basetool.GetSegList;
import com.m3u8.basetool.GetTsSegNoRank;
import com.m3u8.basetool.GetUnusualUrlResponse;

public class TestBase {
	GetSegList getSegList = new GetSegList();
	GetTsSegNoRank getTsSegNoRank = new GetTsSegNoRank();
	GetUnusualUrlResponse getUnusualUrlResponse = new GetUnusualUrlResponse();
	
    @Test
    public void testResponse() throws Exception{    //测试m3u8接口的返回  	
    	getSegList.checkResponse();
    	System.out.println("接口成功调用！");
    }
    
    @Test
    public void testSeg_noRank() throws Throwable {   //测试ts_seg_no标签的排序	
    	getTsSegNoRank.getSeg_noRank();
    }	
    
    @Test
    /**
     * @author wangyouqing
     * @throws Exception
     */
    public void testSeg_noRepeat() throws Exception{  //测试ts_seg_no标签的重复性
    	getTsSegNoRank.getSeg_noRepeat();
    }
    
    @Test  
    public void testFragmentMaxTime() throws Exception{   //测试extinf version的时间恒大于extinf的时间
    	getTsSegNoRank.compareFragmentMaxTime();
    }
  
    @Test
    public void testExtinfDataType() throws Exception{  //测试当extinf version为2和3时，extinf的数据类型
    	//getTsSegNoRank.judgeExtinfDataType();
    }
    
    @Test
    public void testCompareM3uPs() throws Exception{//测试m3u8和playservice接口时长对比
    	getTsSegNoRank.CompareM3uPs();
    }
    
    @Test
    public void testCompareTime() throws Exception{//测试时长连续性
    	getTsSegNoRank.CompareTime();
    }
    
    @Test
    public void testisOnlyValue() throws Exception{//测试标签唯一性
    	getTsSegNoRank.isOnlyValue();
    }
    
    @Test
    /**
     * @author wangyouqing
     * @throws Exception
     */
    public void testUnusualUrlResponse() throws Exception{//测试异常URL的返回
    	getUnusualUrlResponse.getUnusualUrlResponse();
    }
        
}
   
    