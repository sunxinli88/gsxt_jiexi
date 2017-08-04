package com.gxst.service.fujian.jiexi;

import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.service.jiexi_table.JiexiSifagudongbiangengdengji;
import com.gxst.service.jiexi_table.JiexiSifaguquandongjiexx;
import com.gxst.service.jiexi_table.JiexiXingzhengchufaxx;
import com.gxst.util.ConnectionDB;
import com.gxst.util.ToDBC;

public class JiexiHtml06 {
	private static ConnectionDB db_local2 = new ConnectionDB(
			"jdbc:mysql://localhost:3306/db_gsxt?useUnicode=true&characterEncoding=utf8", "root", "123");

	public static void main(String[] args) {
		jiexi();
	}
//,keyword,company_no,company_name
	public static void jiexi() {
		List<Map<String, Object>> htmls = db_local2
				.excuteQuery("select html_tab06_text,keyword,company_no,company_name from company_fujian", null);

		for (Map<String, Object> html : htmls) {

			if (html.get("html_tab06_text") == null) {
				continue;
			}
			String disposerRealName = ToDBC.tool(html.get("company_name").toString());

			String mainHtml = html.get("html_tab06_text").toString();
			Document doc = Jsoup.parse(mainHtml);

			Elements tbs = doc.select("table");

			for (Element tb : tbs) {
				if (tb.select("th").size() > 0) {
					
					String name = tb.select("th").first().text();
					System.out.println(html.get("company_no"));
					if (name.contains("司法股权冻结信息")) {
						JiexiSifaguquandongjiexx.jiexi(tb, html, disposerRealName, db_local2);
					}
					
					if (name.contains("司法股东变更登记信息")) {
						JiexiSifagudongbiangengdengji.jiexi(tb, html, disposerRealName, db_local2);
					}
					
					
				}
			}
		}
	}
				
}
