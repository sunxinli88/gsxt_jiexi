package com.gxst.service.jiexi_table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.service.data.Paramters;
import com.gxst.util.ConnectionDB;

public class JiexiJibenxinxi {
	public static void jiexiJibenxinxi(Element tb,Map<String,Object> html,String disposerRealName,Map<String,Object> map,ConnectionDB db_local){
		
		try {
			List<String> colNames = new ArrayList<String>();
			List<Object> colValues = new ArrayList<Object>();

			colNames.add("comName");
			colNames.add("uniqueno");
			colNames.add("realName");
			colNames.add("disposerRealName");

			colValues.add(html.get("keyword"));
			colValues.add(html.get("company_no"));
			colValues.add(html.get("company_name"));
			colValues.add(disposerRealName);

			Elements ths = tb.select("th");
			String startDate = "", endDate = "";
			for (Element th : ths) {
				if (null != th.nextElementSibling()) {
					String title = th.text();

					if (title.equals("营业期限自")) {
						startDate = th.nextElementSibling().text();
					}
					if (title.equals("营业期限至")) {
						endDate = th.nextElementSibling().text();
					}

					if (null == map.get(title)) {
						continue;
					}
					String colName = map.get(title).toString();
					String colValue = th.nextElementSibling().text();
					colNames.add(colName);
					colValues.add(colValue);

				}
			}
			if (!startDate.equals("") || !endDate.equals("")) {
				if(!colNames.contains("yingyeqixian")){
					colNames.add("yingyeqixian");
					colValues.add(startDate + " 至 " + endDate);
				}
				
			}
			System.out.println(colValues);
			try {
				db_local.insert(colNames.toArray(new String[colNames.size()]),
						colValues.toArray(new Object[colValues.size()]), "comp_gongshangxinxi");
			} catch (Exception e) {

				if (colNames.size() != colValues.size()) {
					Paramters.log
							.info(html.get("company_name").toString() + " 的 jibenxinxi 参数值大小和列不一样");
					System.out.println(colNames);
					System.out.println(colValues);
				}
				Paramters.log.info(html.get("company_name").toString() + e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Paramters.log.info(html.get("company_name").toString() + e.getMessage());
		}
	}
}
