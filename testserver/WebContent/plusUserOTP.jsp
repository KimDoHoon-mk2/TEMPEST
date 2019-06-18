<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import = "com.db.ConnectDB" %>

<%  
	System.out.println("---plusUserOTP");

   ConnectDB conn = ConnectDB.getInstance();   
   
   String id = request.getParameter("id");
   String otp_num = request.getParameter("otp");
   String lock_id = request.getParameter("lock_id");
   String newid = request.getParameter("newid");
   String time = request.getParameter("time");
   
   System.out.println("id="+id);
   System.out.println("otp_num="+otp_num);
   System.out.println("lock_id="+lock_id);
   System.out.println("newid="+newid);
   System.out.println("time="+time);
   
   out.print(conn.plusUserOTP(otp_num,id,lock_id,newid,time));  //  otp인증시  0 리턴
%>