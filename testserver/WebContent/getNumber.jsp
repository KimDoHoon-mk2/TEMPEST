<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import = "com.db.ConnectDB" %>

<%  
	System.out.println("---getNumber");

   ConnectDB conn = ConnectDB.getInstance();   
   
   request.setCharacterEncoding("euc-kr");
   String name = request.getParameter("name");
   System.out.println("name="+name);

   out.print(conn.getNumber(name));
%>