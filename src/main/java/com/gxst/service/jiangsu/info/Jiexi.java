package com.gxst.service.jiangsu.info;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gxst.service.data.Paramters;
import com.gxst.util.ConnectionDB;
import com.gxst.util.ToDBC;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Jiexi {
	public static ConnectionDB db_local3 = new ConnectionDB(
			"jdbc:mysql://localhost:3306/db_gsxt?useUnicode=true&characterEncoding=utf8", "root", "123");

	public static void main(String[] args) {
		getCol();
	}

	public static void getCol() {
		List<Map<String, Object>> htmls = Paramters.db_local
				.excuteQuery("select company_text,keyword,company_no,company_name from company_jiangsu_text_v", null);

		for (Map<String, Object> html : htmls) {

			if (html.get("company_text") == null) {
				continue;
			}
			String disposerRealName = ToDBC.tool(html.get("company_name").toString());

			String mainHtml = html.get("company_text").toString();
			
			JSONArray array = JSONArray.fromObject(mainHtml);
			
			for(int i = 0;i<array.size();i++){
				JSONObject jo = JSONObject.fromObject(array.get(i));

				Iterator<String> it = jo.keys();

				while (it.hasNext()) {
					String colName = it.next().toString();
					
					try {
						Paramters.db_local.insert(new String[] { "colName","tbName" }, new Object[] { colName ,i}, "a_col_b");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}
	}
}
