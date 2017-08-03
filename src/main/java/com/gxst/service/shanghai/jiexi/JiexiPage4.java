package com.gxst.service.shanghai.jiexi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.service.data.Paramters;
import com.gxst.util.ToDBC;

public class JiexiPage4 {
	public static void main(String[] args) {
		jiexi();
	}
	
	public static void jiexi() {
		List<Map<String, Object>> htmls = Paramters.db_local
				.excuteQuery("select page4_content,keyword,company_no,company_name from company_sh", null);

		for (Map<String, Object> html : htmls) {

			if (html.get("page4_content") == null) {
				continue;
			}
			String disposerRealName = ToDBC.tool(html.get("company_name").toString());

			String mainHtml = html.get("page4_content").toString();
			Document doc = Jsoup.parse(mainHtml);

			Elements tbs = doc.select("table");

			for (Element tb : tbs) {
				if (tb.select("th").size() > 0) {
					String name = tb.select("th").first().text();
					
					if(name.contains("司法股东变更登记信息")){
						try{
							Elements trs = tb.select("tr");
							
							for(Element tr:trs){
								Elements tds = tr.select("td");
								
								if(tds.size() > 6){
									
									String all = "";
									for(Element td:tds){
									
										all += td.text();
									}
									if(all.equals("")||all.equals("暂无数据")||all.trim().equals("暂无知识产权出质登记信息。")){
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
									
									colNames.add("beizhixingren");
									colValues.add(tds.get(1).text());
									
									colNames.add("guquanshue");
									colValues.add(tds.get(2).text());
									
									colNames.add("shourangren");
									colValues.add(tds.get(3).text());
									
									colNames.add("zhixingfayuan");
									colValues.add(tds.get(4).text());
									
									colNames.add("xiangqing");
									colValues.add(tds.get(5).text());
									
									System.out.println(colValues);
									try {
										Paramters.db_local.insert(colNames.toArray(new String[colNames.size()]),
												colValues.toArray(new Object[colValues.size()]), "comp_sfgdbgdjxx");
									} catch (Exception e) {

										if (colNames.size() != colValues.size()) {
											Paramters.log.info(html.get("company_name").toString() + " 的 comp_sfgdbgdjxx 参数值大小和列不一样");
											System.out.println(colNames);
											System.out.println(colValues);
										}
										Paramters.log.info(html.get("company_name").toString() + e.getMessage());
									}
								}
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
					
					if(name.contains("司法股权冻结信息")){
						
						try{
							Elements trs = tb.select("tr");
							
							for(Element tr:trs){
								Elements tds = tr.select("td");
								
								if(tds.size() > 6){
									
									String all = "";
									for(Element td:tds){
									
										all += td.text();
									}
									if(all.equals("")||all.equals("暂无数据")||all.trim().equals("暂无知识产权出质登记信息。")){
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
									
									colNames.add("beizhixingren");
									colValues.add(tds.get(1).text());
									
									colNames.add("guquanshue");
									colValues.add(tds.get(2).text());
									
									colNames.add("zhixingfayuan");
									colValues.add(tds.get(3).text());
									
									colNames.add("xzgstzswh");
									colValues.add(tds.get(4).text());
									
									colNames.add("zhuangtai");
									colValues.add(tds.get(5).text());
									
									colNames.add("xiangqing");
									colValues.add(tds.get(6).text());
									System.out.println(colValues);
									
									try {
										Paramters.db_local.insert(colNames.toArray(new String[colNames.size()]),
												colValues.toArray(new Object[colValues.size()]), "comp_sfgqdjxx");
									} catch (Exception e) {

										if (colNames.size() != colValues.size()) {
											Paramters.log.info(html.get("company_name").toString() + " 的 comp_xingzhengchufa 参数值大小和列不一样");
											System.out.println(colNames);
											System.out.println(colValues);
										}
										Paramters.log.info(html.get("company_name").toString() + e.getMessage());
									}
								}
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
					
					
				}
			}
		}
	}
}
