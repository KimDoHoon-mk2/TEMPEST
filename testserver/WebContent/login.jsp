
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import = "com.db.ConnectDB" %>


<% 
	System.out.println("---login");

   ConnectDB conn = ConnectDB.getInstance();   
   
   String id = request.getParameter("id");
   String pwd = request.getParameter("pwd");
   
   System.out.println("id="+id);
   System.out.println("pwd="+pwd);

   out.print(conn.loginDB(id,pwd));
   
   System.out.println("result="+conn.loginDB(id, pwd));
%>