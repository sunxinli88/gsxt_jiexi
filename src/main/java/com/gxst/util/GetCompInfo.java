package com.gxst.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetCompInfo {
	private static ConnectionDB db_web = new ConnectionDB(
			"jdbc:mysql://10.50.5.14:3306/db_comdata?useUnicode=true&characterEncoding=utf8", "user_comdata",
			"rfgythjuiknb456tgy");

	private static ConnectionDB db_web1 = new ConnectionDB(
			"jdbc:mysql://localhost:3306/db_gsxt?useUnicode=true&characterEncoding=utf8", "root", "123");

	public static void main(String[] args) {
		//coloName();
//		getTbComment();
		getTbComment();
	}
	
	public static void getTbComment(){
		List<Map<String, Object>> originalTbNames = db_web.excuteQuery("SHOW TABLES", null);
		List<String> tbNames = new ArrayList<String>();
		System.out.println("列出生产库中的所有表格：");
		for (Map<String, Object> tbName : originalTbNames) {
			String tableName = tbName.get("Tables_in_db_comdata").toString();
			if (tableName.startsWith("comp_")) {
				tbNames.add(tableName);
			}
		}
		for (String tbName : tbNames) {
			List<Map<String, Object>> createTbComments = db_web.excuteQuery("show  create  table " + tbName, null);
			String creatStatment = createTbComments.get(0).get("Create Table").toString();
			// System.out.println(creatStatment);
			String[] comments = creatStatment.split(",");
			String tbColomn = "", primaryKey = "";
			for (String comment : comments) {
				if (comment.contains("PRIMARY KEY")) {
					String[] primary = comment.split("`");
					primaryKey = primary[1];
				}
			}
			String comment = comments[comments.length - 1];
			if (comment.contains("COMMENT")) {
				String[] ss = comment.split("COMMENT");
				if (ss.length > 1) {
					if (ss[1].contains("=")) {
						tbColomn = ss[1].replace("=\'", "").replace("\'", "");
					}
				}
			}
			db_web1.executeUpdate("update z_comp_info set tbComment = '" + tbColomn.trim() + "' ,primaryKey = '"
					+ primaryKey.trim() + "' where tbName = '" + tbName.trim() + "'");
		}
	}

	// 获取企查查表格结构的字段名字和注释/主键/，存入到z_qcc_map
	public static void coloName() {

		List<Map<String, Object>> originalTbNames = db_web.excuteQuery("SHOW TABLES", null);
		List<String> tbNames = new ArrayList<String>();
		System.out.println("列出生产库中的所有表格：");
		for (Map<String, Object> tbName : originalTbNames) {
			String tableName = tbName.get("Tables_in_db_comdata").toString();
			if (tableName.startsWith("comp_")) {
				tbNames.add(tableName);
			}
		}
		//comp_gongsijianjie
		for (String tbName : tbNames) {
//			if(!tbName.equals("comp_gongsijianjie")){
//				continue;
//			}
			List<Map<String, Object>> createTbComments = db_web.excuteQuery("show  create  table " + tbName, null);
			String creatStatment = createTbComments.get(0).get("Create Table").toString();
			// System.out.println(creatStatment);
			String[] comments = creatStatment.split(",");
			for (String comment : comments) {

				String colName = "", colComment = "";
				// System.out.println(comment);
				String[] sss = comment.split("`");
				if (sss.length > 1) {
					colName = sss[1];
				}
				String[] ss = comment.split("COMMENT");
				if(colName.trim().equals(tbName)){
					continue;
				}
				if (ss.length > 1) {
					colComment = ss[1].replace("\'", "");
					if (colComment.contains("=")) {
					} else {
						try {
							db_web1.insert(new String[] { "tbName", "colName", "colComment" },
									new String[] { tbName.trim(), colName.trim(), colComment.trim() }, "z_comp_info");
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}else{
					colComment = "";
					if (colComment.contains("=")) {
					} else {
						try {
							db_web1.insert(new String[] { "tbName", "colName", "colComment" },
									new String[] { tbName.trim(), colName.trim(), colComment.trim() }, "z_comp_info");
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
