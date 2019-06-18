package com.example.kim.tempest;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServerTrans extends AsyncTask<Void, Void, String> {

    String ip = "10.60.23.233:8080";
    String ip2 = "192.168.255.135:8080";
    String code="";
    String send = "";
    String receive="";

    @Override
    protected String doInBackground(Void... voids) {

        try{
            String str;
            URL url = new URL("http://"+ip2+"/testserver/"+code+"");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");

            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(),"EUC-KR");
            osw.write(send);
            osw.flush();

            if(conn.getResponseCode()==conn.HTTP_OK){
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(),"UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();

                while((str = reader.readLine()) != null){
                    buffer.append(str);
                }
                receive = buffer.toString();
                reader.close();
            }
            else{
                Log.i("통신결과",conn.getResponseCode()+"에러");
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return receive;
    }

    public void newID(String ID, String PW){
        this.send = "id="+ID+"&pwd="+PW+"";
        this.code = "join.jsp";
    }

    public void newInfo(String ID, String NAME, String NUMBER){
        this.send = "id="+ID+"&name="+NAME+"&number="+NUMBER+"";
        this.code = "join2.jsp";
    }

    public void login(String ID, String PW){
        this.send = "id="+ID+"&pwd="+PW+"";
        this.code = "login.jsp";
    }

    public void getOTP(String ID, String NAME){
        this.send = "mem_id="+ID+"&lock_id="+NAME+"";
        this.code = "OTP.jsp";
    }

    public void getOTP2(String ID, String NAME){
        this.send = "mem_id="+ID+"&lock_id="+NAME+"";
        this.code = "OTP2.jsp";
    }

    public void authOTP(String ID, String OTP, String LOCK_ID, String NUM, String TYPE){
        this.send = "id="+ID+"&otp="+OTP+"&lock_id="+LOCK_ID+"&num="+NUM+"&type="+TYPE+"";
        this.code = "authOTP.jsp";
    }

    public void openOTP(String ID, String OTP, String LOCK_ID){
        this.send = "id="+ID+"&otp="+OTP+"&lock_id="+LOCK_ID+"";
        this.code = "openOTP.jsp";
    }

    public void closed(String ID, String LOCK_ID){
        this.send = "id="+ID+"&lock_id="+LOCK_ID+"";
        this.code = "closed.jsp";
    }

    public void plusUserOTP(String ID, String OTP, String LOCK_ID, String NEWID, String TIME){
        this.send = "id="+ID+"&otp="+OTP+"&lock_id="+LOCK_ID+"&newid="+NEWID+"&time="+TIME+"";
        this.code = "plusUserOTP.jsp";
    }

    public void plusTimeOTP(String ID, String OTP, String LOCK_ID, String NUM, String TYPE){
        this.send = "id="+ID+"&otp="+OTP+"&lock_id="+LOCK_ID+"&num="+NUM+"&type="+TYPE+"";
        this.code = "plusTimeOTP.jsp";
    }

    public void finishIoT(String ID, String LOCK_ID){
        this.send = "id="+ID+"&lock_id="+LOCK_ID+"";
        this.code = "finishIoT.jsp";
    }

    public void checkList(String ID, String LOCK_ID){
        this.send = "id="+ID+"&lock_id="+LOCK_ID+"";
        this.code = "checkList.jsp";
    }

    public void plusUser(String ID, String LOCK_ID, String DATE){
        this.send = "id="+ID+"&lock_id="+LOCK_ID+"&date="+DATE+"";
        this.code = "plusUser.jsp";
    }

    public void getItem(){
        this.send = "";
        this.code = "Item.jsp";
    }

    public void getNumber_byName(String NAME){
        this.send = "name="+NAME+"";
        this.code = "getNumber.jsp";
    }

    public void getIoT_byAddress(String address){
        this.send = "address="+address+"";
        this.code = "getAddress.jsp";
    }

    public void getIoT_byUser(String ID){
        this.send = "ID="+ID+"";
        this.code = "getIoT_byUser.jsp";
    }

    public void getUser_byIoT(String lock_id){
        this.send = "lock_id="+lock_id+"";
        this.code = "getUser_byIoT.jsp";
    }

    public void getTime_byIoT(String ID){
        this.send = "ID="+ID+"";
        this.code = "OTP.jsp";
    }
}
