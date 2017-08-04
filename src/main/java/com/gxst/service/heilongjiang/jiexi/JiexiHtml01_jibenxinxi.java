package com.gxst.service.heilongjiang.jiexi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.service.data.Paramters;
import com.gxst.util.ConnectionDB;
import com.gxst.util.ToDBC;

public class JiexiHtml01_jibenxinxi {

	private static ConnectionDB db_local2 = new ConnectionDB(
			"jdbc:mysql://localhost:3306/db_gsxt?useUnicode=true&characterEncoding=utf8", "root", "123");
	private static Map<String, Object> map = relation();

	public static void main(String[] args) {
		jiexi();
	}

	public static void jiexi() {
		List<Map<String, Object>> htmls = db_local2
				.excuteQuery("select html_tab01_text,keyword,company_no,company_name from company_hlj", null);

		for (Map<String, Object> html : htmls) {

			if (html.get("html_tab01_text") == null) {
				continue;
			}
			String disposerRealName = ToDBC.tool(html.get("company_name").toString());

			String mainHtml = html.get("html_tab01_text").toString();
			Document doc = Jsoup.parse(mainHtml);

			Elements tbs = doc.select("table");

			for (Element tb : tbs) {
				if (tb.select("th").size() > 0) {
					String name = tb.select("th").first().text();
//
//					if (name.contains("基本信息")) {
//						try {
//							List<String> colNames = new ArrayList<String>();
//							List<Object> colValues = new ArrayList<Object>();
//
//							colNames.add("comName");
//							colNames.add("uniqueno");
//							colNames.add("realName");
//							colNames.add("disposerRealName");
//
//							colValues.add(html.get("keyword"));
//							colValues.add(html.get("company_no"));
//							colValues.add(html.get("company_name"));
//							colValues.add(disposerRealName);
//
//							Elements ths = tb.select("th");
//							String startDate = "", endDate = "";
//							for (Element th : ths) {
//								if (null != th.nextElementSibling()) {
//									String title = th.text();
//
//									if (title.equals("营业期限自")) {
//										startDate = th.nextElementSibling().text();
//									}
//									if (title.equals("营业期限至")) {
//										endDate = th.nextElementSibling().text();
//									}
//
//									if (null == map.get(title)) {
//										continue;
//									}
//									String colName = map.get(title).toString();
//									String colValue = th.nextElementSibling().text();
//									colNames.add(colName);
//									colValues.add(colValue);
//
//								}
//							}
//							if (!startDate.equals("") || !endDate.equals("")) {
//								if(!colNames.contains("yingyeqixian")){
//									colNames.add("yingyeqixian");
//									colValues.add(startDate + " 至 " + endDate);
//								}
//								
//							}
//
//							try {
//								db_local2.insert(colNames.toArray(new String[colNames.size()]),
//										colValues.toArray(new Object[colValues.size()]), "comp_gongshangxinxi");
//							} catch (Exception e) {
//
//								if (colNames.size() != colValues.size()) {
//									Paramters.log
//											.info(html.get("company_name").toString() + " 的 jibenxinxi 参数值大小和列不一样");
//									System.out.println(colNames);
//									System.out.println(colValues);
//								}
//								Paramters.log.info(html.get("company_name").toString() + e.getMessage());
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//							Paramters.log.info(html.get("company_name").toString() + e.getMessage());
//						}
//					}

						if (name.contains("股东信息")) {
							
							// 解析股东信息
							try {
								tb = tb.nextElementSibling();
								Elements trs = tb.select("tr");

								for (Element tr : trs) {
									Elements tds = tr.select("td");

									if (tds.size() > 1) {
										List<String> colNames = new ArrayList<String>();
										List<Object> colValues = new ArrayList<Object>();

										colNames.add("comName");
										colNames.add("uniqueno");
										colNames.add("realName");
										colNames.add("disposerRealName");

										colValues.add(html.get("keyword"));
										colValues.add(html.get("company_no"));
										colValues.add(html.get("company_name"));
										colValues.add(disposerRealName);

										colNames.add("gudongmingzi");
										colValues.add(tds.get(1).text());

										colNames.add("gudongleixing");
										colValues.add(tds.get(0).text());
										System.out.println(colValues);

										try {
											db_local2.insert(colNames.toArray(new String[colNames.size()]),
													colValues.toArray(new Object[colValues.size()]),
													"comp_gudongxinxi");
										} catch (Exception e) {

											if (colNames.size() != colValues.size()) {
												Paramters.log.info(html.get("company_name").toString()
														+ " 的 comp_xingzhengchufa 参数值大小和列不一样");
												System.out.println(colNames);
												System.out.println(colValues);
											}
											Paramters.log.info(html.get("company_name").toString() + e.getMessage());
										}
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
//						if (name.contains("变更信息")) {
//							try {
//								Elements trs = tb.select("tr");
//								for (Element tr : trs) {
//
//									Elements tds = tr.select("td");
//
//									if (tds.size() > 0) {
//										List<String> colNames = new ArrayList<String>();
//										List<Object> colValues = new ArrayList<Object>();
//
//										colNames.add("comName");
//										colNames.add("uniqueno");
//										colNames.add("realName");
//										colNames.add("disposerRealName");
//
//										colValues.add(html.get("keyword"));
//										colValues.add(html.get("company_no"));
//										colValues.add(html.get("company_name"));
//										colValues.add(disposerRealName);
//
//										colNames.add("biangengxiangmu");
//										colValues.add(tds.get(0).text());
//										if (tds.size() > 1) {
//											colNames.add("biangengqian");
//											colValues.add(tds.get(1).text());
//										}
//										if (tds.size() > 4) {
//											colNames.add("biangenghou");
//											colValues.add(tds.get(2).text());
//										}
//										if (tds.size() > 3) {
//											colNames.add("biangengriqi");
//											colValues.add(tds.get(3).text());
//										}
//
//										try {
//											Paramters.db_local.insert(colNames.toArray(new String[colNames.size()]),
//													colValues.toArray(new Object[colValues.size()]), "comp_biangengjilu");
//										} catch (Exception e) {
//
//											if (colNames.size() != colValues.size()) {
//												Paramters.log.info(
//														html.get("company_name").toString() + " 的 biangengjilu 参数值大小和列不一样");
//												System.out.println(colNames);
//												System.out.println(colValues);
//											}
//											Paramters.log.info(html.get("company_name").toString() + e.getMessage());
//										}
//									}
//								}
//							} catch (Exception e) {
//								e.printStackTrace();
//								Paramters.log.info(html.get("company_name").toString() + e.getMessage());
//							}
//						}
//						
//						if (name.contains("主要人员信息")) {
//							
//							try {
//								Elements trs = tb.select("tr");
//								for (Element tr : trs) {
//									Elements tds = tr.select("td");
//									if(tds.size()>2){
//										List<String> colNames = new ArrayList<String>();
//										List<Object> colValues = new ArrayList<Object>();
//
//										colNames.add("comName");
//										colNames.add("uniqueno");
//										colNames.add("realName");
//										colNames.add("disposerRealName");
//
//										colValues.add(html.get("keyword"));
//										colValues.add(html.get("company_no"));
//										colValues.add(html.get("company_name"));
//										colValues.add(disposerRealName);
//										
//										colNames.add("mingzi");
//										colValues.add(tds.get(1).text());
//										
//										colNames.add("zhiwei");
//										colValues.add(tds.get(2).text());
//										
//										try {
//											Paramters.db_local.insert(colNames.toArray(new String[colNames.size()]),
//													colValues.toArray(new Object[colValues.size()]), "comp_zhuyaorenyuan");
//										} catch (Exception e) {
//
//											if (colNames.size() != colValues.size()) {
//												Paramters.log.info(html.get("company_name").toString() + " 的 comp_zhuyaorenyuan 参数值大小和列不一样");
//												System.out.println(colNames);
//												System.out.println(colValues);
//											}
//											Paramters.log.info(html.get("company_name").toString() + e.getMessage());
//										}
//									}
//									if(tds.size()>3){
//										List<String> colNames = new ArrayList<String>();
//										List<Object> colValues = new ArrayList<Object>();
//
//										colNames.add("comName");
//										colNames.add("uniqueno");
//										colNames.add("realName");
//										colNames.add("disposerRealName");
//
//										colValues.add(html.get("keyword"));
//										colValues.add(html.get("company_no"));
//										colValues.add(html.get("company_name"));
//										colValues.add(disposerRealName);
//										
//										colNames.add("mingzi");
//										colValues.add(tds.get(4).text());
//										
//										colNames.add("zhiwei");
//										colValues.add(tds.get(5).text());
//										
//										try {
//											Paramters.db_local.insert(colNames.toArray(new String[colNames.size()]),
//													colValues.toArray(new Object[colValues.size()]), "comp_zhuyaorenyuan");
//										} catch (Exception e) {
//
//											if (colNames.size() != colValues.size()) {
//												Paramters.log.info(html.get("company_name").toString() + " 的 comp_zhuyaorenyuan 参数值大小和列不一样");
//												System.out.println(colNames);
//												System.out.println(colValues);
//											}
//											Paramters.log.info(html.get("company_name").toString() + e.getMessage());
//										}
//									}
//								}
//							} catch (Exception e) {
//								e.printStackTrace();
//								Paramters.log.info(html.get("company_name").toString() + e.getMessage());
//							}
//						}
//						
//						if (name.contains("分支机构信息")) {
//							
//							try{
//								
//								Elements trs = tb.select("tr");
//								for (Element tr : trs) {
//
//									Elements tds = tr.select("td");
//
//										if (tds.size() == 0) {
//											continue;
//										}
//										List<String> colNames = new ArrayList<String>();
//										List<Object> colValues = new ArrayList<Object>();
//
//										colNames.add("comName");
//										colNames.add("uniqueno");
//										colNames.add("realName");
//										colNames.add("disposerRealName");
//
//										colValues.add(html.get("keyword"));
//										colValues.add(html.get("company_no"));
//										colValues.add(html.get("company_name"));
//										colValues.add(disposerRealName);
//
//										if (tds.size() > 2) {
//											colNames.add("mingzi");
//											colValues.add(tds.get(2).text());
//										}
//
//										try {
//											Paramters.db_local.insert(colNames.toArray(new String[colNames.size()]),
//													colValues.toArray(new Object[colValues.size()]), "comp_fenzhijigou");
//										} catch (Exception e) {
//
//											if (colNames.size() != colValues.size()) {
//												Paramters.log
//														.info(html.get("company_name").toString() + " 的 comp_fenzhijigou 参数值大小和列不一样");
//												System.out.println(colNames);
//												System.out.println(colValues);
//											}
//											Paramters.log.info(html.get("company_name").toString() + e.getMessage());
//										}
//									}
//							}catch(Exception e){
//								e.printStackTrace();
//								Paramters.log.info(html.get("company_name").toString() + e.fillInStackTrace());
//							}
//						}
//						
//						if (name.contains("清算信息")) {
//							
//							try {
//								List<String> colNames = new ArrayList<String>();
//								List<Object> colValues = new ArrayList<Object>();
//
//								colNames.add("comName");
//								colNames.add("uniqueno");
//								colNames.add("realName");
//								colNames.add("disposerRealName");
//
//								colValues.add(html.get("keyword"));
//								colValues.add(html.get("company_no"));
//								colValues.add(html.get("company_name"));
//								colValues.add(disposerRealName);
//								Elements ths = tb.select("th");
//								for (Element th : ths) {
//									if (null != th.nextElementSibling()) {
//										
//										if(th.text().trim().equals("清算负责人")){
//											colNames.add("qingsuanzufuzeren");
//											colValues.add(th.nextElementSibling().text());
//										}
//										if(th.text().trim().equals("清算负责人")){
//											colNames.add("qingsuanzuchengyuan");
//											colValues.add(th.nextElementSibling().text());
//										}
//									}
//								}
//								try {
//									db_local2.insert(colNames.toArray(new String[colNames.size()]),
//											colValues.toArray(new Object[colValues.size()]), "comp_qingsuanxinxi");
//								} catch (Exception e) {
//
//									if (colNames.size() != colValues.size()) {
//										Paramters.log.info(html.get("company_name").toString() + " 的 comp_qingsuanxinxi 参数值大小和列不一样");
//										System.out.println(colNames);
//										System.out.println(colValues);
//									}
//									Paramters.log.info(html.get("company_name").toString() + e.getMessage());
//								}
//							} catch (Exception e) {
//								e.printStackTrace();
//								Paramters.log.info(html.get("company_name").toString() + e.getMessage());
//							}
//						}
//						if (name.contains("股权出质登记信息")) {
//							
//							try{
//								Elements trs = tb.select("tr");
//								
//								for(Element tr:trs){
//									Elements tds = tr.select("td");
//									
//									if(tds.size()>6){
//										
//										String all = "";
//										for(Element td:tds){
//										
//											all += td.text();
//										}
//										if(all.equals("")||all.equals("暂无数据")){
//											continue;
//										}
//										
//										List<String> colNames = new ArrayList<String>();
//										List<Object> colValues = new ArrayList<Object>();
//
//										colNames.add("comName");
//										colNames.add("uniqueno");
//										colNames.add("realName");
//										colNames.add("disposerRealName");
//
//										colValues.add(html.get("keyword"));
//										colValues.add(html.get("company_no"));
//										colValues.add(html.get("company_name"));
//										colValues.add(disposerRealName);
//										
//										colNames.add("dengjibianhao");
//										colValues.add(tds.get(1).text());
//										
//										colNames.add("chuzhiren");
//										colValues.add(tds.get(2).text());
//										
//										colNames.add("chuzhiguquanshue");
//										colValues.add(tds.get(4).text());
//										
//										colNames.add("zhiquanren");
//										colValues.add(tds.get(5).text());
//										
//										colNames.add("zzzjhm");
//										colValues.add(tds.get(6).text());
//										
//										colNames.add("gqczsldjrq");
//										colValues.add(tds.get(7).text());
//										
//										colNames.add("zhuangtai");
//										colValues.add(tds.get(8).text());
//										
//										try {
//											db_local2.insert(colNames.toArray(new String[colNames.size()]),
//													colValues.toArray(new Object[colValues.size()]), "comp_guquanchuzhi");
//										} catch (Exception e) {
//
//											if (colNames.size() != colValues.size()) {
//												Paramters.log.info(html.get("company_name").toString() + " 的 comp_guquanchuzhi 参数值大小和列不一样");
//												System.out.println(colNames);
//												System.out.println(colValues);
//											}
//											Paramters.log.info(html.get("company_name").toString() + e.getMessage());
//										}
//									}
//								}
//							}catch(Exception e){
//								e.printStackTrace();
//							}
//						}
//							if (name.contains("动产抵押登记信息")) {
//								
//								try{
//									Elements trs = tb.select("tr");
//									
//									for(Element tr:trs){
//										Elements tds = tr.select("td");
//										
//										if(tds.size()>6){
//											
//											String all = "";
//											for(Element td:tds){
//											
//												all += td.text();
//											}
//											if(all.equals("")||all.equals("暂无数据")){
//												continue;
//											}
//											
//											List<String> colNames = new ArrayList<String>();
//											List<Object> colValues = new ArrayList<Object>();
//
//											colNames.add("comName");
//											colNames.add("uniqueno");
//											colNames.add("realName");
//											colNames.add("disposerRealName");
//
//											colValues.add(html.get("keyword"));
//											colValues.add(html.get("company_no"));
//											colValues.add(html.get("company_name"));
//											colValues.add(disposerRealName);
//											
//											colNames.add("dengjibianhao");
//											colValues.add(tds.get(1).text());
//											
//											colNames.add("dengjiriqi");
//											colValues.add(tds.get(2).text());
//											
//											colNames.add("dengjijiguan");
//											colValues.add(tds.get(3).text());
//											
//											colNames.add("bdbzqse");
//											colValues.add(tds.get(4).text());
//											
//											colNames.add("zhuangtai");
//											colValues.add(tds.get(5).text());
//											
//											try {
//												Paramters.db_local.insert(colNames.toArray(new String[colNames.size()]),
//														colValues.toArray(new Object[colValues.size()]), "comp_dongchandiya");
//											} catch (Exception e) {
//
//												if (colNames.size() != colValues.size()) {
//													Paramters.log.info(html.get("company_name").toString() + " 的 comp_dongchandiya 参数值大小和列不一样");
//													System.out.println(colNames);
//													System.out.println(colValues);
//												}
//												Paramters.log.info(html.get("company_name").toString() + e.getMessage());
//											}
//										}
//									}
//								}catch(Exception e){
//									e.printStackTrace();
//								}
//							}
//								if (name.contains("经营异常信息")) {
//									try{
//										Elements trs = tb.select("tr");
//										
//										for(Element tr:trs){
//											Elements tds = tr.select("td");
//											
//											if(tds.size()>3){
//												
//												String all = "";
//												for(Element td:tds){
//												
//													all += td.text();
//												}
//												System.out.println("文本信息："+all);
//												if(all.equals("")||all.equals("暂无数据")){
//													continue;
//												}
//												
//												List<String> colNames = new ArrayList<String>();
//												List<Object> colValues = new ArrayList<Object>();
//
//												colNames.add("comName");
//												colNames.add("uniqueno");
//												colNames.add("realName");
//												colNames.add("disposerRealName");
//
//												colValues.add(html.get("keyword"));
//												colValues.add(html.get("company_no"));
//												colValues.add(html.get("company_name"));
//												colValues.add(disposerRealName);
//												
//												colNames.add("lrjyycmlyy");
//												colValues.add(tds.get(1).text());
//												
//												colNames.add("lieruriqi");
//												colValues.add(tds.get(2).text());
//
//												colNames.add("ycjyycmlyy");
//												colValues.add(tds.get(3).text());
//												
//												colNames.add("yichuriqi");
//												colValues.add(tds.get(4).text());
//												
//												colNames.add("zuochujuedingjiguan");
//												colValues.add(tds.get(5).text());
//												
//												try {
//													Paramters.db_local.insert(colNames.toArray(new String[colNames.size()]),
//															colValues.toArray(new Object[colValues.size()]), "comp_jingyingyichang");
//												} catch (Exception e) {
//
//													if (colNames.size() != colValues.size()) {
//														Paramters.log.info(html.get("company_name").toString() + " 的 comp_jingyingyichang 参数值大小和列不一样");
//														System.out.println(colNames);
//														System.out.println(colValues);
//													}
//													Paramters.log.info(html.get("company_name").toString() + e.getMessage());
//												}
//											}
//										}
//									}catch(Exception e){
//										e.printStackTrace();
//									}
//									
//								}
//								
//								if (name.contains("行政处罚信息")) {
//									
//									try{
//										Elements trs = doc.select("tr");
//										
//										for(Element tr:trs){
//											Elements tds = tr.select("td");
//											
//											if(tds.size() > 5){
//												
//												String all = "";
//												for(Element td:tds){
//												
//													all += td.text();
//												}
//												if(all.equals("")||all.equals("暂无数据")||all.trim().equals("暂无知识产权出质登记信息。")){
//													continue;
//												}
//												
//												List<String> colNames = new ArrayList<String>();
//												List<Object> colValues = new ArrayList<Object>();
//
//												colNames.add("comName");
//												colNames.add("uniqueno");
//												colNames.add("realName");
//												colNames.add("disposerRealName");
//
//												colValues.add(html.get("keyword"));
//												colValues.add(html.get("company_no"));
//												colValues.add(html.get("company_name"));
//												colValues.add(disposerRealName);
//												
//												colNames.add("wenhao");
//												colValues.add(tds.get(1).text());
//												
//												colNames.add("leixing");
//												colValues.add(tds.get(2).text());
//												
//												colNames.add("chufaneirong");
//												colValues.add(tds.get(3).text());
//												
//												colNames.add("juedingjiguan");
//												colValues.add(tds.get(4).text());
//												
//												colNames.add("juedingriqi");
//												colValues.add(tds.get(5).text());
//												
//												try {
//													Paramters.db_local.insert(colNames.toArray(new String[colNames.size()]),
//															colValues.toArray(new Object[colValues.size()]), "comp_xingzhengchufa");
//												} catch (Exception e) {
//
//													if (colNames.size() != colValues.size()) {
//														Paramters.log.info(html.get("company_name").toString() + " 的 comp_xingzhengchufa 参数值大小和列不一样");
//														System.out.println(colNames);
//														System.out.println(colValues);
//													}
//													Paramters.log.info(html.get("company_name").toString() + e.getMessage());
//												}
//											}
//										}
//									}catch(Exception e){
//										e.printStackTrace();
//									}
//								}
//								if (name.contains("抽查检查信息")) {
//									try{
//										Elements trs = tb.select("tr");
//										
//										for(Element tr:trs){
//											Elements tds = tr.select("td");
//											
//											if(tds.size()>3){
//												
//												String all = "";
//												for(Element td:tds){
//												
//													all += td.text();
//												}
//												if(all.equals("")||all.equals("暂无数据")){
//													continue;
//												}
//												
//												List<String> colNames = new ArrayList<String>();
//												List<Object> colValues = new ArrayList<Object>();
//
//												colNames.add("comName");
//												colNames.add("uniqueno");
//												colNames.add("realName");
//												colNames.add("disposerRealName");
//
//												colValues.add(html.get("keyword"));
//												colValues.add(html.get("company_no"));
//												colValues.add(html.get("company_name"));
//												colValues.add(disposerRealName);
//												
//												colNames.add("jianchashishijiguan");
//												colValues.add(tds.get(1).text());
//												
//												colNames.add("leixing");
//												colValues.add(tds.get(2).text());
//												
//												colNames.add("riqi");
//												colValues.add(tds.get(3).text());
//												
//												colNames.add("jieguo");
//												colValues.add(tds.get(4).text());
//												
//												try {
//													db_local2.insert(colNames.toArray(new String[colNames.size()]),
//															colValues.toArray(new Object[colValues.size()]), "comp_chouchajiancha");
//												} catch (Exception e) {
//
//													if (colNames.size() != colValues.size()) {
//														Paramters.log.info(html.get("company_name").toString() + " 的 icbc_check_text 参数值大小和列不一样");
//														System.out.println(colNames);
//														System.out.println(colValues);
//													}
//													Paramters.log.info(html.get("company_name").toString() + e.getMessage());
//												}
//											}
//										}
//									}catch(Exception e){
//										e.printStackTrace();
//									}
//									
//								
//								}
								
								
								
				}
			}
		}
	}

	public static Map<String, Object> relation() {
		Map<String, Object> map = new HashMap<String, Object>();

		List<Map<String, Object>> cols = Paramters.db_local.excuteQuery(
				"select colName,heilongjiang_name from z_comp_info where tbName = ?",
				new String[] { "comp_gongshangxinxi" });
		for (Map<String, Object> col : cols) {
			if (null != col.get("heilongjiang_name")) {
				map.put(col.get("heilongjiang_name").toString(), col.get("colName"));
			}
		}

		return map;
	}
}
