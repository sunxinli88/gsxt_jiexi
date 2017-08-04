package com.gxst.service.jiexi_table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.service.data.Paramters;
import com.gxst.util.ConnectionDB;

public class JiexiBiangengxx {
	public static void jiexi(Element tb,Map<String,Object> html,String disposerRealName,ConnectionDB db_local){
		try {
			Elements trs = tb.select("tr");
			for (Element tr : trs) {

				Elements tds = tr.select("td");

				if (tds.size() > 0) {
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

					colNames.add("biangengxiangmu");
					colValues.add(tds.get(0).text());
					if (tds.size() > 1) {
						colNames.add("biangengqian");
						colValues.add(tds.get(2).text());
					}
					if (tds.size() > 2) {
						colNames.add("biangenghou");
						colValues.add(tds.get(3).text());
					}
					if (tds.size() > 3) {
						colNames.add("biangengriqi");
						colValues.add(tds.get(1).text());
					}

					System.out.println(colValues);
					try {
						db_local.insert(colNames.toArray(new String[colNames.size()]),
								colValues.toArray(new Object[colValues.size()]), "comp_biangengjilu");
					} catch (Exception e) {

						if (colNames.size() != colValues.size()) {
							Paramters.log.info(
									html.get("company_name").toString() + " 的 biangengjilu 参数值大小和列不一样");
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
