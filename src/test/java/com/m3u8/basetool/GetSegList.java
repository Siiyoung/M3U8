package com.m3u8.basetool;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GetSegList {

	String url="http://pl.youku.com/playlist/m3u8?vid=373787369&type=mp4&ts=1458296769&keyframe=0&ep=cyaREkuEUMkG7CvagT8bZCqzdCJeXP0N9RiNhtFkCdQkQey2&sid=14582967698591236a0ea&token=0949&ctype=12&ev=1&oip=3702858343";
	//String url = "http://pl.youku.com/playlist/m3u8?vid=21324343243543543534&ts=1458891649&ep=cSaREkGEV8gE5yrcjT8bZC3qJ3NdXP0M8xeDhtJjALgjT%2By8nD%2FUxZ62SfxLHv4RciEAGeLwq9nmaEMTePdGq2kP3SGpPuXp&sid=345889164293512318c4b&token=1729&ctype=12&ev=1&oip=3702858339";

    getResponse getResponse = new getResponse();
     
   
    
    /**
	 * 验证各标签唯一性
	 * @throws Exception 
	 * */
	public boolean isonlyvalue() throws Exception{
		String response = getResponse.getResponseByGet(url);
		String M3u="#EXTM3U";//标识播放列表文件扩展名的格式
		String MAXtime="#EXT-X-TARGETDURATION";//指定媒体段文件最大持续的时间
		String VERSION="#EXT-X-VERSION";//指定播放列表兼容性版本。相关的媒体文件和服务器都必须全部支持该标签指定的版本。
		String M3uEND="#EXT-X-ENDLIST";//该标签指示其后没有媒体文件段了
		int n=0;
		String s="";
		List<Integer> list=new ArrayList<Integer>(); 
		String [] sts={"#EXTM3U","#EXT-X-TARGETDURATION","#EXT-X-VERSION","#EXT-X-ENDLIST"};
		List<Integer> t_m3u=new ArrayList<Integer>(); 
		List<Integer> t_max=new ArrayList<Integer>(); 
		List<Integer> t_ver=new ArrayList<Integer>(); 
		List<Integer> t_mend=new ArrayList<Integer>(); 
		GetSegList l=new GetSegList();
		t_m3u=l.getindexof(response, M3u);
		t_max=l.getindexof(response, MAXtime);
		t_ver=l.getindexof(response, VERSION);
		t_mend=l.getindexof(response, M3uEND);
		list.add(t_m3u.size());
		list.add(t_max.size());
		list.add(t_ver.size());
		list.add(t_mend.size());
		for(int m=0;m<list.size();m++){
			int num=list.get(m);
			if(num==1){
				n++;
				//s=s+sts[m]+"标签正常\r\n";
			}
			else{
				s=s+sts[m]+"参数数量异常，为"+num+"个,请检查!"+"\r\n";
			}
		}
		if(n==4){
			s=s+M3u+","+MAXtime+","+VERSION+","+M3uEND+"标签唯一性验证通过。";
			System.out.println(s);
			return true;
		}
			
		else{
			s=s+"有错误！";
			System.out.println(s);
			throw new IOException(s+"有错误！");
		}				
	}
	
	
	
	/**
	 * 获取playservice的时长
	 * aim取值从：flvhd,mp4hd,mp4hd2中选取，标、高、超
	 * 目前不支持mp4hd3 1080p格式
	 * @throws Exception 
	 * */
	
	public List<Double> GetPstime() throws Exception{
		GetSegList gsl = new GetSegList();
		String definition = gsl.getDefinition();//获取视频清晰度		
		int vnum;
		String vid="";
	    vnum = url.indexOf("vid=");
	    if(vnum == -1){
	    	vnum = url.indexOf("%22v%22");
	    	if(vnum == -1){
	    		//return 
	    		throw new IOException("URL参数有问题，缺失vid参数！");
	    	}
	    	int n = url.indexOf("_", vnum);
	    	vid = url.substring(vnum+11, n);
	    }
	    else{
	    	int n = url.indexOf("&", vnum);
	    	vid = url.substring(vnum+4, n);
	    }
	    String v =vid.toString();
	    String psurl = "http://play.youku.com/play/get.json?vid="+v+"&ct=10&ran=7106";	
	    String response = Httpunit.getResponseByGet(psurl);
		//String response = (String) getResponse.getResponseByGet(psurl);
		GetSegList l=new GetSegList();
		List<Double> result = new ArrayList<Double>();
		String mvideo = "total_milliseconds_video\":\"";
		List<String> time = new ArrayList<String>();//各分片时长
		List<Integer> liststream = new ArrayList<Integer>();//各清晰度所在位置
		List<Integer> listaim = new ArrayList<Integer>();//获取目标清晰度各分片位置
		String aimstr = "";//获取目标清晰度的seg内容
		int aimpos = response.indexOf(definition)-14;//获取目标清晰度stream_type节点所在位置
		
		liststream = l.getindexof(response, "stream_type");
		for(int n=0;n<liststream.size();n++){
			if(liststream.get(n)==aimpos){
				if(n==0)
					aimstr = response.substring(0,liststream.get(n));
				else
					aimstr = response.substring(liststream.get(n-1), liststream.get(n));
			}
				
		}
		if(aimstr.equals(""))
			result.add((double) 0);//-1代表未找到该格式
		else{
			listaim = l.getindexof(aimstr, mvideo);
			for(int n:listaim){
				int m = n+mvideo.length();
				int i=0;
				while(i<aimstr.length()){
					if(aimstr.charAt(m+i)!='\"'){
						i++;
					}
					else{
						time.add(aimstr.substring(m,m+i));
						i=0;
						break;
					}
				}					
			}					
		}
		
		double d = 0;
		double count = 0;
		for(String s:time){	
			d=Double.valueOf(s).doubleValue()/1000;
			BigDecimal big = new BigDecimal(d);
			count = big.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			result.add(count);
			d = 0;
			
		}
		
		return result;			
	}
	
	/**
	 * 获取m3u8视频流时长
	 * @throws Exception 
	 * */		
	public List<Double> Getm3u8Time() throws Exception{
		GetSegList gid=new GetSegList();
		String response = getResponse.getResponseByGet(url+"&ykss=faacf356010c2b5c133c029a");
		List<Integer> L_EXD=new ArrayList<Integer>();//EXT-X-DISCONTINUITY参数位置链表 
		L_EXD=gid.getindexof(response,"#EXT-X-DISCONTINUITY");
	    double doubl=0;//ts流实际时长
		double doublm=0;//ts分片情况下总时长
		List<Double> result = new ArrayList<Double>();
		int size=L_EXD.size();//#EXT-X-DISCONTINUITY标签个数
		/**
		   * 获取ts时长*/
		  /*1没有分片*/
		  if(size==0){
			  doublm = gid.gettsendtime(response);
			  BigDecimal big = new BigDecimal(doublm);
			  double count = big.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			  result.add(count);
		  }
		  /*1有分片*/		  	 
		  else{			  
			  int i=0;
			  for(int n=0;n<=size;n++){
				  String rsps1="";
				  if(n!=size){
					  rsps1=response.substring(i,L_EXD.get(n));//第n分片字符串	
					  i=L_EXD.get(n);
				  }
				  else{
					  rsps1=response.substring(i);
				  }					  
				  doubl=gid.gettsendtime(rsps1);
				  BigDecimal big = new BigDecimal(doubl);
				  double count = big.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
				  result.add(count);
			  }
			  /*判断是否有广告*/	
			  int nu=gid.getAdnum();
			  if(nu!=0){
				  for(int n=0;n<nu;n++){
					  result.remove(0);//移除广告部分
				  }				      
			  }
		  }
		  return result;				
	}
	
	
	/**
	 * 获取视频清晰度
	 * @throws Exception 
	 * */
	public String getDefinition() throws Exception{
		String response = getResponse.getResponseByGet(url);
		String result= "";
		GetSegList g=new GetSegList();
		List<Integer> l1 = new ArrayList<Integer>();
		List<Integer> l2 = new ArrayList<Integer>();
		l1=g.getindexof(response, "http://");
		l2=g.getindexof(response, "ts_start");
		if(l1.size()!=l2.size()||l1.size()==0||l2.size()==0)
			return "error";
			//throw new IOException("TS流信息出现异常！"+"ts流数量为："+l1.size());
		else{
			int n=l1.size()-1;
			String str=response.substring(l1.get(n), l2.get(n));
			String [] st = str.split("/");
			String sd = st[st.length-1];
			String s = sd.substring(2,6);
			if(s.equals("0001"))
				result="mp4hd2";
			if(s.equals("0002"))
				result="flvhd";
			if(s.equals("0008"))
				result="mp4hd";	
			else
				result="error";
		}
		return result;	
	}
	
	
	/**
	 * 获取广告个数
	 * */
	public Integer getAdnum() throws Exception{
		GetSegList g = new GetSegList();
		List<Integer> l=new ArrayList<Integer>();
		for(int n=1;n<8;n++){
			String str = "%22a"+n+"%22";
			l=g.getindexof(url, str);
		}
		return l.size();
	}
	
	/**
	 * 获取去广告后的数据
	 * @throws Exception 
	 * */
	public String getDeteAdInfo() throws Exception{
		GetSegList g = new GetSegList();
		String response = getResponse.getResponseByGet(url);
		int adnum = g.getAdnum();
		if(adnum==0)
			return response;
		else{
			List<Integer> l = g.getindexof(response, "#EXT-X-DISCONTINUITY");
			int n = l.get(adnum-1);
			return response.substring(n);
		}		
	}
	
	/**
	 * 获取最后一个ts_end的值
	 * @throws Exception 
	 * */
	public double gettsendtime(String st) throws Exception{
		//String response = getResponse.getResponseByGet(url);
		double d;
		String s;
		GetSegList get=new GetSegList();
//		String [] resp = get.geteveryurl();
//		String str = resp[resp.length-1];
		List<String> l=get.getvalue(st,"ts_end=", "&ts_seg_no=");
		if(l.size()==0){
			return 0;
		}
		s=l.get(l.size()-1);
		d=Double.valueOf(s).doubleValue();
		return d;
	}
	
	
	/**
	 * 从str中获取str2的值，str3为目标值之后的字符串
	 * @throws Exception 
	 * */
	public List<String> getvalue(String str,String str2,String str3) throws Exception {
		List<Integer> ts1=new ArrayList<Integer>(); 
		List<Integer> ts2=new ArrayList<Integer>();
		List<String> value=new ArrayList<String>(); 
		int m=str2.length();
		GetSegList l=new GetSegList();

		ts1=l.getindexof(str, str2);
		ts2=l.getindexof(str, str3);
		for(int n=0;n<ts1.size();n++){
			int n1=ts1.get(n);
			int n2=ts2.get(n);
			value.add(str.substring(n1+m, n2));
		}
		return value;
	}
	
	/**
	 * 获取各分片的单独返回链表
	 * @throws Exception 
	 * */
	public String[] geteveryurl() throws Exception{	
		GetSegList get=new GetSegList();
		String response = get.getDeteAdInfo();
		return response.split("#EXT-X-DISCONTINUITY");		
	}
    
	public List<Integer> getindexof(String rep,String aim){
		List<Integer> list=new ArrayList<Integer>();
		int m=0;
		for(int n=0;n<=(rep.length()-aim.length());n++){
			while(m<aim.length()){
				if(rep.charAt(n+m)!=aim.charAt(m))
					break;
				m++;
			}
			if(m==aim.length()){
				list.add(n);
				m=0;
			}
		}
		return list;
	}
	
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
		Pattern pattern=Pattern.compile("(?<=EXT-X-TARGETDURATION:)(\\-|\\+?)\\d+");
		List<String> results=new ArrayList<String>();
		for(String segList:strs){
			Matcher matcher=pattern.matcher(segList);
			if(matcher.find()){
				results.add(matcher.group());
			}
		
		}
		return results;
	}
    
    public List<String> solveExtinf(String[] strs) throws IOException{    //正则匹配EXTINF: 获取其值
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
    	Pattern pattern=Pattern.compile("(?<=EXT-X-VERSION:)\\d+");
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
		List<String> strs=solveExtVersion(resp);
				
		for(String s:strs){
			extVersion.add(s);			
		}		
		return extVersion;    	
    }
    
    
}
	
		
