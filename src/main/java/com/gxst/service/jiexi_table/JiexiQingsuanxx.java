package com.gxst.service.jiexi_table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.service.data.Paramters;
import com.gxst.util.ConnectionDB;

public class JiexiQingsuanxx {
	public static void jiexiQingsuanxx(Element tb,Map<String,Object> html,String disposerRealName,ConnectionDB db_local){
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
			for (Element th : ths) {
				if (null != th.nextElementSibling()) {
					
					if(th.text().trim().equals("清算负责人")){
						colNames.add("qingsuanzufuzeren");
						colValues.add(th.nextElementSibling().text());
					}
					if(th.text().trim().equals("清算负责人")){
						colNames.add("qingsuanzuchengyuan");
						colValues.add(th.nextElementSibling().text());
					}
				}
			}
			System.out.println(colValues);
			try {
				db_local.insert(colNames.toArray(new String[colNames.size()]),
						colValues.toArray(new Object[colValues.size()]), "comp_qingsuanxinxi");
			} catch (Exception e) {

				if (colNames.size() != colValues.size()) {
					Paramters.log.info(html.get("company_name").toString() + " 的 comp_qingsuanxinxi 参数值大小和列不一样");
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
