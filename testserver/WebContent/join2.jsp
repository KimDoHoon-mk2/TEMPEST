<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import = "com.db.ConnectDB" %>

<%  
	System.out.println("---join2");

   ConnectDB conn = ConnectDB.getInstance();   
   
	request.setCharacterEncoding("euc-kr");
	String id = request.getParameter("id");
	String pw = request.getParameter("pw");
   String name = request.getParameter("name");
   String number = request.getParameter("number");
   
   System.out.println("id="+id);
   System.out.println("pw="+pw);
   System.out.println("name="+name);
   System.out.println("number="+number);

   out.print(conn.connectionDB2(id,pw,name,number));
%>