<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import = "com.db.ConnectDB" %>

<%  
	System.out.println("---checkList");

    ConnectDB conn = ConnectDB.getInstance();   
   
	String ID = request.getParameter("id");
    String lock_id = request.getParameter("lock_id");
    
    System.out.println("id="+ID);
    System.out.println("lock_id="+lock_id);

    out.print(conn.check_list(ID,lock_id));
%>