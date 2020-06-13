/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.model;

import cse.maven_webmail.control.CommandType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author miso
 */
public class getSentMessage {
    private final String downloadTempDir = "C:/temp/download/";
    Log log = LogFactory.getLog(getSentMessage.class);
    
    int number = 0;
    String fromAddress = null;
    String toAddress = null;//받은 사람! 수신자!
    String ccAddress = null;//참조자!
    String subject = null;
    String body = null;
    String saveDate = null;
    String filename = null; 
     
    public String getSentMessagedetail(int num) throws SQLException, IOException{
        //String id = userid;
        number = num;
        Boolean status;
        log.info(number);
        StringBuilder buffer = new StringBuilder();

        //MessageParser parser = new MessageParser(message, userid);
        //parser.parse(true);
        
        status = connectToDB2();

        buffer.append("보낸 사람: " + fromAddress + " <br>");
        buffer.append("받은 사람: " + toAddress + " <br>");
        buffer.append("Cc &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; : " + ccAddress + " <br>");
        buffer.append("보낸 날짜: " + saveDate + " <br>");
        buffer.append("제 &nbsp;&nbsp;&nbsp;  목: " + subject + " <br> <hr>");

        body = body.replaceAll("\r\n","<br>");
        buffer.append(body);

        String attachedFile = filename;
        if (attachedFile != null) {
            buffer.append("<br> <hr> 첨부파일: <a href=ReadMail.do?menu="
                    + CommandType.DOWNLOAD_COMMAND
                    + "&userid=" + fromAddress
                    + "&filename=" + filename.replaceAll(" ", "%20")
                    + " target=_top> " + attachedFile + "</a> <br>");
        }

        return buffer.toString();
    }
    
    public boolean tempfiledownload(ResultSet rs) throws IOException, SQLException{
        FileOutputStream fos = null;
        ResultSet result = null;
        log.info("tempfiledownload method");
        try{
            result = rs;
            String fileName = result.getString("file_name");
            String userid = null;
            InputStream ist_filebody = null;
            
            userid = fromAddress;
            
//          fileName = fileName.replaceAll(" ", "%20");
            if (fileName != null) {
                log.info("MessageParser.getPart() : file = " + fileName);
                // 첨부 파일을 서버의 내려받기 임시 저장소에 저장
                
                String tempUserDir = this.downloadTempDir + userid;
                File dir = new File(tempUserDir);
                if (!dir.exists()) {  // tempUserDir 생d성
                    dir.mkdir();
                }

                //String filename = file_name;
                // 파일명에 " "가 있을 경우 서블릿에 파라미터로 전달시 문제 발생함.
                // " "를 모두 "_"로 대체함.
//                filename = filename.replaceAll("%20", " ");
                //DataHandler dh = p.getDataHandler();
                fos = new FileOutputStream(tempUserDir + "/" + fileName);
                
                while(rs.next()){
                     ist_filebody = result.getBinaryStream("file_body");
                     
                     int byteRead;
                     while((byteRead = ist_filebody.read()) != -1){
                         fos.write(byteRead);
                     }
                     fos.flush();
                     fos.close();
                     
                }  
             }
            result.close();
            return true;
        }
        catch(Exception ex){
            log.info("temp folder file save failed");
            log.info(ex.getMessage());
            result.close();
            return false;
        }       
    }
    
    public Boolean connectToDB2() throws SQLException{
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet result = null;
        
        FileOutputStream fos = null;
        InputStream ist_filebody = null;
        //int Number = num;
        
        log.info(number);

        try {
            //DBCP데이터베이스 기법 사용
            //데이터베이스 정보는 context.xml에 있음
            //Context 와 Datasource 검색
            //log.info("database connect");
            String JNDIname = "java:/comp/env/jdbc/Webmail";
            //log.info(agent.getUserid());

            javax.naming.Context ctx = new javax.naming.InitialContext();
            javax.sql.DataSource ds = (javax.sql.DataSource)ctx.lookup(JNDIname);

            //Connection 객체 생성
            conn = ds.getConnection();
            //Statement 객체 생성
            stmt = conn.createStatement();

            if (conn == null) {
                throw new Exception("DB Connect Fail");
            }
            stmt = conn.createStatement();
            //복호화
            //ResultSet rs = stmt.executeQuery("SELECT * FROM test WHERE t_user");

            String sql = "SELECT "
                    + "CAST(AES_DECRYPT(UNHEX(sender), 'sender') AS CHAR), "
                    + "CAST(AES_DECRYPT(UNHEX(recipients), 'recipient') AS CHAR), "
                    + "CAST(AES_DECRYPT(UNHEX(message_name), 'message_name') AS CHAR), "
                    + "CAST(AES_DECRYPT(UNHEX(CarbonCopy),'CarbonCopy') AS CHAR), "
                    + "CAST(AES_DECRYPT(UNHEX(message_body), 'message_body') AS CHAR), "                 
                    + "file_name, file_body, saveDate FROM sent_mail_inbox WHERE num= " + number + ";";
            
            result = stmt.executeQuery(sql);
            
            while(result.next()){
                fromAddress = result.getString("CAST(AES_DECRYPT(UNHEX(sender), 'sender') AS CHAR)");
                toAddress = result.getString("CAST(AES_DECRYPT(UNHEX(recipients), 'recipient') AS CHAR)");
                ccAddress = result.getString("CAST(AES_DECRYPT(UNHEX(CarbonCopy),'CarbonCopy') AS CHAR)");
                subject = result.getString("CAST(AES_DECRYPT(UNHEX(message_name), 'message_name') AS CHAR)");
                body = result.getString("CAST(AES_DECRYPT(UNHEX(message_body), 'message_body') AS CHAR)");
                filename = result.getString("file_name");
                //ist_filebody = decrypt_rs.getBinaryStream("file_body");
                saveDate = result.getString("saveDate");
                
                if (filename != null) {
                log.info("MessageParser.getPart() : file = " + filename);
                // 첨부 파일을 서버의 내려받기 임시 저장소에 저장
                
                String tempUserDir = this.downloadTempDir + fromAddress;
                File dir = new File(tempUserDir);
                if (!dir.exists()) {  // tempUserDir 생d성
                    dir.mkdir();
                }

                //String filename = file_name;
                // 파일명에 " "가 있을 경우 서블릿에 파라미터로 전달시 문제 발생함.
                // " "를 모두 "_"로 대체함.
//                filename = filename.replaceAll("%20", " ");
                //DataHandler dh = p.getDataHandler();
                fos = new FileOutputStream(tempUserDir + "/" + filename);

                ist_filebody = result.getBinaryStream("file_body");

                int byteRead;
                while((byteRead = ist_filebody.read()) != -1){
                    fos.write(byteRead);
                }
                fos.flush();
                fos.close();
                     
                }  
             }
            log.info(fromAddress);
            log.info(toAddress);
            log.info(filename);

           return true;
        }
        catch(Exception ex){
            log.info("database connect failed");
            log.info(ex.getMessage());
            return false;
        }
        finally{
            result.close();
            stmt.close();
            conn.close();
        }
      }
    
}
