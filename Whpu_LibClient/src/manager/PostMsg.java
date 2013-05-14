package manager;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HostParams;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.util.Log;

import Tool.StreamTool;

public class PostMsg {
	
	//Stream页面里面有Host地址 端口是80
	public static String v_index="v_index";
	public static String v_value="v_value";
	public static String v_pagenum="v_pagenum";
	public static String v_count="v_count";
	public static String FLD_DAT_BEG="FLD_DAT_BEG";
	public static String FLD_DAT_END="FLD_DAT_END";
	public static String v_LogicSrch="v_LogicSrch";
	public static String v_LogicKeyLen="v_LogicKeyLen";
	public static String v_seldatabase="v_seldatabase";
	public static String v_curkey="v_curkey";
	public static String v_addr="v_addr";
	public static String v_curdbno="v_curdbno";
	public static int Cursrc=1;
	public static int Pagenum=10;
	
	public static NameValuePair[] pairs=new NameValuePair[13];
	//用目标地址 实例一个POST方法
	private HttpClient client =new HttpClient();
	private PostMethod post=new PostMethod("http://opac.lib.whpu.edu.cn:80/cgi-bin/IlaswebBib");
	
	/*
	 * 下一页
	 */
	public List nextPage(){
		client.getHostConfiguration().setHost("http://opac.lib.whpu.edu.cn",80);
		post.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk"); 
		post.setRequestBody(pairs);
		//执行POST方法
		List list=new ArrayList();
		try {
			client.executeMethod(post);
			//将POST返回的数据以流的形式读入，再把输入流流至一个buff缓冲字节数组
			//StreamTool类是我自己写的一个工具类，其内容将在下文附出
			byte[] buff = StreamTool.readInputStream(post.getResponseBodyAsStream());
			String html = new String(buff,"GBK");
			Log.i("html", html);
			
			Document document=Jsoup.parse(html);
			//取得整个网页中所有的Tr标签开头的元素
			Elements trs=document.select("TR");
			Elements inputs=document.select("INPUT");
			Cursrc=Integer.parseInt(document.select("INPUT[name=v_curscr]").first().attr("value"));
			Pagenum=Integer.parseInt(document.select("INPUT[name=v_pagenum]").first().attr("value"));
			int totalInputs=inputs.size();
			//获取下一页要POST过去的信息
			
			for(int i=0;i<totalInputs;i++){
				Element element=inputs.get(i);
				pairs[i]=new NameValuePair(element.attr("name"), element.attr("value"));
			}
			int totalTrs=trs.size();
			for(int i=2;i<totalTrs-2;i++){
				//获取这个tr里面的td元素
				Elements tds=trs.get(i).select("td");
				int totalTds=tds.size();
				Map<String,String> map=new HashMap<String,String>();
				for(int j=0;j<totalTds;j++){
					switch (j) {
					
					case 0:
						/*	0表示第一个，即书名
						 *	put方法即向map加入一条键值对
						 *	html()方法就得到标签括起来的内容
						*/
						map.put("book_title", tds.get(j).html().toString());
						break;
					case 1:
						/* 	1表示第二个，作者
						 * 
						 */
						map.put("book_author", tds.get(j).html().toString());
						break;
					case 2:
						/* 	2表示第三个，出版社
						 * 
						 */
						map.put("book_press", tds.get(j).html().toString());
						break;
					case 3:
						/* 	3表示第四个，页数
						 * 
						 */
						map.put("book_pages", tds.get(j).html().toString());
						break;
					case 4:
						/* 	4表示第五个，价格
						 * 
						 */
						map.put("book_price", tds.get(j).html().toString());
						break;
					case 5:
						/* 	5表示第六个，索取号
						 * 
						 */
						map.put("book_noFor", tds.get(j).html().toString());
						break;
					case 6:
						/* 	6表示第七个，详细信息（网址）
						 * 
						 */
						map.put("book_detail", tds.get(j).select("a").attr("href").toString());
						break;
					default:
						break;
					}
				}
				list.add(map);
			}
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//任务完成了，释放连接
			post.releaseConnection();
			return list;
		}
	}
	/**
	 * 获取所选书籍的详细信息
	 * @param bundle
	 * @return
	 */
	public List getDetail(String href){
		List list=new ArrayList();
		String url="http://opac.lib.whpu.edu.cn:80/cgi-bin/"+href;
		GetMethod get=new GetMethod(url);
		get.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");   
		try {
			client.executeMethod(get);
			//将GET返回的数据以流的形式读入，再把输入流流至一个buff缓冲字节数组
			//StreamTool类是我自己写的一个工具类，其内容将在下文附出
			byte[] buff = StreamTool.readInputStream(get.getResponseBodyAsStream());
			String html = new String(buff,"GBK");
			Log.i("html", html);
			Document document=Jsoup.parse(html);
			Elements tr=document.select("div[name=holding] TR");
			System.out.println("tr.size:"+tr.size());
			for(int i=1;i<tr.size()-1;i++){
				Map<String,String> map=new HashMap<String,String>();
				Elements td=tr.get(i).select("td");
				for(int j=0;j<td.size();j++){
					System.out.printf(td.get(j).html());
					switch (j) {
					case 0:
						//条码号
						map.put("book_barcode", td.get(j).html().toString());
						break;
					case 1:
						//馆藏地点
						map.put("book_lib", td.get(j).html().toString());
						break;
					case 2:
						//流通类型
						map.put("book_type", td.get(j).html().toString());
						break;
					case 3:
						//状态
						map.put("book_state", td.get(j).html().toString());
						break;
					case 4:
						//应还日期
						map.put("book_backTime", td.get(j).html().toString());
						break;
					case 5:
						//卷册说明
						map.put("book_explain", td.get(j).html().toString());
						break;
					default:
						break;
					}
				}
				list.add(map);
			}
			
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
		
	/*
	 * 查询页面查询，获取BookList
	 */
	public List getList(Bundle bundle) {
		// TODO Auto-generated method stub
		client.getHostConfiguration().setHost("http://opac.lib.whpu.edu.cn",80);
		post.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");  
		//将需要的键值对写出来
		NameValuePair beg = new NameValuePair("FLD_DAT_BEG" , "");
		NameValuePair end = new NameValuePair("FLD_DAT_END" , "");
		NameValuePair vIndex = new NameValuePair("v_index" , bundle.getString("v_index"));
		NameValuePair vLogicSrch = new NameValuePair("v_LogicSrch" , bundle.getString("v_LogicSrch"));
		NameValuePair vPagenum = new NameValuePair("v_pagenum" , bundle.getString("v_pagenum"));
		NameValuePair vSeldatabase = new NameValuePair("v_seldatabase" , bundle.getString("v_seldatabase"));
		NameValuePair vValue = new NameValuePair("v_value" ,bundle.getString("v_value"));
		//NameValuePair vValue = new NameValuePair("v_value" ,"java");
		//给POST方法加入上述键值对
		post.setRequestBody(new NameValuePair[] {beg , end  , vIndex , vLogicSrch , vPagenum , vSeldatabase , vValue });
		
		
		//post.setRequestEntity(entity);
		//执行POST方法
		List list=new ArrayList();
		try {
			client.executeMethod(post);
			//将POST返回的数据以流的形式读入，再把输入流流至一个buff缓冲字节数组
			//StreamTool类是我自己写的一个工具类，其内容将在下文附出
			byte[] buff = StreamTool.readInputStream(post.getResponseBodyAsStream());
			String html = new String(buff,"GBK");
			Log.i("html", html);
			
			Document document=Jsoup.parse(html);
			//取得整个网页中所有的Tr标签开头的元素
			Elements trs=document.select("TR");
			Elements inputs=document.select("INPUT");
			Cursrc=Integer.parseInt(document.select("INPUT[name=v_curscr]").first().attr("value"));
			Pagenum=Integer.parseInt(document.select("INPUT[name=v_pagenum]").first().attr("value"));
			int totalInputs=inputs.size();
			//获取下一页要POST过去的信息
			
			for(int i=0;i<totalInputs;i++){
				Element element=inputs.get(i);
				pairs[i]=new NameValuePair(element.attr("name"), element.attr("value"));
			}
			int totalTrs=trs.size();
			for(int i=2;i<totalTrs-2;i++){
				//获取这个tr里面的td元素
				Elements tds=trs.get(i).select("td");
				int totalTds=tds.size();
				Map<String,String> map=new HashMap<String,String>();
				for(int j=0;j<totalTds;j++){
					switch (j) {
					
					case 0:
						/*	0表示第一个，即书名
						 *	put方法即向map加入一条键值对
						 *	html()方法就得到标签括起来的内容
						*/
						map.put("book_title", tds.get(j).html().toString());
						break;
					case 1:
						/* 	1表示第二个，作者
						 * 
						 */
						map.put("book_author", tds.get(j).html().toString());
						break;
					case 2:
						/* 	2表示第三个，出版社
						 * 
						 */
						map.put("book_press", tds.get(j).html().toString());
						break;
					case 3:
						/* 	3表示第四个，页数
						 * 
						 */
						map.put("book_pages", tds.get(j).html().toString());
						break;
					case 4:
						/* 	4表示第五个，价格
						 * 
						 */
						map.put("book_price", tds.get(j).html().toString());
						break;
					case 5:
						/* 	5表示第六个，索取号
						 * 
						 */
						map.put("book_noFor", tds.get(j).html().toString());
						break;
					case 6:
						/* 	6表示第七个，详细信息（网址）
						 * 
						 */
						map.put("book_detail", tds.get(j).select("a").attr("href").toString());
						break;
					default:
						break;
					}
				}
				list.add(map);
			}
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//任务完成了，释放连接
			post.releaseConnection();
			return list;
		}
	}
}

