package com.gxst.service.guangdong.jiexi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.service.data.Paramters;
import com.gxst.util.ToDBC;

public class JiexiRegister {
	private static String tbName = "comp_gongshangxinxi";
	private static Map<String, Object> map = relation();

	public static void main(String[] args) throws SQLException {
		trueJiexi();
	}

	public static void trueJiexi() {
 		List<Map<String, Object>> htmls = Paramters.db_local.excuteQuery(
				"select icbc_register_info_text,keyword,company_no,company_name from company_url_gd", null);

		for (Map<String, Object> html : htmls) {

			if (html.get("icbc_register_info_text") == null) {
				continue;
			}
			String disposerRealName = ToDBC.tool(html.get("company_name").toString());

			String mainHtml = html.get("icbc_register_info_text").toString();
			Document doc = Jsoup.parse(mainHtml);
			Elements tbs = doc.select("table");

			for (Element tb : tbs) {

				if (tb.select("th").size() == 0) {
					continue;
				}
				String name = tb.select("th").first().text();

				// 解析工商信息
				if (name.contains("企业基本信息") || name.contains("基本信息")) {
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
						String startDate = "", endDate = "";
						for (Element th : ths) {
							if (null != th.nextElementSibling()) {
								String title = th.text();

								if (title.equals("营业期限自")) {
									startDate = th.nextElementSibling().text();
								}
								if (title.equals("营业期限至")) {
									endDate = th.nextElementSibling().text();
								}
								
								if(null ==map.get(title)){
									continue;
								}
								String colName = map.get(title).toString();
								String colValue = th.nextElementSibling().text();
								colNames.add(colName);
								colValues.add(colValue);

							}
						}
						if (!startDate.equals("") || !endDate.equals("")) {
							colNames.add("yingyeqixian");
							colValues.add(startDate + " 至 " + endDate);
						}

						try {
							Paramters.db_local.insert(colNames.toArray(new String[colNames.size()]),
									colValues.toArray(new Object[colValues.size()]), tbName);
						} catch (Exception e) {

							if (colNames.size() != colValues.size()) {
								Paramters.log.info(html.get("company_name").toString() + " 的 jibenxinxi 参数值大小和列不一样");
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
				// 解析变更记录
				if (name.contains("变更信息")) {
					try{
						Elements trs = tb.select("tr");
						for (Element tr : trs) {

							Elements tds = tr.select("td");

							if (tds.size() > 0) {
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

								if (tds.size() > 0) {
									colNames.add("xuhao");
									colValues.add(tds.get(0).text());
								}
								if (tds.size() > 1) {
									colNames.add("biangengxiangmu");
									colValues.add(tds.get(1).text());
								}
								if (tds.size() > 2) {
									colNames.add("biangengqian");
									colValues.add(tds.get(2).text());
								}
								if (tds.size() > 3) {
									colNames.add("biangenghou");
									colValues.add(tds.get(3).text());
								}
								if (tds.size() > 4) {
									colNames.add("biangengriqi");
									colValues.add(tds.get(4).text());
								}

								try {
									Paramters.db_local.insert(colNames.toArray(new String[colNames.size()]),
											colValues.toArray(new Object[colValues.size()]), "comp_biangengjilu");
								} catch (Exception e) {

									if (colNames.size() != colValues.size()) {
										Paramters.log
												.info(html.get("company_name").toString() + " 的 biangengjilu 参数值大小和列不一样");
										System.out.println(colNames);
										System.out.println(colValues);
									}
									Paramters.log.info(html.get("company_name").toString() + e.getMessage());
								}
							}
						}
					}catch(Exception e){
						e.printStackTrace();
						Paramters.log.info(html.get("company_name").toString() + e.getMessage());
					}
				}
				// 解析股东信息
				if (name.contains("股东信息")) {
					try{
						if (doc.select("#invInfo").size()>0) {
							Element infos = doc.select("#invInfo").first();
							Elements trs = infos.select("tr");

							for (Element tr : trs) {
								Elements tds = tr.select("td");

								if (tds.size() == 0) {
									continue;
								}
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

								colNames.add("gudongleixing");
								colValues.add(tds.get(0).text());

								if (tds.size() > 1) {
									colNames.add("gudongmingzi");
									colValues.add(tds.get(1).text());
								}

								try {
									Paramters.db_local.insert(colNames.toArray(new String[colNames.size()]),
											colValues.toArray(new Object[colValues.size()]), "comp_gudongxinxi");
								} catch (Exception e) {

									if (colNames.size() != colValues.size()) {
										Paramters.log
												.info(html.get("company_name").toString() + " 的 gudongxinxi 参数值大小和列不一样");
										System.out.println(colNames);
										System.out.println(colValues);
									}
									Paramters.log.info(html.get("company_name").toString() + e.getMessage());
								}
							}
						}
					}catch(Exception e){
						e.printStackTrace();
						Paramters.log.info(html.get("company_name").toString() + e.fillInStackTrace());
					}
				}

				// 解析 投资人信息---> 融资信息
				if (name.contains("投资人信息")) {
					try{
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

								colNames.add("touzifang");
								colValues.add(tds.get(1).text());

								try {
									Paramters.db_local.insert(colNames.toArray(new String[colNames.size()]),
											colValues.toArray(new Object[colValues.size()]), "comp_rongzixinxi");
								} catch (Exception e) {

									if (colNames.size() != colValues.size()) {
										Paramters.log
												.info(html.get("company_name").toString() + " 的 gudongxinxi 参数值大小和列不一样");
										System.out.println(colNames);
										System.out.println(colValues);
									}
									Paramters.log.info(html.get("company_name").toString() + e.getMessage());
								}
							}
						}
					}catch(Exception e){
						e.printStackTrace();
						Paramters.log.info(html.get("company_name").toString() + e.getMessage());
					}
				}
			}
		}
	}

	public static Map<String, Object> relation() {
		Map<String, Object> map = new HashMap<String, Object>();

		List<Map<String, Object>> cols = Paramters.db_local.excuteQuery(
				"select colName,guangdong_name from z_comp_info where tbName = ?", new String[] { tbName });
		for (Map<String, Object> col : cols) {
			if (null != col.get("guangdong_name")) {
				map.put(col.get("guangdong_name").toString(), col.get("colName"));
			}
		}

		return map;

	}

	public static void jiexi() throws SQLException {
		List<Map<String, Object>> htmls = Paramters.db_local
				.excuteQuery("select icbc_register_info_text from company_url_gd", null);

		for (Map<String, Object> html : htmls) {
			if (html.get("icbc_register_info_text") == null) {
				continue;
			}
			String mainHtml = html.get("icbc_register_info_text").toString();
			Document doc = Jsoup.parse(mainHtml);

			//
			Elements tbs = doc.select("table");

			for (Element tb : tbs) {

				if (tb.select("th").size() == 0) {
					continue;
				}
				String name = tb.select("th").first().text();

				String content = name.replace("0", "").replace("1", "").replace("2", "").replace("3", "")
						.replace("4", "").replace("5", "").replace("6", "").replace("7", "").replace("8", "")
						.replace("9", "").trim();

				if (content.startsWith("基本信息") || content.startsWith("股东信息") || content.startsWith("变更信息")
						|| content.startsWith("营业期限")) {
					content = content.substring(0, 4);
				}
				if (content.startsWith("投资人信息") || content.startsWith("自然人股东")) {
					content = content.substring(0, 5);
				}
				if (content.startsWith("企业基本信息")) {
					content = content.substring(0, 6);

				}
				if (content.startsWith("出资人及出资信息")) {
					content = content.substring(0, 8);

				}
				if (content.startsWith("法定代表人")) {
					content = content.substring(0, 5);

				}
				Paramters.db_local.insert(new String[] { "colName" }, new Object[] { content }, "a_col_a");
			}
		}
	}

}
