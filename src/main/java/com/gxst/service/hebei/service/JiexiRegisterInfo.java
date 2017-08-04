package com.gxst.service.hebei.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.service.data.Paramters;
import com.gxst.service.jiexi_table.JiexiBiangengxx;
import com.gxst.service.jiexi_table.JiexiGudongxx;
import com.gxst.service.jiexi_table.JiexiJibenxinxi;
import com.gxst.util.ConnectionDB;
import com.gxst.util.ToDBC;

public class JiexiRegisterInfo {
	private static ConnectionDB db_local2 = new ConnectionDB(
			"jdbc:mysql://localhost:3306/db_gsxt?useUnicode=true&characterEncoding=utf8", "root", "123");
	private static Map<String, Object> map = relation();

	public static void main(String[] args) {
		jiexi();
	}

	public static void jiexi() {
		List<Map<String, Object>> htmls = db_local2.excuteQuery(
				"select icbc_register_info_text,keyword,company_no,company_name from company_url_hb", null);

		for (Map<String, Object> html : htmls) {

			if (html.get("icbc_register_info_text") == null) {
				continue;
			}
			String disposerRealName = ToDBC.tool(html.get("company_name").toString());

			String mainHtml = html.get("icbc_register_info_text").toString();
			Document doc = Jsoup.parse(mainHtml);

			Elements tbs = doc.select("table");

			for (Element tb : tbs) {
				if (tb.select("th").size() > 0) {
					String name = tb.select("th").first().text();

					System.out.println(html.get("company_no"));
					if (name.contains("基本信息")) {
						JiexiJibenxinxi.jiexiJibenxinxi(tb, html, disposerRealName, map, db_local2);
					}
					if (name.contains("股东信息")) {
						JiexiGudongxx.jiexiGudongxx(tb, html, disposerRealName, db_local2);
					}
					
					if (name.contains("变更信息")) {
						JiexiBiangengxx.jiexi(tb, html, disposerRealName, db_local2);
					}

				}
			}
		}
	}

	public static Map<String, Object> relation() {
		Map<String, Object> map = new HashMap<String, Object>();

		List<Map<String, Object>> cols = Paramters.db_local.excuteQuery(
				"select colName,hubei_name from z_comp_info where tbName = ?", new String[] { "comp_gongshangxinxi" });
		for (Map<String, Object> col : cols) {
			if (null != col.get("hubei_name")) {
				map.put(col.get("hubei_name").toString(), col.get("colName"));
			}
		}

		return map;
	}
}
