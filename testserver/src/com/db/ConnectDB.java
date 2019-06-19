package com.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ConnectDB {
    private static ConnectDB instance = new ConnectDB();

    public static ConnectDB getInstance() {  
        return instance;
    }
    public ConnectDB() {  }

    String jdbcUrl = "jdbc:mysql://localhost:3306/jsptest?serverTimezone=UTC";
    String userId = "root";
    String userPw = "root";
    String driver = "com.mysql.cj.jdbc.Driver";

    Connection conn = null;
    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
    PreparedStatement pstmt3 = null;
    PreparedStatement pstmt4 = null;
    ResultSet rs = null;
    ResultSet rs2 = null;
    ResultSet rs3 = null;


    String sql = "";
    String sql2 = "";
    String sql3 = "";
    String sql4 = "";
    String returns = "error";
    
    public String lockerState(String id) {
             
       try {
          Class.forName(driver);
          conn = DriverManager.getConnection(jdbcUrl,userId,userPw);    
          sql = "SELECT state,status FROM locker WHERE lock_id=?";
          pstmt = conn.prepareStatement(sql);
          pstmt.setString(1,id);
          rs = pstmt.executeQuery();
          
          if(rs.next())
          {
        	  int state = Integer.parseInt(rs.getString("state"));
        	  int status = Integer.parseInt(rs.getString("status"));
        	  
        	  return String.valueOf(state+status*2);
          }
       }catch(Exception e) 
       {
         e.printStackTrace();
       }
       return "DBfail";
      
    }
    
    public String checkState(){
        
        try {
           Class.forName(driver);
           conn = DriverManager.getConnection(jdbcUrl,userId,userPw);    
           sql = "select lock_id, status from locker";
           pstmt = conn.prepareStatement(sql);
           rs = pstmt.executeQuery();

           JSONObject jsonObject = new JSONObject();
           JSONArray resultArray = new JSONArray();
           
           
           while(rs.next())
           {
                
                JSONObject todoInfo = new JSONObject();
                todoInfo.put("lock_id", rs.getString("locker_id"));
                todoInfo.put("status", rs.getString("status"));
                
                resultArray.add(todoInfo);
                
           }
           String jsonInfo = resultArray.toJSONString();
              System.out.println(jsonInfo);
       
           return jsonInfo;
        }
           catch(Exception e) 
          {
            e.printStackTrace();
          }
          return "DBfail";
 }
   
    public String finishIoT(String ID, String lock_id) {
    	try {
            Class.forName(driver);
            conn = DriverManager.getConnection(jdbcUrl,userId,userPw);
            sql = "SELECT * FROM auth_list WHERE mem_id = ? and lock_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,ID);
            pstmt.setString(2,lock_id);

            rs = pstmt.executeQuery();
            if(rs.next())
            {
          	  sql2 = "DELETE FROM auth_list WHERE mem_id = ? and lock_id = ?";
          	  pstmt2 = conn.prepareStatement(sql2);
          	  pstmt2.setString(1,ID);
          	  pstmt2.setString(2,lock_id);
              pstmt2.executeUpdate();
          
          	  sql3 = "SELECT * FROM auth_list WHERE lock_id = ?";
          	  pstmt3 = conn.prepareStatement(sql3);
          	  pstmt3.setString(1, lock_id);
          	  rs2 = pstmt3.executeQuery();
          	  if(rs2.next()) {
          	  }
          	  else {
              	  sql4 = "UPDATE locker SET status = 0 WHERE lock_id = ?";
              	  pstmt4 = conn.prepareStatement(sql4);
              	  pstmt4.setString(1, lock_id);
              	  pstmt4.executeUpdate();
          	  }
              return "0";
            }
           else
              return "1";
         }catch(Exception e) {
           e.printStackTrace();
         }finally {
             if (pstmt4 != null)try {pstmt4.close();    } catch (SQLException ex) {}
             if (pstmt3 != null)try {pstmt3.close();    } catch (SQLException ex) {}
             if (pstmt2 != null)try {pstmt2.close();    } catch (SQLException ex) {}
             if (pstmt != null)try {pstmt.close();	} catch (SQLException ex) {}
             if (conn != null)try {conn.close();    } catch (SQLException ex) {}
         }
         return "2";
      }
   
    public String authOTP(String OTP,String ID, String lock_id, String num, String type) {  //아이디나 락커 번호 추가 필요함
       try {
          Class.forName(driver);
          conn = DriverManager.getConnection(jdbcUrl,userId,userPw);
          sql = "SELECT * FROM otp WHERE otp_num = ? and mem_id = ?";
          pstmt = conn.prepareStatement(sql);
          pstmt.setString(1,OTP);
          pstmt.setString(2,ID);

          rs = pstmt.executeQuery();
          if(rs.next())
          {
        	  sql2 = "UPDATE locker SET status = ? WHERE lock_id = ?";
        	  pstmt2 = conn.prepareStatement(sql2);
        	  pstmt2.setString(1,"1");
        	  pstmt2.setString(2,lock_id);
              pstmt2.executeUpdate();
        
        	  sql3 = "INSERT INTO auth_list VALUES(?,?,?)";
        	  pstmt3 = conn.prepareStatement(sql3);
        	  pstmt3.setString(1,ID);
        	  pstmt3.setString(2, lock_id);
        	  LocalDateTime now = LocalDateTime.now().plusHours(9);
        	  LocalDateTime newDateTime;
        	  switch(type) {
        	  case "0":
        		  newDateTime = now.plusSeconds(Integer.parseInt(num));
        		  break;
        	  case "1":
        		  newDateTime = now.plusMinutes(Integer.parseInt(num));
        		  break;
        	  case "2":
        		  newDateTime = now.plusHours(Integer.parseInt(num));
        		  break;
        	  case "3":
        		  newDateTime = now.plusDays(Integer.parseInt(num));
        		  break;
        	  default:
        		  return "2";
        	  }
        	  java.sql.Timestamp date = Timestamp.valueOf(newDateTime);
        	  pstmt3.setTimestamp(3, date);
        	  pstmt3.executeUpdate();
        	  
        	  sql4 = "DELETE FROM otp WHERE mem_id = ?";
        	  pstmt4 = conn.prepareStatement(sql4);
        	  pstmt4.setString(1, ID);
        	  pstmt4.executeUpdate();
              
              return "0";
          }
         else
            return "1"; 
       }catch(Exception e) {
         e.printStackTrace();
       }finally {
           if (pstmt4 != null)try {pstmt4.close();    } catch (SQLException ex) {}
           if (pstmt3 != null)try {pstmt3.close();    } catch (SQLException ex) {}
           if (pstmt2 != null)try {pstmt2.close();    } catch (SQLException ex) {}
           if (pstmt != null)try {pstmt.close();	} catch (SQLException ex) {}
           if (conn != null)try {conn.close();    } catch (SQLException ex) {}
       }
       return "2";
    }
    
    public String openOTP(String OTP,String ID, String lock_id) {
        try {
           Class.forName(driver);
           conn = DriverManager.getConnection(jdbcUrl,userId,userPw);
           sql = "SELECT * FROM otp WHERE otp_num = ? and mem_id = ?";
           pstmt = conn.prepareStatement(sql);
           pstmt.setString(1,OTP);
           pstmt.setString(2,ID);

           rs = pstmt.executeQuery();
           if(rs.next())
           {
         	  sql2 = "DELETE FROM otp WHERE mem_id = ?";
         	  pstmt2 = conn.prepareStatement(sql2);
         	  pstmt2.setString(1, ID);
         	  pstmt2.executeUpdate();
         	  
         	  sql3 = "UPDATE locker SET state = 1,time = CURRENT_TIMESTAMP WHERE lock_id = ?";
        	  pstmt3 = conn.prepareStatement(sql3);
        	  pstmt3.setString(1, lock_id);
        	  pstmt3.executeUpdate();
         	  
               return "0";
           }
          else
             return "1";  
        }catch(Exception e) {
          e.printStackTrace();
        }finally {
            if (pstmt3 != null)try {pstmt3.close();    } catch (SQLException ex) {}
            if (pstmt2 != null)try {pstmt2.close();    } catch (SQLException ex) {}
            if (pstmt != null)try {pstmt.close();	} catch (SQLException ex) {}
            if (conn != null)try {conn.close();    } catch (SQLException ex) {}
        }
        return "2"; 
     }
    
    public String closed(String lock_id) {
    	try {
            Class.forName(driver);
            conn = DriverManager.getConnection(jdbcUrl,userId,userPw);
            sql = "UPDATE locker SET state = 0 WHERE lock_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,lock_id);
        	pstmt.executeUpdate();
            
            return "0";
         }catch(Exception e) {
           e.printStackTrace();
         }finally {
             if (pstmt4 != null)try {pstmt4.close();    } catch (SQLException ex) {}
             if (pstmt3 != null)try {pstmt3.close();    } catch (SQLException ex) {}
             if (pstmt2 != null)try {pstmt2.close();    } catch (SQLException ex) {}
             if (pstmt != null)try {pstmt.close();	} catch (SQLException ex) {}
             if (conn != null)try {conn.close();    } catch (SQLException ex) {}
         }
         return "2";
    }
    
    public String plusUserOTP(String OTP,String ID, String lock_id, String newid, String time) {
        try {
           Class.forName(driver);
           conn = DriverManager.getConnection(jdbcUrl,userId,userPw);
           sql = "SELECT * FROM otp WHERE otp_num = ? and mem_id = ?";
           pstmt = conn.prepareStatement(sql);
           pstmt.setString(1,OTP);
           pstmt.setString(2,ID);

           rs = pstmt.executeQuery();
           if(rs.next())
           {
         	  sql2 = "DELETE FROM otp WHERE mem_id = ?";
         	  pstmt2 = conn.prepareStatement(sql2);
         	  pstmt2.setString(1, ID);
         	  pstmt2.executeUpdate();
         	  
         	  sql3 = "INSERT INTO auth_list VALUES(?,?,?)";
        	  pstmt3 = conn.prepareStatement(sql3);
        	  pstmt3.setString(1, newid);
        	  pstmt3.setString(2, lock_id);
        	  pstmt3.setString(3, time);
        	  pstmt3.executeUpdate();
         	  
               return "0"; 
           }
          else
             return "1";   
        }catch(Exception e) {
          e.printStackTrace();
        }finally {
            if (pstmt3 != null)try {pstmt3.close();    } catch (SQLException ex) {}
            if (pstmt2 != null)try {pstmt2.close();    } catch (SQLException ex) {}
            if (pstmt != null)try {pstmt.close();	} catch (SQLException ex) {}
            if (conn != null)try {conn.close();    } catch (SQLException ex) {}
        }
        return "2";
     }
    
    public String plusTimeOTP(String OTP,String ID, String lock_id, String num, String type) {
        try {
           Class.forName(driver);
           conn = DriverManager.getConnection(jdbcUrl,userId,userPw);
           sql = "SELECT * FROM otp WHERE otp_num = ? and mem_id = ?";
           pstmt = conn.prepareStatement(sql);
           pstmt.setString(1,OTP);
           pstmt.setString(2,ID);
           rs = pstmt.executeQuery();
           
           if(rs.next())
           {
         	  sql2 = "DELETE FROM otp WHERE mem_id = ?";
         	  pstmt2 = conn.prepareStatement(sql2);
         	  pstmt2.setString(1, ID);
         	  pstmt2.executeUpdate();
         	  
         	  sql3 = "SELECT due_date FROM auth_list WHERE mem_id = ? and lock_id = ?";
        	  pstmt3 = conn.prepareStatement(sql3);
         	  pstmt3.setString(1, ID);
        	  pstmt3.setString(2, lock_id);
              rs2 = pstmt3.executeQuery();
        	  if(rs2.next())
              {
        		  java.sql.Timestamp old_time = rs2.getTimestamp("due_date");
        		  LocalDateTime old_time_ = old_time.toLocalDateTime();
        		  LocalDateTime new_time_;
        		  switch(type) {
            	  case "0":
            		  new_time_ = old_time_.plusSeconds(Integer.parseInt(num));
            		  break;
            	  case "1":
            		  new_time_ = old_time_.plusMinutes(Integer.parseInt(num));
            		  break;
            	  case "2":
            		  new_time_ = old_time_.plusHours(Integer.parseInt(num));
            		  break;
            	  case "3":
            		  new_time_ = old_time_.plusDays(Integer.parseInt(num));
            		  break;
            	  default:
            		  return "2";
            	  }
            	  java.sql.Timestamp new_time = Timestamp.valueOf(new_time_);
            	  sql4 = "UPDATE auth_list SET due_date = ? WHERE lock_id = ?";
            	  pstmt4 = conn.prepareStatement(sql4);
            	  pstmt4.setTimestamp(1, new_time);
            	  pstmt4.setString(2, lock_id);
            	  pstmt4.executeUpdate();
                  return "0"; 
              }else {
            	  return "2";
              }
         	  
           }
          else
             return "1";   
        }catch(Exception e) {
          e.printStackTrace();
        }finally {
            if (pstmt3 != null)try {pstmt3.close();    } catch (SQLException ex) {}
            if (pstmt2 != null)try {pstmt2.close();    } catch (SQLException ex) {}
            if (pstmt != null)try {pstmt.close();	} catch (SQLException ex) {}
            if (conn != null)try {conn.close();    } catch (SQLException ex) {}
        }
        return "2";
     }
    
    public String check_list(String ID, String lock_id) {
        try {
           Class.forName(driver);
           conn = DriverManager.getConnection(jdbcUrl,userId,userPw);
           sql = "SELECT * FROM auth_list WHERE lock_id = ? and mem_id = ?";
           pstmt = conn.prepareStatement(sql);
           pstmt.setString(1,lock_id);
           pstmt.setString(2,ID);

           rs = pstmt.executeQuery();
           if(rs.next())
           {
               return "0";
           }
          else
             return "1";   
        }catch(Exception e) {
          e.printStackTrace();
        }finally {
            if (pstmt3 != null)try {pstmt3.close();    } catch (SQLException ex) {}
            if (pstmt2 != null)try {pstmt2.close();    } catch (SQLException ex) {}
            if (pstmt != null)try {pstmt.close();	} catch (SQLException ex) {}
            if (conn != null)try {conn.close();    } catch (SQLException ex) {}
        }
        return "2"; 
     }
    
    public String storeOTP(String OTP,String mem_id,String lock_id) { 
       try {
          Class.forName(driver);
          conn = DriverManager.getConnection(jdbcUrl,userId,userPw);
          sql = "SELECT * FROM otp WHERE mem_id = ?";
          pstmt = conn.prepareStatement(sql);
          pstmt.setString(1,mem_id);

          rs = pstmt.executeQuery();
          if(rs.next())
          {
        	  sql2 = "UPDATE otp SET otp_num = ? WHERE mem_id = ?";
              pstmt2 = conn.prepareStatement(sql2);
              pstmt2.setString(1, OTP);
              pstmt2.setString(2, mem_id);
              pstmt2.executeUpdate();
          }
          else {
        	  sql2 = "INSERT INTO otp(mem_id,lock_id,otp_num) VALUES(?,?,?)";
              pstmt2 = conn.prepareStatement(sql2);
              pstmt2.setString(1, mem_id);
              pstmt2.setString(2, lock_id);
              pstmt2.setString(3, OTP);
              pstmt2.executeUpdate();
          }
       }catch(Exception e) 
       {
         e.printStackTrace(); 
       }finally {
           if (pstmt3 != null)try {pstmt3.close();    } catch (SQLException ex) {}
           if (pstmt2 != null)try {pstmt2.close();    } catch (SQLException ex) {}
           if (pstmt != null)try {pstmt.close();	} catch (SQLException ex) {}
           if (conn != null)try {conn.close();    } catch (SQLException ex) {}
       }
       return returns;
    }

    public String connectionDB(String id, String pwd) 
    {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(jdbcUrl, userId, userPw);

            sql = "SELECT ID FROM people WHERE ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return "1";
            } else {
                return "0";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pstmt2 != null)try {pstmt2.close();    } catch (SQLException ex) {}
            if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
            if (conn != null)try {conn.close();    } catch (SQLException ex) {    }
        }
        return "2";
    }
    
    public String connectionDB2(String id, String pw, String name, String number) 
    {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(jdbcUrl, userId, userPw);

            sql = "SELECT name FROM people WHERE name = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return "1";
            } else {
                sql2 = "INSERT INTO people VALUES(?,?,?,?)";
                pstmt2 = conn.prepareStatement(sql2);
                pstmt2.setString(1, id);
                pstmt2.setString(2, pw);
                pstmt2.setString(3, name);
                pstmt2.setString(4, number);
                
                pstmt2.executeUpdate();
                
                return "0";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pstmt2 != null)try {pstmt2.close();    } catch (SQLException ex) {}
            if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
            if (conn != null)try {conn.close();    } catch (SQLException ex) {    }
        }
        return "2";
    }
    
    public String loginDB(String id, String pwd) {
      try {
         
           Class.forName(driver);
           conn = DriverManager.getConnection(jdbcUrl, userId, userPw);
         sql = "SELECT * FROM people WHERE ID = ?";
         
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, id);
         rs = pstmt.executeQuery();
         
         if(rs.next()) 
         {
            if(rs.getString(2).equals(pwd)) 
            {
            	JSONObject jobj = new JSONObject();
            	ResultSetMetaData rmd = rs.getMetaData();
            	
                for(int i=1;i<=rmd.getColumnCount();i++) {
                	jobj.put(rmd.getColumnName(i),rs.getString(rmd.getColumnName(i)));
                }
            	
               return jobj.toString();
            }
            else {
            	return "2";
            }
         }
         else 
         { 
            return "1";
         }
      }catch(Exception e) {
         e.printStackTrace();
      }
      return "3"; 
   }
    
    public String getAddress(String address) {
    	try {
            Class.forName(driver);
            conn = DriverManager.getConnection(jdbcUrl, userId, userPw);

            sql = "SELECT * FROM locker WHERE address = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,address);
            rs = pstmt.executeQuery();
            
            JSONArray jary = new JSONArray();
         
            while(rs.next()){
            	JSONObject jobj = new JSONObject();
            	ResultSetMetaData rmd = rs.getMetaData();
            	
                for(int i=1;i<=rmd.getColumnCount();i++) {
                	jobj.put(rmd.getColumnName(i),rs.getString(rmd.getColumnName(i)));
                }
                jary.add(jobj);
            }
           
            return jary.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pstmt2 != null)try {pstmt2.close();} catch (SQLException ex) {}
            if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
            if (conn != null)try {conn.close();} catch (SQLException ex) {}
        }
    	return returns;
    }
    
    public String getNumber(String name) {
    	try {
            Class.forName(driver);
            conn = DriverManager.getConnection(jdbcUrl, userId, userPw);

            sql = "SELECT * FROM people WHERE name = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,name);
            rs = pstmt.executeQuery();
            
            if(rs.next()) {
            	JSONObject jobj = new JSONObject();
                ResultSetMetaData rmd = rs.getMetaData();
                	
                for(int i=1;i<=rmd.getColumnCount();i++) {
                   	jobj.put(rmd.getColumnName(i),rs.getString(rmd.getColumnName(i)));
                }
                          
                return jobj.toString();
            }
            else {
            	return "1";
            }     
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pstmt2 != null)try {pstmt2.close();} catch (SQLException ex) {}
            if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
            if (conn != null)try {conn.close();} catch (SQLException ex) {}
        }
    	return returns;
    }
    
    public String getIoT(String ID) {
    	try {
            Class.forName(driver);
            conn = DriverManager.getConnection(jdbcUrl, userId, userPw);

            sql = "SELECT auth_list.*, locker.name FROM auth_list LEFT OUTER JOIN locker ON auth_list.lock_id = locker.lock_id WHERE mem_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,ID);
            rs = pstmt.executeQuery();
            
            JSONArray jary = new JSONArray();
         
            while(rs.next()){
            	JSONObject jobj = new JSONObject();
            	ResultSetMetaData rmd = rs.getMetaData();
            	
                for(int i=1;i<=rmd.getColumnCount();i++) {
                	jobj.put(rmd.getColumnName(i),rs.getString(rmd.getColumnName(i)));
                }
                jary.add(jobj);
            }
           
            return jary.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pstmt2 != null)try {pstmt2.close();} catch (SQLException ex) {}
            if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
            if (conn != null)try {conn.close();} catch (SQLException ex) {}
        }
    	return returns;
    }
    
    public String getUser(String lock_id) {
    	try {
            Class.forName(driver);
            conn = DriverManager.getConnection(jdbcUrl, userId, userPw);

            sql = "SELECT auth_list.*, people.name FROM auth_list LEFT OUTER JOIN people ON auth_list.mem_id = people.ID WHERE lock_id = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,lock_id);
            rs = pstmt.executeQuery();
            
            JSONArray jary = new JSONArray();
         
            while(rs.next()){
            	JSONObject jobj = new JSONObject();
            	ResultSetMetaData rmd = rs.getMetaData();
            	
                for(int i=1;i<=rmd.getColumnCount();i++) {
                	jobj.put(rmd.getColumnName(i),rs.getString(rmd.getColumnName(i)));
                }
                jary.add(jobj);
            }
           
            return jary.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pstmt2 != null)try {pstmt2.close();} catch (SQLException ex) {}
            if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
            if (conn != null)try {conn.close();} catch (SQLException ex) {}
        }
    	return returns;
    }
    
    public String plusUser(String ID, String lock_id, String date) {
    	try {
            Class.forName(driver);
            conn = DriverManager.getConnection(jdbcUrl, userId, userPw);
            
            sql = "SELECT * FROM people WHERE ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,ID);
            rs = pstmt.executeQuery();
         
            if(rs.next()){
            	sql2 = "SELECT * FROM auth_list WHERE mem_id = ? and lock_id = ?";
                pstmt2 = conn.prepareStatement(sql2);
                pstmt2.setString(1,ID);
                pstmt2.setString(2,lock_id);
                rs2 = pstmt2.executeQuery();
                
                if(rs2.next()) {
                	return "1";
                }
                else{
                	sql3 = "INSERT INTO auth_list VALUES (?,?,?)";
                    pstmt3 = conn.prepareStatement(sql3);
                    pstmt3.setString(1,ID);
                    pstmt3.setString(2,lock_id);
                    pstmt3.setString(3,date);
                    pstmt3.executeUpdate();
                    return "0";
                }
            }
            else {
            	return "2";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pstmt3 != null)try {pstmt3.close();} catch (SQLException ex) {}
            if (pstmt2 != null)try {pstmt2.close();} catch (SQLException ex) {}
            if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
            if (conn != null)try {conn.close();} catch (SQLException ex) {}
        }
    	return returns;
    }
}