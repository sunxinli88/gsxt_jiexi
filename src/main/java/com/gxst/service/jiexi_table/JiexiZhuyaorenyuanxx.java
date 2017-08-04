package com.gxst.service.jiexi_table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.service.data.Paramters;
import com.gxst.util.ConnectionDB;

public class JiexiZhuyaorenyuanxx {
	public static void jiexiZhuyaorenyuanxx(Element tb,Map<String,Object> html,String disposerRealName,ConnectionDB db_local){
		try {
			Elements trs = tb.select("tr");
			for (Element tr : trs) {
				Elements tds = tr.select("td");
				if(tds.size()>2){
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
					
					colNames.add("mingzi");
					colValues.add(tds.get(1).text());
					
					colNames.add("zhiwei");
					colValues.add(tds.get(2).text());
					System.out.println(colValues);
					try {
						db_local.insert(colNames.toArray(new String[colNames.size()]),
								colValues.toArray(new Object[colValues.size()]), "comp_zhuyaorenyuan");
					} catch (Exception e) {

						if (colNames.size() != colValues.size()) {
							Paramters.log.info(html.get("company_name").toString() + " 的 comp_zhuyaorenyuan 参数值大小和列不一样");
							System.out.println(colNames);
							System.out.println(colValues);
						}
						Paramters.log.info(html.get("company_name").toString() + e.getMessage());
					}
				}
				if(tds.size()>3){
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
					
					colNames.add("mingzi");
					colValues.add(tds.get(4).text());
					
					colNames.add("zhiwei");
					colValues.add(tds.get(5).text());
					
					try {
						Paramters.db_local.insert(colNames.toArray(new String[colNames.size()]),
								colValues.toArray(new Object[colValues.size()]), "comp_zhuyaorenyuan");
					} catch (Exception e) {

						if (colNames.size() != colValues.size()) {
							Paramters.log.info(html.get("company_name").toString() + " 的 comp_zhuyaorenyuan 参数值大小和列不一样");
							System.out.println(colNames);
							System.out.println(colValues);
						}
						Paramters.log.info(html.get("company_name").toString() + e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Paramters.log.info(html.get("company_name").toString() + e.getMessage());
		}
		
		
		
	}
}
