<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import = "com.db.ConnectDB" %>

<%  
	System.out.println("---closed");

   ConnectDB conn = ConnectDB.getInstance();   
   
   String id = request.getParameter("id");
   String lock_id = request.getParameter("lock_id");
   
   System.out.println("id="+id);
   System.out.println("lock_id="+lock_id);
   
   String check = conn.check_list(id,lock_id);
   
   if(check.equals("0")) out.print(conn.closed(lock_id));
   else out.print(check);
%>