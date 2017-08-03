package com.gxst.service.beijing.jiexi;

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

public class JiexiMain {
	private static String tbName = "comp_gongshangxinxi";
	private static Map<String,Object>  map = relation();
	public static void main(String[] args) {
		jiexi();
	}

	public static void jiexi() {

		List<Map<String, Object>> htmls = Paramters.db_local
				.excuteQuery("select MainPage_html,keyword,company_no,company_name from company_beijing", null);

		for (Map<String, Object> html : htmls) {

			if (html.get("MainPage_html") == null) {
				continue;
			}
			String disposerRealName = ToDBC.tool(html.get("company_name").toString());

			String mainHtml = html.get("MainPage_html").toString();
			Document doc = Jsoup.parse(mainHtml);

			Element djxxDiv = doc.select(".detailsList").first();

			Elements titles = djxxDiv.select("tr");

			List<String> colNames = new ArrayList<String>();
			List<String> colValues = new ArrayList<String>();
			
			colNames.add("comName");
			colNames.add("uniqueno");
			colNames.add("realName");
			colNames.add("disposerRealName");
			
			colValues.add(html.get("keyword").toString());
			colValues.add(html.get("company_no").toString());
			colValues.add(html.get("company_name").toString());
			colValues.add(disposerRealName);

			for (Element title : titles) {
				if (title.select(">*").size() % 2 == 0&& title.select(">*").size() != 0) {
					Elements ths = title.select("th");
					for(Element th:ths){
						String htmlName = th.text();
						
						if(null!= map.get(htmlName)){
							String colName = map.get(htmlName).toString();
							String colValue = th.nextElementSibling().text();
							colNames.add(colName);
							colValues.add(colValue);
						}
						
					}
				}
			}
			try {
				Paramters.db_local.insert(colNames.toArray(new String[colNames.size()]),
						colValues.toArray(new String[colValues.size()]), tbName);
			} catch (Exception e) {

				if (colNames.size() != colValues.size()) {
					Paramters.log.info(html.get("company_name").toString() + " 的变更记录参数值大小和列不一样");
					System.out.println(colNames);
					System.out.println(colValues);
				}
				Paramters.log.info(html.get("company_name").toString() + e.getMessage());
			}

		}
	}
	
	public static Map<String,Object> relation(){
		Map<String,Object> map = new HashMap<String,Object>();
		
		List<Map<String, Object>> cols = Paramters.db_local.excuteQuery("select colName,htmlName from z_comp_info where tbName = ?", new String[]{tbName});
		for(Map<String,Object> col:cols){
			if(null!= col.get("htmlName")){
				map.put(col.get("htmlName").toString(), col.get("colName"));
			}
		}
		
		return map;
		
	}
}
