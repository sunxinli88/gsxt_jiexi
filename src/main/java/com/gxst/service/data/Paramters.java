package com.gxst.service.data;

import org.apache.log4j.Logger;

import com.gxst.util.ConnectionDB;

public class Paramters {
	public static ConnectionDB db_local = new ConnectionDB(
			"jdbc:mysql://localhost:3306/db_gsxt?useUnicode=true&characterEncoding=utf8", "root", "123");
	public static Logger log= Logger.getLogger(Paramters.class);

}
