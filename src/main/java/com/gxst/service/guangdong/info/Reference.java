package com.gxst.service.guangdong.info;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.service.data.Paramters;
import com.gxst.util.ConnectionDB;

public class Reference {
	private static ConnectionDB db_local2 = new ConnectionDB(
			"jdbc:mysql://localhost:3306/db_gsxt?useUnicode=true&characterEncoding=utf8", "root", "123");
	public static void main(String[] args) {
			infoZhuyaorenyuan();
		
	}
	//获取变更信息有哪些列
	public static void infoZhuyaorenyuan(){
		
		List<Map<String, Object>> htmls = db_local2
				.excuteQuery("select icbc_reference_info_text from company_url_gd", null);
		
		for (Map<String, Object> html : htmls) {
			if (html.get("icbc_reference_info_text") == null) {
				continue;
			}
			String mainHtml = html.get("icbc_reference_info_text").toString();
			Document doc = Jsoup.parse(mainHtml);

			//
			Elements tbs = doc.select("table");

			for (Element tb : tbs) {
				
				if(tb.select("th").size()==0){
					continue;
				}
				String name = tb.select("th").first().text();
				if(name.contains("主要人员信息")||name.contains("分支机构信息")||name.contains("清算信息")){
					Elements ths = tb.select("th");
					
					for(Element th:ths){
						if(null!=th.nextElementSibling()){
							String title = th.text();
							String content = th.nextElementSibling().text();
							try {
								db_local2.insert(new String[] { "colName","colValue","tbName" }, new Object[] { title,content,name }, "a_col_biangeng");
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
	
	public static void getTb() throws SQLException {
		List<Map<String, Object>> htmls = Paramters.db_local
				.excuteQuery("select icbc_reference_info_text from company_url_gd", null);

		for (Map<String, Object> html : htmls) {
			if (html.get("icbc_reference_info_text") == null) {
				continue;
			}
			String mainHtml = html.get("icbc_reference_info_text").toString();
			Document doc = Jsoup.parse(mainHtml);

			//
			Elements tbs = doc.select("table");

			for (Element tb : tbs) {
				if(tb.select("th").size()>0){
					String name = tb.select("th").first().text();
					
					db_local2.insert(new String[] { "colName" }, new Object[] { name }, "a_col");
				}
				
			}
		}
	}
	
}
