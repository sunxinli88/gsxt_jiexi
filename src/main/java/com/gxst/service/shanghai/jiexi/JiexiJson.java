package com.gxst.service.shanghai.jiexi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gxst.service.data.Paramters;
import com.gxst.util.ConnectionDB;
import com.gxst.util.ToDBC;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JiexiJson {
	public static ConnectionDB db_local3 = new ConnectionDB(
			"jdbc:mysql://localhost:3306/db_gsxt?useUnicode=true&characterEncoding=utf8", "root", "123");

	public static void main(String[] args) {
		jiexi();
	}
	
	public static void jiexi(){
		List<Map<String, Object>> htmls = db_local3
				.excuteQuery("select company_text,keyword,company_no,company_name from companyinfo_sh_text", null);

		for (Map<String, Object> html : htmls) {

			if (html.get("company_text") == null) {
				continue;
			}
			String disposerRealName = ToDBC.tool(html.get("company_name").toString());

			String mainHtml = html.get("company_text").toString();
			
			JSONArray array = JSONArray.fromObject(mainHtml);
			
			for(int i = 0;i<array.size();i++){
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
				
				JSONObject json = JSONObject.fromObject(array.get(i));
					String yingyeqixian = "";
					if(json.get("经营范围:")!=null){
						colNames.add("jingyingfanwei");
						colValues.add(json.get("经营范围:"));
					}
					if(json.get("经营范围及方式:")!=null){
						if(!colNames.contains("jingyingfanwei")){
						colNames.add("jingyingfanwei");
						colValues.add(json.get("经营范围及方式:"));
						}
					}
					
					
					if(json.get("注册资本:")!=null){
						if(!colNames.contains("zhuceziben")){
							colNames.add("zhuceziben");
							colValues.add(json.get("注册资本:"));
						}
					} 
					if(json.get("注册资金:")!=null){
						if(!colNames.contains("zhuceziben")){
							colNames.add("zhuceziben");
							colValues.add(json.get("注册资金:"));
						}
					}
					if(json.get("注册号:")!=null){
						colNames.add("zhucehao");
						colValues.add(json.get("注册号:"));
					}
					
					if(json.get("营业期限:")!=null){
						colNames.add("yingyeqixian");
						colValues.add(json.get("营业期限:"));
					}
					if(json.get("经营期限:")!=null){
						if(!colNames.contains("yingyeqixian")){
						colNames.add("yingyeqixian");
						colValues.add(json.get("经营期限:"));
						}
					}
					
					
					if(json.get("核准日期")!=null){
						colNames.add("hezhunriqi");
						colValues.add(json.get("核准日期"));
					}
					if(json.get("成立日期:")!=null){
						colNames.add("chengliriqi");
						colValues.add(json.get("成立日期:"));
					}
					if(json.get("住所:")!=null){
						if(!colNames.contains("qiyedizhi")){
							colNames.add("qiyedizhi");
							colValues.add(json.get("住所:"));
						}
					}
					if(json.get("地址:")!=null){
						if(!colNames.contains("qiyedizhi")){
							colNames.add("qiyedizhi");
							colValues.add(json.get("地址:"));
						}
					}
					if(json.get("住所:")!=null){
						if(!colNames.contains("qiyedizhi")){
							colNames.add("qiyedizhi");
							colValues.add(json.get("住所:"));
						}
					}
					if(json.get("公司类型:")!=null){
						if(!colNames.contains("gongsileixing")){
							colNames.add("gongsileixing");
							colValues.add(json.get("公司类型:"));
						}
					}
					if(json.get("企业类型:")!=null){
						if(!colNames.contains("gongsileixing")){
							colNames.add("gongsileixing");
							colValues.add(json.get("企业类型:"));
						}
					}
					if(json.get("企业类型")!=null){
						if(!colNames.contains("gongsileixing")){
							colNames.add("gongsileixing");
							colValues.add(json.get("企业类型"));
						}
					}
					
					if(json.get("登记机关:")!=null){
						if(!colNames.contains("dengjijiguan")){
						colNames.add("dengjijiguan");
						colValues.add(json.get("登记机关:"));
						}
					}
					if(json.get("登记机关")!=null){
						if(!colNames.contains("dengjijiguan")){
							colNames.add("dengjijiguan");
							colValues.add(json.get("登记机关"));
						}
						
					}
					
					if(json.get("法定代表人:")!=null){
						colNames.add("fadingdaibiaoren");
						colValues.add(json.get("法定代表人:"));
					}
					if(json.get("法定代表人姓名:")!=null){
						if(!colNames.contains("fadingdaibiaoren")){
						colNames.add("fadingdaibiaoren");
						colValues.add(json.get("法定代表人姓名:"));
						}
					}
					
					if(json.get("负责人:")!=null){
						colNames.add("fuzeren");
						colValues.add(json.get("负责人:"));
					}
					System.out.println(colValues);
					try {
						db_local3.insert(colNames.toArray(new String[colNames.size()]),
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
			}
		}
	}

	public static void getCol() {
		List<Map<String, Object>> htmls = Paramters.db_local
				.excuteQuery("select company_text,keyword,company_no,company_name from companyinfo_sh_text", null);

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
						Paramters.db_local.insert(new String[] { "colName"}, new Object[] { colName }, "a_col_a");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}
	}
}
