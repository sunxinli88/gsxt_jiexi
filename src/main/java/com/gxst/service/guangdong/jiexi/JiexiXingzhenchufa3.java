package com.gxst.service.guangdong.jiexi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gxst.service.data.Paramters;
import com.gxst.util.ToDBC;

public class JiexiXingzhenchufa3 {
	public static void main(String[] args) {
		jiexi();
	}

	public static void jiexi() {
		List<Map<String, Object>> htmls = Paramters.db_local.excuteQuery(
				"select otherinfo_pledge_text,keyword,company_no,company_name from company_url_gd", null);
		
		for (Map<String, Object> html : htmls) {

			if (html.get("otherinfo_pledge_text") == null) {
				continue;
			}
			String disposerRealName = ToDBC.tool(html.get("company_name").toString());

			String mainHtml = html.get("otherinfo_pledge_text").toString();
			Document doc = Jsoup.parse(mainHtml);
			try{
				Elements trs = doc.select("tr");
				
				for(Element tr:trs){
					Elements tds = tr.select("td");
					
					if(tds.size() > 4){
						
						String all = "";
						for(Element td:tds){
						
							all += td.text();
						}
						if(all.equals("")||all.equals("暂无数据")||all.trim().equals("暂无知识产权出质登记信息。")){
							continue;
						}
						
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
						
						colNames.add("wenhao");
						colValues.add(tds.get(1).text());
						
						colNames.add("leixing");
						colValues.add(tds.get(2).text());
						
						colNames.add("chufaneirong");
						colValues.add(tds.get(3).text());
						
						colNames.add("juedingjiguan");
						colValues.add(tds.get(4).text());
						
						colNames.add("juedingriqi");
						colValues.add(tds.get(5).text());
						System.out.println(colValues);
//						try {
//							Paramters.db_local.insert(colNames.toArray(new String[colNames.size()]),
//									colValues.toArray(new Object[colValues.size()]), "comp_xingzhengchufa");
//						} catch (Exception e) {
//
//							if (colNames.size() != colValues.size()) {
//								Paramters.log.info(html.get("company_name").toString() + " 的 comp_xingzhengchufa 参数值大小和列不一样");
//								System.out.println(colNames);
//								System.out.println(colValues);
//							}
//							Paramters.log.info(html.get("company_name").toString() + e.getMessage());
//						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
