package com.gxst.service.heilongjiang.jiexi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.util.ConnectionDB;
import com.gxst.util.ToDBC;

public class JiexiBiangengxx {
	private static ConnectionDB db_local = new ConnectionDB(
			"jdbc:mysql://localhost:3306/db_gsxt?useUnicode=true&characterEncoding=utf8", "root", "123");

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, InstantiationException, ClassNotFoundException {

		jiexi("html_tab02_text", "company_hlj", "com.gxst.service.jiexi_table.JiexiBiangengxx",
				"变更信息");
	}

	/**
	 * @param 解析对象的字段明细，解析对象的表格名字，调用具体的解析代码，解析的数据项名字
	 * @throws ClassNotFoundException
	 * 
	 * 
	 */
	public static void jiexi(String colName, String tbName, String method, String cName)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, InstantiationException, ClassNotFoundException {
		List<Map<String, Object>> htmls = db_local
				.excuteQuery("select " + colName + ",keyword,company_no,company_name from " + tbName, null);

		for (Map<String, Object> html : htmls) {

			if (html.get(colName) == null) {
				continue;
			}
			String disposerRealName = ToDBC.tool(html.get("company_name").toString());

			String mainHtml = html.get(colName).toString();
			Document doc = Jsoup.parse(mainHtml);

			Elements tbs = doc.select("table");

			Class methdClass = Class.forName(method);
			Method jiexi = methdClass.getDeclaredMethod("jiexi",
					new Class[] { Element.class, Map.class, String.class, ConnectionDB.class });
			Object obj = methdClass.newInstance();
			for (Element tb : tbs) {
				if (tb.select("th").size() > 0) {
					String name = tb.select("th").first().text();

					System.out.println(html.get("company_no"));

					if (name.contains(cName)) {

						// Element tb,Map<String,Object> html,String
						// disposerRealName,ConnectionDB db_local
						
						jiexi.invoke(obj, tb, html, disposerRealName, db_local);
					}

				}
			}
		}
	}
}
