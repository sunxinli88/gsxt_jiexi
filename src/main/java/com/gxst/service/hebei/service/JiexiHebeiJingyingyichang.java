package com.gxst.service.hebei.service;

import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.service.jiexi_table.JiexiJingyingyichang;
import com.gxst.service.jiexi_table.JiexiXingzhengchufaxx;
import com.gxst.util.ConnectionDB;
import com.gxst.util.ToDBC;

public class JiexiHebeiJingyingyichang {
	private static ConnectionDB db_local = new ConnectionDB(
			"jdbc:mysql://localhost:3306/db_gsxt?useUnicode=true&characterEncoding=utf8", "root", "123");

	public static void main(String[] args) {
		jiexi();
	}

	public static void jiexi() {
		List<Map<String, Object>> htmls = db_local.excuteQuery(
				"select icbc_abnormal_text,keyword,company_no,company_name from company_url_hb", null);

		for (Map<String, Object> html : htmls) {

			if (html.get("icbc_abnormal_text") == null) {
				continue;
			}
			String disposerRealName = ToDBC.tool(html.get("company_name").toString());

			String mainHtml = html.get("icbc_abnormal_text").toString();
			Document doc = Jsoup.parse(mainHtml);

			Elements tbs = doc.select("table");

			for (Element tb : tbs) {
				if (tb.select("th").size() > 0) {
					String name = tb.select("th").first().text();

					System.out.println(html.get("company_no"));
					
					if (name.contains("经营异常信息")) {
						JiexiJingyingyichang.jiexiJingyingyichang(tb, html, disposerRealName, db_local);
					}
					
				}
			}
		}
	}
}
