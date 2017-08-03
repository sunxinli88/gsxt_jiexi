package com.gxst.service.guangdong.jiexi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.service.data.Paramters;
import com.gxst.util.ToDBC;

public class JiexiReference {

	public static void main(String[] args) {
		jiexi();
	}
	public static void jiexi() {
		List<Map<String, Object>> htmls = Paramters.db_local.excuteQuery(
				"select icbc_reference_info_text,keyword,company_no,company_name from company_url_gd", null);

		for (Map<String, Object> html : htmls) {

			if (html.get("icbc_reference_info_text") == null) {
				continue;
			}
			String disposerRealName = ToDBC.tool(html.get("company_name").toString());

			String mainHtml = html.get("icbc_reference_info_text").toString();
			Document doc = Jsoup.parse(mainHtml);
			Elements tbs = doc.select("table");

			for (Element tb : tbs) {

				if (tb.select("th").size() == 0) {
					continue;
				}
				String name = tb.select("th").first().text();
				
				// 解析工商信息
				if (name.contains("主要人员信息")) {
					try {
						Elements trs = tb.select("tr");
						for (Element tr : trs) {
							Elements tds = tr.select("td");
							if(tds.size()>2){
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
								
								colNames.add("mingzi");
								colValues.add(tds.get(1).text());
								
								colNames.add("zhiwei");
								colValues.add(tds.get(2).text());
								
								try {
									Paramters.db_local.insert(colNames.toArray(new String[colNames.size()]),
											colValues.toArray(new Object[colValues.size()]), "comp_zhuyaorenyuan");
								} catch (Exception e) {

									if (colNames.size() != colValues.size()) {
										Paramters.log.info(html.get("company_name").toString() + " 的 comp_zhuyaorenyuan 参数值大小和列不一样");
										System.out.println(colNames);
										System.out.println(colValues);
									}
									Paramters.log.info(html.get("company_name").toString() + e.getMessage());
								}
							}
							if(tds.size()>3){
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
								
								colNames.add("mingzi");
								colValues.add(tds.get(4).text());
								
								colNames.add("zhiwei");
								colValues.add(tds.get(5).text());
								
								try {
									Paramters.db_local.insert(colNames.toArray(new String[colNames.size()]),
											colValues.toArray(new Object[colValues.size()]), "comp_zhuyaorenyuan");
								} catch (Exception e) {

									if (colNames.size() != colValues.size()) {
										Paramters.log.info(html.get("company_name").toString() + " 的 comp_zhuyaorenyuan 参数值大小和列不一样");
										System.out.println(colNames);
										System.out.println(colValues);
									}
									Paramters.log.info(html.get("company_name").toString() + e.getMessage());
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						Paramters.log.info(html.get("company_name").toString() + e.getMessage());
					}
				}
				// 解析工商信息
				if (name.contains("分支机构信息")) {
					try {
						Elements trs = tb.select("tr");
						for (Element tr : trs) {
							Elements tds = tr.select("td");
							if(tds.size()>2){
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
								
								colNames.add("mingzi");
								colValues.add(tds.get(2).text());
								
								try {
									Paramters.db_local.insert(colNames.toArray(new String[colNames.size()]),
											colValues.toArray(new Object[colValues.size()]), "comp_fenzhijigou");
								} catch (Exception e) {

									if (colNames.size() != colValues.size()) {
										Paramters.log.info(html.get("company_name").toString() + " 的 comp_fenzhijigou 参数值大小和列不一样");
										System.out.println(colNames);
										System.out.println(colValues);
									}
									Paramters.log.info(html.get("company_name").toString() + e.getMessage());
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						Paramters.log.info(html.get("company_name").toString() + e.getMessage());
					}
				}
				// 解析清算信息
				if (name.contains("清算信息")) {
					try {
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
						Elements ths = tb.select("th");
						for (Element th : ths) {
							if (null != th.nextElementSibling()) {
								
								if(th.text().trim().equals("清算负责人")){
									colNames.add("qingsuanzufuzeren");
									colValues.add(th.nextElementSibling().text());
								}
								if(th.text().trim().equals("清算负责人")){
									colNames.add("qingsuanzuchengyuan");
									colValues.add(th.nextElementSibling().text());
								}
							}
						}
						try {
							Paramters.db_local.insert(colNames.toArray(new String[colNames.size()]),
									colValues.toArray(new Object[colValues.size()]), "comp_qingsuanxinxi");
						} catch (Exception e) {

							if (colNames.size() != colValues.size()) {
								Paramters.log.info(html.get("company_name").toString() + " 的 comp_qingsuanxinxi 参数值大小和列不一样");
								System.out.println(colNames);
								System.out.println(colValues);
							}
							Paramters.log.info(html.get("company_name").toString() + e.getMessage());
						}
					} catch (Exception e) {
						e.printStackTrace();
						Paramters.log.info(html.get("company_name").toString() + e.getMessage());
					}
				}
			}
		}
	}
}
