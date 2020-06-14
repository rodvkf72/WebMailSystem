/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cse.maven_webmail.model;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author miso
 */

public class getSentMail {
    
    //private final String downloadTempDir = "C:/temp/download/";
    private static final Logger log = LoggerFactory.getLogger(getSentMail.class);
    
    
    public String getSentMessageList(String userid) throws SQLException, NamingException{
        
        Connection conn = null;
        Statement stmt = null;
        
        String userID = userid;
        ResultSet decrypt_rs = null;
        
        StringBuilder buffer = new StringBuilder();
        int num = 0;
        String toAddress = null;//받은 사람! 수신자!
        String ccAddress = null;//참조자!
        String subject = null;
        String body = null;
        String saveDate = null;
        String id = userid;
        
        log.info(userID);

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
            //stmt = conn.createStatement();
            //복호화
            //ResultSet rs = stmt.executeQuery("SELECT * FROM test WHERE t_user");

            String sql = "SELECT num, "
                    + "CAST(AES_DECRYPT(UNHEX(recipients), 'recipient') AS CHAR), "
                    + "CAST(AES_DECRYPT(UNHEX(message_name), 'message_name') AS CHAR), "
                    + "CAST(AES_DECRYPT(UNHEX(CarbonCopy),'CarbonCopy') AS CHAR), "
                    + "saveDate FROM sent_mail_inbox WHERE CAST(AES_DECRYPT(UNHEX(sender), 'sender') AS CHAR)='" + userID + "' order by saveDate desc;";
            
            decrypt_rs = stmt.executeQuery(sql);

            buffer.append("<table>");  // table start
            buffer.append("<tr> "
                    + " <th> No. </td> "
                    + " <th> 받은 사람 </td>"
                    + " <th> 참조자 </td>"
                    + " <th> 제목 </td>     "
                    + " <th> 보낸 날짜 </td>   "
                    + " </tr>");

            while(decrypt_rs.next()){
                num = decrypt_rs.getInt("num");
                toAddress = decrypt_rs.getString("CAST(AES_DECRYPT(UNHEX(recipients), 'recipient') AS CHAR)");
                ccAddress = decrypt_rs.getString("CAST(AES_DECRYPT(UNHEX(CarbonCopy),'CarbonCopy') AS CHAR)");
                subject = decrypt_rs.getString("CAST(AES_DECRYPT(UNHEX(message_name), 'message_name') AS CHAR)");
                saveDate = decrypt_rs.getString("saveDate");
                
                buffer.append("<tr> "
                        + " <td id=no>" + num + " </td> "
                        + " <td id=sender>" + toAddress + "</td>"
                        + " <td id=cc>" + ccAddress + "</td>"
                        + " <td id=subject> "
                        + " <a href=show_sentmessage.jsp?msgid=" + num + " title=\"메일 보기\"> "
                        + subject + "</a> </td>"
                        + " <td id=date>" + saveDate + "</td>"
                        + " </tr>");
            }
            
            

            buffer.append("</table>");
            //decrypt_rs.close();
            return buffer.toString();
        }
        catch(Exception ex){
            log.info("loading failed");
            log.info(ex.getMessage());
            
            return "fail";
        }
        finally{
            decrypt_rs.close();
            stmt.close();
            conn.close();
         }        
    }
}
    
  