package com.gxst.service.shanghai.info;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.service.data.Paramters;
import com.gxst.util.ConnectionDB;
import com.gxst.util.ToDBC;

public class te {
	private static ConnectionDB db_local2 = new ConnectionDB(
			"jdbc:mysql://localhost:3306/db_gsxt?useUnicode=true&characterEncoding=utf8", "root", "123");

	public static void main(String[] args) {
		getCol();
	}

	public static void getCol() {
		List<Map<String, Object>> htmls = db_local2
				.excuteQuery("select page4_content,keyword,company_no,company_name from company_sh", null);

		for (Map<String, Object> html : htmls) {

			if (html.get("page4_content") == null) {
				continue;
			}
			String disposerRealName = ToDBC.tool(html.get("company_name").toString());

			String mainHtml = html.get("page4_content").toString();
			Document doc = Jsoup.parse(mainHtml);

			Elements tbs = doc.select("table");

			for (Element tb : tbs) {

				if (tb.select("th").size() > 0) {
					String name = tb.select("th").first().text();
					if (name.startsWith("股东信息")) {
						name = "股东信息";
					}
					Elements ths = tb.select("th");

					for (Element th : ths) {

						String title = th.text();
						System.out.println(name+" : "+title);
						
						try {
							db_local2.insert(new String[] { "colName", "tbName" }, new Object[] { title, name },
									"a_col_b");
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
