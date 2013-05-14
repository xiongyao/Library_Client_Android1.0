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
	
	//Streamҳ��������Host��ַ �˿���80
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
	//��Ŀ���ַ ʵ��һ��POST����
	private HttpClient client =new HttpClient();
	private PostMethod post=new PostMethod("http://opac.lib.whpu.edu.cn:80/cgi-bin/IlaswebBib");
	
	/*
	 * ��һҳ
	 */
	public List nextPage(){
		client.getHostConfiguration().setHost("http://opac.lib.whpu.edu.cn",80);
		post.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk"); 
		post.setRequestBody(pairs);
		//ִ��POST����
		List list=new ArrayList();
		try {
			client.executeMethod(post);
			//��POST���ص�������������ʽ���룬�ٰ�����������һ��buff�����ֽ�����
			//StreamTool�������Լ�д��һ�������࣬�����ݽ������ĸ���
			byte[] buff = StreamTool.readInputStream(post.getResponseBodyAsStream());
			String html = new String(buff,"GBK");
			Log.i("html", html);
			
			Document document=Jsoup.parse(html);
			//ȡ��������ҳ�����е�Tr��ǩ��ͷ��Ԫ��
			Elements trs=document.select("TR");
			Elements inputs=document.select("INPUT");
			Cursrc=Integer.parseInt(document.select("INPUT[name=v_curscr]").first().attr("value"));
			Pagenum=Integer.parseInt(document.select("INPUT[name=v_pagenum]").first().attr("value"));
			int totalInputs=inputs.size();
			//��ȡ��һҳҪPOST��ȥ����Ϣ
			
			for(int i=0;i<totalInputs;i++){
				Element element=inputs.get(i);
				pairs[i]=new NameValuePair(element.attr("name"), element.attr("value"));
			}
			int totalTrs=trs.size();
			for(int i=2;i<totalTrs-2;i++){
				//��ȡ���tr�����tdԪ��
				Elements tds=trs.get(i).select("td");
				int totalTds=tds.size();
				Map<String,String> map=new HashMap<String,String>();
				for(int j=0;j<totalTds;j++){
					switch (j) {
					
					case 0:
						/*	0��ʾ��һ����������
						 *	put��������map����һ����ֵ��
						 *	html()�����͵õ���ǩ������������
						*/
						map.put("book_title", tds.get(j).html().toString());
						break;
					case 1:
						/* 	1��ʾ�ڶ���������
						 * 
						 */
						map.put("book_author", tds.get(j).html().toString());
						break;
					case 2:
						/* 	2��ʾ��������������
						 * 
						 */
						map.put("book_press", tds.get(j).html().toString());
						break;
					case 3:
						/* 	3��ʾ���ĸ���ҳ��
						 * 
						 */
						map.put("book_pages", tds.get(j).html().toString());
						break;
					case 4:
						/* 	4��ʾ��������۸�
						 * 
						 */
						map.put("book_price", tds.get(j).html().toString());
						break;
					case 5:
						/* 	5��ʾ����������ȡ��
						 * 
						 */
						map.put("book_noFor", tds.get(j).html().toString());
						break;
					case 6:
						/* 	6��ʾ���߸�����ϸ��Ϣ����ַ��
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
			//��������ˣ��ͷ�����
			post.releaseConnection();
			return list;
		}
	}
	/**
	 * ��ȡ��ѡ�鼮����ϸ��Ϣ
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
			//��GET���ص�������������ʽ���룬�ٰ�����������һ��buff�����ֽ�����
			//StreamTool�������Լ�д��һ�������࣬�����ݽ������ĸ���
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
						//�����
						map.put("book_barcode", td.get(j).html().toString());
						break;
					case 1:
						//�ݲصص�
						map.put("book_lib", td.get(j).html().toString());
						break;
					case 2:
						//��ͨ����
						map.put("book_type", td.get(j).html().toString());
						break;
					case 3:
						//״̬
						map.put("book_state", td.get(j).html().toString());
						break;
					case 4:
						//Ӧ������
						map.put("book_backTime", td.get(j).html().toString());
						break;
					case 5:
						//���˵��
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
	 * ��ѯҳ���ѯ����ȡBookList
	 */
	public List getList(Bundle bundle) {
		// TODO Auto-generated method stub
		client.getHostConfiguration().setHost("http://opac.lib.whpu.edu.cn",80);
		post.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");  
		//����Ҫ�ļ�ֵ��д����
		NameValuePair beg = new NameValuePair("FLD_DAT_BEG" , "");
		NameValuePair end = new NameValuePair("FLD_DAT_END" , "");
		NameValuePair vIndex = new NameValuePair("v_index" , bundle.getString("v_index"));
		NameValuePair vLogicSrch = new NameValuePair("v_LogicSrch" , bundle.getString("v_LogicSrch"));
		NameValuePair vPagenum = new NameValuePair("v_pagenum" , bundle.getString("v_pagenum"));
		NameValuePair vSeldatabase = new NameValuePair("v_seldatabase" , bundle.getString("v_seldatabase"));
		NameValuePair vValue = new NameValuePair("v_value" ,bundle.getString("v_value"));
		//NameValuePair vValue = new NameValuePair("v_value" ,"java");
		//��POST��������������ֵ��
		post.setRequestBody(new NameValuePair[] {beg , end  , vIndex , vLogicSrch , vPagenum , vSeldatabase , vValue });
		
		
		//post.setRequestEntity(entity);
		//ִ��POST����
		List list=new ArrayList();
		try {
			client.executeMethod(post);
			//��POST���ص�������������ʽ���룬�ٰ�����������һ��buff�����ֽ�����
			//StreamTool�������Լ�д��һ�������࣬�����ݽ������ĸ���
			byte[] buff = StreamTool.readInputStream(post.getResponseBodyAsStream());
			String html = new String(buff,"GBK");
			Log.i("html", html);
			
			Document document=Jsoup.parse(html);
			//ȡ��������ҳ�����е�Tr��ǩ��ͷ��Ԫ��
			Elements trs=document.select("TR");
			Elements inputs=document.select("INPUT");
			Cursrc=Integer.parseInt(document.select("INPUT[name=v_curscr]").first().attr("value"));
			Pagenum=Integer.parseInt(document.select("INPUT[name=v_pagenum]").first().attr("value"));
			int totalInputs=inputs.size();
			//��ȡ��һҳҪPOST��ȥ����Ϣ
			
			for(int i=0;i<totalInputs;i++){
				Element element=inputs.get(i);
				pairs[i]=new NameValuePair(element.attr("name"), element.attr("value"));
			}
			int totalTrs=trs.size();
			for(int i=2;i<totalTrs-2;i++){
				//��ȡ���tr�����tdԪ��
				Elements tds=trs.get(i).select("td");
				int totalTds=tds.size();
				Map<String,String> map=new HashMap<String,String>();
				for(int j=0;j<totalTds;j++){
					switch (j) {
					
					case 0:
						/*	0��ʾ��һ����������
						 *	put��������map����һ����ֵ��
						 *	html()�����͵õ���ǩ������������
						*/
						map.put("book_title", tds.get(j).html().toString());
						break;
					case 1:
						/* 	1��ʾ�ڶ���������
						 * 
						 */
						map.put("book_author", tds.get(j).html().toString());
						break;
					case 2:
						/* 	2��ʾ��������������
						 * 
						 */
						map.put("book_press", tds.get(j).html().toString());
						break;
					case 3:
						/* 	3��ʾ���ĸ���ҳ��
						 * 
						 */
						map.put("book_pages", tds.get(j).html().toString());
						break;
					case 4:
						/* 	4��ʾ��������۸�
						 * 
						 */
						map.put("book_price", tds.get(j).html().toString());
						break;
					case 5:
						/* 	5��ʾ����������ȡ��
						 * 
						 */
						map.put("book_noFor", tds.get(j).html().toString());
						break;
					case 6:
						/* 	6��ʾ���߸�����ϸ��Ϣ����ַ��
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
			//��������ˣ��ͷ�����
			post.releaseConnection();
			return list;
		}
	}
}

