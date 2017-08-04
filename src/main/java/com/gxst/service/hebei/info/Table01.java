package com.gxst.service.hebei.info;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.util.ConnectionDB;

public class Table01 {
	private static ConnectionDB db_local2 = new ConnectionDB(
			"jdbc:mysql://localhost:3306/db_gsxt?useUnicode=true&characterEncoding=utf8", "root", "123");
	public static void main(String[] args) {
			getTb();
		
	}
	private static void getTb() {
		List<Map<String, Object>> htmls = db_local2
				.excuteQuery("select icbc_reference_info_text from company_url_hb", null);

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
					Elements ths = tb.select("th");
					for(Element th:ths){
						try {
							db_local2.insert(new String[] { "colName","tbName" }, new Object[] { th.text(),name }, "a_col_b");
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
