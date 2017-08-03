package com.gxst.service.beijing.info;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.util.ConnectionDB;

public class JiexiZyryHtmlInfo {
	private static ConnectionDB db_local = new ConnectionDB(
			"jdbc:mysql://localhost:3306/db_gsxt?useUnicode=true&characterEncoding=utf8", "root", "123");

	public static void main(String[] args) {
		getCol();
	}
	
	public static void getCol() {
		List<Map<String, Object>> htmls = db_local.excuteQuery("select zyry_html from company_beijing", null);

		for (Map<String, Object> html : htmls) {
			if (html.get("zyry_html") == null) {
				continue;
			}
			String mainHtml = html.get("zyry_html").toString();
			Document doc = Jsoup.parse(mainHtml);
			
			Element djxxDiv = doc.select("#touziren").first();
			
			Elements titles = djxxDiv.select("th");
			
			for(Element title:titles){
				try {
					db_local.insert(new String[] { "colName" }, new Object[] { title.text()}, "a_col");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void getTb() {
		List<Map<String, Object>> htmls = db_local.excuteQuery("select zyry_html from company_beijing", null);

		for (Map<String, Object> html : htmls) {
			if (html.get("zyry_html") == null) {
				continue;
			}
			String mainHtml = html.get("zyry_html").toString();
			Document doc = Jsoup.parse(mainHtml);
			
			
			//
			Elements tbs = doc.select(".detailsList");
			
			for(Element tb:tbs){
				String name = tb.attr("id");
				try {
					db_local.insert(new String[] { "colName" }, new Object[] { name+tbs.size() }, "a_col");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
