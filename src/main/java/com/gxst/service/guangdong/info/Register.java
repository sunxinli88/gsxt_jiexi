package com.gxst.service.guangdong.info;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.chainsaw.Main;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.service.data.Paramters;
import com.gxst.util.ConnectionDB;

public class Register {

	public static void main(String[] args) throws SQLException {
		infoBiangeng();
	}
	
	//获取变更信息有哪些列
	public static void infoBiangeng(){
		ConnectionDB db_local2 = new ConnectionDB(
				"jdbc:mysql://localhost:3306/db_gsxt?useUnicode=true&characterEncoding=utf8", "root", "123");
		
		List<Map<String, Object>> htmls = db_local2
				.excuteQuery("select icbc_register_info_text from company_url_gd", null);
		
		for (Map<String, Object> html : htmls) {
			if (html.get("icbc_register_info_text") == null) {
				continue;
			}
			String mainHtml = html.get("icbc_register_info_text").toString();
			Document doc = Jsoup.parse(mainHtml);

			//
			Elements tbs = doc.select("table");

			for (Element tb : tbs) {
				
				if(tb.select("th").size()==0){
					continue;
				}
				String name = tb.select("th").first().text();
				if(name.contains("变更信息")){
					Elements ths = tb.select("th");
					
					for(Element th:ths){
						if(null!=th.nextElementSibling()){
							String title = th.text();
							String content = th.nextElementSibling().text();
							try {
								db_local2.insert(new String[] { "colName","colValue","tbName" }, new Object[] { title,content,name }, "a_col_biangeng");
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
	
	//获取基本信息有哪些列
	public static void jiexi(){
		
		
		ConnectionDB db_local2 = new ConnectionDB(
				"jdbc:mysql://localhost:3306/db_gsxt?useUnicode=true&characterEncoding=utf8", "root", "123");
		
		List<Map<String, Object>> htmls = db_local2
				.excuteQuery("select icbc_register_info_text from company_url_gd", null);
		
		for (Map<String, Object> html : htmls) {
			if (html.get("icbc_register_info_text") == null) {
				continue;
			}
			String mainHtml = html.get("icbc_register_info_text").toString();
			Document doc = Jsoup.parse(mainHtml);

			//
			Elements tbs = doc.select("table");

			for (Element tb : tbs) {
				
				if(tb.select("th").size()==0){
					continue;
				}
				String name = tb.select("th").first().text();
				if(name.contains("企业基本信息")||name.contains("基本信息")){
					Elements ths = tb.select("th");
					
					for(Element th:ths){
						if(null!=th.nextElementSibling()){
							String title = th.text();
							String content = th.nextElementSibling().text();
							try {
								db_local2.insert(new String[] { "colName","colValue","tbName" }, new Object[] { title,content,name }, "a_col_b");
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

	// 企业基本信息
	public static void getBaseCol() {
		List<Map<String, Object>> htmls = Paramters.db_local
				.excuteQuery("select icbc_register_info_text from company_url_gd", null);

		for (Map<String, Object> html : htmls) {
			if (html.get("icbc_register_info_text") == null) {
				continue;
			}
			String mainHtml = html.get("icbc_register_info_text").toString();
			Document doc = Jsoup.parse(mainHtml);

			Elements tbs = doc.select(".detailsList");

			for (Element tb : tbs) {

				String name = tb.select("tr").first().text();

				String n = name.replace("0", "").replace("1", "").replace("2", "").replace("3", "").replace("4", "")
						.replace("5", "").replace("6", "").replace("7", "").replace("8", "").replace("9", "").trim();

				if(n.equals("基本信息")){
					Elements colNames = tb.select("th");
					
					for(Element colName:colNames){
						if(null!=colName.nextElementSibling()){
							String title = colName.text();
							try {
								Paramters.db_local.insert(new String[] { "colName" }, new Object[] { title }, "a_col");
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
	public static void getTb2() throws SQLException {
		List<Map<String, Object>> htmls = Paramters.db_local
				.excuteQuery("select icbc_register_info_text from company_url_gd", null);

		for (Map<String, Object> html : htmls) {
			if (html.get("icbc_register_info_text") == null) {
				continue;
			}
			String mainHtml = html.get("icbc_register_info_text").toString();
			Document doc = Jsoup.parse(mainHtml);

			//
			Elements tbs = doc.select("table");

			for (Element tb : tbs) {
				String name = tb.select("th").first().text();

				String n = name.replace("0", "").replace("1", "").replace("2", "").replace("3", "").replace("4", "")
						.replace("5", "").replace("6", "").replace("7", "").replace("8", "").replace("9", "").trim();
				Paramters.db_local.insert(new String[] { "colName" }, new Object[] { n }, "a_col");
			}
		}
	}
	public static void getTb() throws SQLException {
		List<Map<String, Object>> htmls = Paramters.db_local
				.excuteQuery("select icbc_register_info_text from company_url_gd", null);

		for (Map<String, Object> html : htmls) {
			if (html.get("icbc_register_info_text") == null) {
				continue;
			}
			String mainHtml = html.get("icbc_register_info_text").toString();
			Document doc = Jsoup.parse(mainHtml);

			//
			Elements tbs = doc.select(".detailsList");

			for (Element tb : tbs) {
				String name = tb.select("tr").first().text();

				String n = name.replace("0", "").replace("1", "").replace("2", "").replace("3", "").replace("4", "")
						.replace("5", "").replace("6", "").replace("7", "").replace("8", "").replace("9", "").trim();
				Paramters.db_local.insert(new String[] { "colName" }, new Object[] { n }, "a_col");
			}
		}
	}
	
	public static void copeTb(){
		List<Map<String, Object>> colNames = Paramters.db_local
				.excuteQuery("select id,colName from a_col", null);
		
		for(Map<String, Object> map:colNames){
			String colName = map.get("colName").toString();
			if(colName.contains("基本信息")){
				Paramters.db_local.executeUpdate("delect from a_col where id = "+map.get("id"));
			}
		}

	}
	
	public static void shaixuan(){
		List<Map<String, Object>> colNames = Paramters.db_local
		.excuteQuery("select * from a_col", null);
		
		for(Map<String, Object> colName:colNames){
			String content = colName.get("colName").toString();
			if(content.length()>7){
				String r = "";
				if(content.startsWith("基本信息")||content.startsWith("股东信息")||content.startsWith("变更信息")||content.startsWith("营业期限")){
					r = content.substring(0, 4);
					String sql = "update a_col set colName = '"+r+"'  where id = "+colName.get("id");
					System.out.println(sql);
					Paramters.db_local.executeUpdate(sql);
				}
				if(content.startsWith("投资人信息")||content.startsWith("自然人股东")){
					r = content.substring(0, 5);
					String sql = "update a_col set colName = '"+r+"'  where id = "+colName.get("id");
					System.out.println(sql);
					Paramters.db_local.executeUpdate(sql);
				}
				if(content.startsWith("企业基本信息")){
					r = content.substring(0, 6);
					String sql = "update a_col set colName = '"+r+"'  where id = "+colName.get("id");
					System.out.println(sql);
					Paramters.db_local.executeUpdate(sql);
				}
				if(content.startsWith("出资人及出资信息")){
					r = content.substring(0, 8);
					String sql = "update a_col set colName = '"+r+"'  where id = "+colName.get("id");
					System.out.println(sql);
					Paramters.db_local.executeUpdate(sql);
				}
				if(content.startsWith("法定代表人")){
					r = content.substring(0, 5);
					String sql = "update a_col set colName = '"+r+"'  where id = "+colName.get("id");
					System.out.println(sql);
					Paramters.db_local.executeUpdate(sql);
				}
				
				
			}
			
		}
	}

}
