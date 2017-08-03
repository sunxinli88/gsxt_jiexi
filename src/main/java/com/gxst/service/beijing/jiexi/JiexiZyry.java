package com.gxst.service.beijing.jiexi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.service.data.Paramters;
import com.gxst.util.ToDBC;

public class JiexiZyry {
	private static String tbName = "comp_zhuyaorenyuan";

	public static void main(String[] args) {
		jiexi();
	}

	public static void jiexi() {

		List<Map<String, Object>> htmls = Paramters.db_local
				.excuteQuery("select zyry_html,keyword,company_no,company_name from company_beijing", null);

		for (Map<String, Object> html : htmls) {

			if (html.get("zyry_html") == null) {
				continue;
			}
			String disposerRealName = ToDBC.tool(html.get("company_name").toString());

			String mainHtml = html.get("zyry_html").toString();
			Document doc = Jsoup.parse(mainHtml);

			Element djxxDiv = doc.select(".detailsList").first();

			Elements titles = djxxDiv.select("tr");

			if (titles.size() > 2) {
				List<Map<String, Object>> rs = new ArrayList<Map<String, Object>>();
				for (int i = 2; i < titles.size(); i++) {

					Elements contents = titles.get(i).select("td");
					if (contents.size() > 2) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("xingming", contents.get(1).text());
						map.put("zhiwei", contents.get(2).text());
						rs.add(map);
					}

					if (contents.size() > 3) {
						Map<String, Object> map1 = new HashMap<String, Object>();
						map1.put("xingming", contents.get(4).text());
						map1.put("zhiwei", contents.get(5).text());
						rs.add(map1);
					}
				}
				for (Map<String, Object> r : rs) {
					List<String> colNames = new ArrayList<String>();
					colNames.add("comName");
					colNames.add("uniqueno");
					colNames.add("realName");
					colNames.add("disposerRealName");

					colNames.add("mingzi");
					colNames.add("zhiwei");

					List<String> colValues = new ArrayList<String>();
					colValues.add(html.get("keyword").toString());
					colValues.add(html.get("company_no").toString());
					colValues.add(html.get("company_name").toString());
					colValues.add(disposerRealName);
					colValues.add(r.get("xingming").toString());
					colValues.add(r.get("zhiwei").toString());

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
}
