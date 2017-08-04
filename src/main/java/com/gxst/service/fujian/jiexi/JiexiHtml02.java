package com.gxst.service.fujian.jiexi;

import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.service.jiexi_table.JiexiBiangengxx;
import com.gxst.service.jiexi_table.JiexiGuquanbiangengxinxi;
import com.gxst.service.jiexi_table.JiexiJingyingyichang;
import com.gxst.service.jiexi_table.JiexiXingzhengchufaxx;
import com.gxst.service.jiexi_table.JiexiXingzhengxuke;
import com.gxst.service.jiexi_table.JiexiZhishichanquanchuzhidjxx;
import com.gxst.util.ConnectionDB;
import com.gxst.util.ToDBC;

public class JiexiHtml02 {

	private static ConnectionDB db_local2 = new ConnectionDB(
			"jdbc:mysql://localhost:3306/db_gsxt?useUnicode=true&characterEncoding=utf8", "root", "123");

	public static void main(String[] args) {
		jiexi();
	}
//,keyword,company_no,company_name
	public static void jiexi() {
		List<Map<String, Object>> htmls = db_local2
				.excuteQuery("select html_tab02_text,keyword,company_no,company_name from company_fujian", null);

		for (Map<String, Object> html : htmls) {

			if (html.get("html_tab02_text") == null) {
				continue;
			}
			String disposerRealName = ToDBC.tool(html.get("company_name").toString());

			String mainHtml = html.get("html_tab02_text").toString();
			Document doc = Jsoup.parse(mainHtml);

			Elements tbs = doc.select("table");

			for (Element tb : tbs) {
				if (tb.select("th").size() > 0) {
					String name = tb.select("th").first().text();

//					if (name.contains("知识产权出质登记信息")) {
//						JiexiZhishichanquanchuzhidjxx.jiexi(tb, html, disposerRealName, db_local2);
//					}
//					
//					if (name.contains("行政处罚信息")) {
//						JiexiXingzhengchufaxx.jiexi(tb, html, disposerRealName, db_local2);
//					}
//					
//					if (name.contains("行政许可信息")) {
//						JiexiXingzhengxuke.jiexi(tb, html, disposerRealName, db_local2);
//					}
//					
//					if (name.contains("股权变更信息")) {
//						JiexiGuquanbiangengxinxi.jiexi(tb, html, disposerRealName, db_local2);
//					}
					if (name.contains("变更信息")) {
						JiexiBiangengxx.jiexi(tb, html, disposerRealName, db_local2);

					}
//					if (name.contains("股东及出资信息")) {// 无有效数据
//						if (tb.select("td").size() > 3) {
//							System.out.println(tb);
//						}
//						 System.out.println(tb);
//
//					}

				}
			}
		}
	}
}
