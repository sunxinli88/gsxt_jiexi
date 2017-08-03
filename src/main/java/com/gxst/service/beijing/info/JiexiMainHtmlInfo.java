package com.gxst.service.beijing.info;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.util.ConnectionDB;

public class JiexiMainHtmlInfo {
	private static ConnectionDB db_local = new ConnectionDB(
			"jdbc:mysql://localhost:3306/db_gsxt?useUnicode=true&characterEncoding=utf8", "root", "123");

	public static void main(String[] args) {
		try {
			getCol();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void mainPage() {
		List<Map<String, Object>> htmls = db_local.excuteQuery("select MainPage_html from company_beijing", null);

		for (Map<String, Object> html : htmls) {
			String mainHtml = html.get("MainPage_html").toString();
		}
	}

	public static void getCol() throws SQLException {
		List<Map<String, Object>> htmls = db_local.excuteQuery("select MainPage_html from company_beijing", null);

		for (Map<String, Object> html : htmls) {
			if (html.get("MainPage_html") == null) {
				continue;
			}
			String mainHtml = html.get("MainPage_html").toString();
			Document doc = Jsoup.parse(mainHtml);
			
			Element djxxDiv = doc.select("div #djxxDiv").first();
			
			Elements titles = djxxDiv.select("th");
			
			for(Element title:titles){
				db_local.insert(new String[] { "colName" }, new Object[] { title.text()}, "a_col");
			}
		}
	}

	public static void getTb() throws SQLException {
		List<Map<String, Object>> htmls = db_local.excuteQuery("select MainPage_html from company_beijing where id > 5000", null);

		for (Map<String, Object> html : htmls) {
			if (html.get("MainPage_html") == null) {
				continue;
			}
			String mainHtml = html.get("MainPage_html").toString();
			Document doc = Jsoup.parse(mainHtml);

			String djxxDiv = doc.select("div #djxxDiv").text();
			if (!djxxDiv.equals("")) {
				db_local.insert(new String[] { "colName" }, new Object[] { "djxxDiv" }, "a_col");
			}

			String bgxxDiv = doc.select("div #bgxxDiv").text();
			if (!bgxxDiv.equals("")) {
				db_local.insert(new String[] { "colName" }, new Object[] { "bgxxDiv" }, "a_col");
			}

			String dcdyDiv = doc.select("div #dcdyDiv").text();
			if (!dcdyDiv.equals("")) {
				db_local.insert(new String[] { "colName" }, new Object[] { "dcdyDiv" }, "a_col");
			}

			String gqczdjDiv = doc.select("div #gqczdjDiv").text();
			if (!gqczdjDiv.equals("")) {
				db_local.insert(new String[] { "colName" }, new Object[] { "gqczdjDiv" }, "a_col");
			}

			String xzcfDiv = doc.select("div #xzcfDiv").text();
			if (!xzcfDiv.equals("")) {
				db_local.insert(new String[] { "colName" }, new Object[] { "xzcfDiv" }, "a_col");
			}

			String jyycDiv = doc.select("div #jyycDiv").text();
			if (!jyycDiv.equals("")) {
				db_local.insert(new String[] { "colName" }, new Object[] { "jyycDiv" }, "a_col");
			}

			String yzwfDiv = doc.select("div #yzwfDiv").text();
			if (!yzwfDiv.equals("")) {
				db_local.insert(new String[] { "colName" }, new Object[] { "yzwfDiv" }, "a_col");
			}

			String ccycDiv = doc.select("div #ccycDiv").text();
			if (!ccycDiv.equals("")) {
				db_local.insert(new String[] { "colName" }, new Object[] { "ccycDiv" }, "a_col");
			}
		}
	}
}
