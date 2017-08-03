package com.gxst.service.beijing.info;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.service.data.Paramters;

public class JiexiFzjgHtmlInfo {
	public static void main(String[] args) {
		getCol();
	}
	
	public static void getCol() {
		List<Map<String, Object>> htmls = Paramters.db_local.excuteQuery("select fzjg_html from company_beijing", null);

		for (Map<String, Object> html : htmls) {
			if (html.get("fzjg_html") == null) {
				continue;
			}
			String mainHtml = html.get("fzjg_html").toString();
			Document doc = Jsoup.parse(mainHtml);
			
			Element djxxDiv = doc.select("#touziren").first();
			
			Elements titles = djxxDiv.select("th");
			
			for(Element title:titles){
				try {
					Paramters.db_local.insert(new String[] { "colName" }, new Object[] { title.text()}, "a_col");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void getTb() throws SQLException {
		List<Map<String, Object>> htmls = Paramters.db_local.excuteQuery("select fzjg_html from company_beijing", null);

		for (Map<String, Object> html : htmls) {
			if (html.get("fzjg_html") == null) {
				continue;
			}
			String mainHtml = html.get("fzjg_html").toString();
			Document doc = Jsoup.parse(mainHtml);
			
			
			//
			Elements tbs = doc.select(".detailsList");
			
			for(Element tb:tbs){
				String name = tb.attr("id");
				Paramters.db_local.insert(new String[] { "colName" }, new Object[] { name+tbs.size() }, "a_col");
			}
		}
	}
}
