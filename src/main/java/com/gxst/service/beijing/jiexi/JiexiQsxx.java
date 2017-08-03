package com.gxst.service.beijing.jiexi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.service.data.Paramters;
import com.gxst.util.ToDBC;

public class JiexiQsxx {
	private static String tbName = "comp_qingsuanxinxi";

	public static void main(String[] args) {
		jiexi();
	}

	public static void jiexi() {

		List<Map<String, Object>> htmls = Paramters.db_local
				.excuteQuery("select qsxx_html,keyword,company_no,company_name from company_beijing", null);

		for (Map<String, Object> html : htmls) {

			if (html.get("qsxx_html") == null) {
				continue;
			}
			String disposerRealName = ToDBC.tool(html.get("company_name").toString());

			String mainHtml = html.get("qsxx_html").toString();
			Document doc = Jsoup.parse(mainHtml);

			Element djxxDiv = doc.select(".detailsList").first();

			Elements titles = djxxDiv.select("tr");

			List<String> colNames = new ArrayList<String>();
			colNames.add("comName");
			colNames.add("uniqueno");
			colNames.add("realName");
			colNames.add("disposerRealName");
			
			colNames.add("qingsuanzufuzeren");
			colNames.add("qingsuanzuchengyuan");

			List<String> colValues = new ArrayList<String>();
			colValues.add(html.get("keyword").toString());
			colValues.add(html.get("company_no").toString());
			colValues.add(html.get("company_name").toString());
			colValues.add(disposerRealName);

			String fuzeren = titles.get(1).select("td").text().trim();
			String chengyuan = titles.get(2).select("td").text().trim();

			if (!fuzeren.equals("") && !chengyuan.equals("")) {
				colValues.add(fuzeren);
				colValues.add(chengyuan);
				
				System.out.println(fuzeren+" 清算组成员："+chengyuan);

				try {
					Paramters.db_local.insert(colNames.toArray(new String[colNames.size()]),
							colValues.toArray(new String[colValues.size()]), tbName);
				} catch (Exception e) {

					if (colNames.size() != colValues.size()) {
						Paramters.log.info(html.get("company_name").toString() + " 的变更记录参数值大小和列不一样");
						System.out.println(colNames);
						System.out.println(colValues);
					}
					Paramters.log.info(html.get("company_name").toString() + e.getMessage());
				}
			}

		}
	}
}
