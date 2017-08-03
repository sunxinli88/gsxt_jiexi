package com.gxst.service.beijing.info;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.service.data.Paramters;

public class JiexiQsxxHtmlInfo {
	public static void main(String[] args) {
		getTb();
	}
	public static void getTb() {
		List<Map<String, Object>> htmls = Paramters.db_local.excuteQuery("select qsxx_html from company_beijing", null);

		for (Map<String, Object> html : htmls) {
			if (html.get("qsxx_html") == null) {
				continue;
			}
			String mainHtml = html.get("qsxx_html").toString();
			Document doc = Jsoup.parse(mainHtml);
			
			
			//
			Elements tbs = doc.select(".detailsList");
			
			String content = doc.text();
			
			try {
				Paramters.db_local.insert(new String[] { "colName" }, new Object[] { tbs.size()+content}, "a_col");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
