package com.elcareates.legacy.std;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConnDB {

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private static final String CLASS_FORNAME = "com.mariadb.jdbc.Driver";               
	//private static final String JDBC_URL = "jdbc:mariadb://localhost:3306/springbook?&characterEncoding=UTF-";
	private static final String JDBC_URL = "jdbc:mariadb://localhost:3306/springbook";
	private static final String JDBC_USER = "root";
	private static final String JDBC_PASS = "root00";
	   
	public static Connection getConnection() throws Exception {
	      
		Connection conn = null;
		Class.forName(CLASS_FORNAME);
		conn = DriverManager.getConnection( JDBC_URL, JDBC_USER, JDBC_PASS);
		System.out.println(" ConnProperty.getConnection() conn : " + conn);
		return conn;
	   } 
}