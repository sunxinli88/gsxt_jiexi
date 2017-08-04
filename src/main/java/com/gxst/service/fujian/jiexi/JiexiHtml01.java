package com.gxst.service.fujian.jiexi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.service.data.Paramters;
import com.gxst.service.jiexi_table.JiexiBiangengxx;
import com.gxst.service.jiexi_table.JiexiChouchajiancha;
import com.gxst.service.jiexi_table.JiexiDongchandiyadjxx;
import com.gxst.service.jiexi_table.JiexiGudongxx;
import com.gxst.service.jiexi_table.JiexiGuquanchuzhixx;
import com.gxst.service.jiexi_table.JiexiJibenxinxi;
import com.gxst.service.jiexi_table.JiexiJingyingyichang;
import com.gxst.service.jiexi_table.JiexiQingsuanxx;
import com.gxst.service.jiexi_table.JiexiXingzhengchufaxx;
import com.gxst.service.jiexi_table.JiexiZhuyaorenyuanxx;
import com.gxst.util.ConnectionDB;
import com.gxst.util.ToDBC;

public class JiexiHtml01 {
	private static ConnectionDB db_local2 = new ConnectionDB(
			"jdbc:mysql://localhost:3306/db_gsxt?useUnicode=true&characterEncoding=utf8", "root", "123");
	private static Map<String, Object> map = relation();

	public static void main(String[] args) {
		jiexi();
	}

	public static void jiexi() {
		List<Map<String, Object>> htmls = db_local2
				.excuteQuery("select html_tab01_text,keyword,company_no,company_name from company_fujian", null);

		for (Map<String, Object> html : htmls) {

			if (html.get("html_tab01_text") == null) {
				continue;
			}
			String disposerRealName = ToDBC.tool(html.get("company_name").toString());

			String mainHtml = html.get("html_tab01_text").toString();
			Document doc = Jsoup.parse(mainHtml);

			Elements tbs = doc.select("table");

			for (Element tb : tbs) {
				if (tb.select("th").size() > 0) {
					String name = tb.select("th").first().text();

					// if (name.contains("经营异常信息")) {
					// JiexiJingyingyichang.jiexiJingyingyichang(tb, html,
					// disposerRealName,db_local2);
					// }
					// if (name.contains("主要人员信息")) {
					// JiexiZhuyaorenyuanxx.jiexiZhuyaorenyuanxx(tb, html,
					// disposerRealName,db_local2);
					// }
					// if (name.contains("行政处罚信息")) {
					// JiexiXingzhengchufaxx.jiexiXingzhengchufaxx(tb, html,
					// disposerRealName,db_local2);
					// }

					// if (name.contains("股权出质登记信息")) {//无有效数据
					// if(tb.select("td").size()> 6){
					// System.out.println(tb);
					// }
					// System.out.println(tb);
					//
					// JiexiGuquanchuzhixx.jiexiGuquanchuzhixx(tb, html,
					// disposerRealName);
					// }
					// if (name.contains("抽查检查信息")) {
					// JiexiChouchajiancha.jiexiGuquanchuzhixx(tb, html,
					// disposerRealName,db_local2);
					// }
					//
					// if (name.contains("动产抵押登记信息")) {
					// JiexiDongchandiyadjxx.jiexiGuquanchuzhixx(tb, html,
					// disposerRealName,db_local2);
					// }

					// if (name.contains("分支机构信息")) {//无有效数据
					// if (tb.select("td").size() > 2) {
					// System.out.println(tb);
					// }
					// System.out.println(tb);
					//
					// }
					if (name.contains("股东信息")) {// 无有效数据

						// Element tb2 = tb.nextElementSibling();
						if (tb.select("td").size() > 2) {
							JiexiGudongxx.jiexiGudongxx(tb, html, disposerRealName, db_local2);
							System.out.println(tb);
						}
					}
					// if (name.contains("变更信息")) {
					// JiexiBiangengxx.jiexi(tb, html,
					// disposerRealName,db_local2);
					// }
					// if (name.contains("清算信息")) {
					// JiexiQingsuanxx.jiexiQingsuanxx(tb, html,
					// disposerRealName,db_local2);
					// }
					// System.out.println(html.get("company_no"));
					// if (name.contains("基本信息")) {
					// JiexiJibenxinxi.jiexiJibenxinxi(tb, html,
					// disposerRealName,map, db_local2);
					// }
				}
			}
		}
	}

	public static Map<String, Object> relation() {
		Map<String, Object> map = new HashMap<String, Object>();

		List<Map<String, Object>> cols = Paramters.db_local.excuteQuery(
				"select colName,fujian_name from z_comp_info where tbName = ?", new String[] { "comp_gongshangxinxi" });
		for (Map<String, Object> col : cols) {
			if (null != col.get("fujian_name")) {
				map.put(col.get("fujian_name").toString(), col.get("colName"));
			}
		}

		return map;
	}
}
