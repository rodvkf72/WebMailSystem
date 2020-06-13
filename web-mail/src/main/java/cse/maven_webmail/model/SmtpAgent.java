/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.model;

import com.sun.mail.smtp.SMTPMessage;
import cse.maven_webmail.control.WriteMailHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jongmin
 */
public class SmtpAgent {

    protected String host = null;
    protected String userid = null;
    protected String to = null;
    protected String cc = null;
    protected String subj = null;
    protected String body = null;
    protected String file1 = null;
    protected String file2 = null;
    protected Multipart mp;

    private static final Logger logger = LoggerFactory.getLogger(SmtpAgent.class);
=======
    protected File attachedFile = null;
>>>>>>> 06481a15806221043498e2018e7b54991e9913f2

    public SmtpAgent(String host, String userid) {
        this.host = host;
        this.userid = userid;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getSubj() {
        return subj;
    }

    public void setSubj(String subj) {
        this.subj = subj;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFile1() {
        return file1;
    }

    public void setFile1(String file1) {
        this.file1 = file1;
    }

    public String getFile2() {
        return file2;
    }

    public void setFile2(String file2) {
        this.file2 = file2;
    }

    public class Inner implements Runnable {

        String file;
        Multipart mtp;

        public Inner(String file, Multipart mp) {
            this.file = file;
            this.mtp = mp;
        }

        public void run() {
            if (this.file != null) {
                MimeBodyPart a1 = new MimeBodyPart();
                DataSource src = new FileDataSource(this.file);
                try {
                    a1.setDataHandler(new DataHandler(src));
                } catch (MessagingException ex) {
                    java.util.logging.Logger.getLogger(SmtpAgent.class.getName()).log(Level.SEVERE, null, ex);
                }
                int index = this.file.lastIndexOf('/');
                String fileName = this.file.substring(index + 1);
                try {
                    // "B": base64, "Q": quoted-printable
                    a1.setFileName(MimeUtility.encodeText(fileName, "UTF-8", "B"));
                } catch (UnsupportedEncodingException ex) {
                    java.util.logging.Logger.getLogger(SmtpAgent.class.getName()).log(Level.SEVERE, null, ex);
                } catch (MessagingException ex) {
                    java.util.logging.Logger.getLogger(SmtpAgent.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    mtp.addBodyPart(a1);
                } catch (MessagingException ex) {
                    java.util.logging.Logger.getLogger(SmtpAgent.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    // LJM 100418 -  현재 로그인한 사용자의 이메일 주소를 반영하도록 수정되어야 함. - test only
    // LJM 100419 - 일반 웹 서버와의 SMTP 동작시 setFrom() 함수 사용 필요함.
    //              없을 경우 메일 전송이 송신주소가 없어서 걸러짐.
    public boolean sendMessage() {
        boolean status = false;

        // 1. property 설정
        Properties props = System.getProperties();
        props.put("mail.smtp.host", this.host);
        logger.info("SMTP host : " + props.get("mail.smtp.host"));

        // 2. session 가져오기
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(false);

        try {
            SMTPMessage msg = new SMTPMessage(session);
            mp = new MimeMultipart();

            Runnable r = new Inner(this.file1, mp);
            Runnable r2 = new Inner(this.file2, mp);
            Thread t = new Thread(r);
            Thread t2 = new Thread(r2);
            t.start();
            t2.start();
            //Multipart mp = new MimeMultipart();
            //Runnable r = new Inner(this.file1, mp);
            //Thread t = new Thread(r);
            //t.start();
            // msg.setFrom(new InternetAddress(this.userid + "@" + this.host));
            msg.setFrom(new InternetAddress(this.userid));  // 200102 LJM - 테스트 목적으로 수정
            //msg.setFrom(new InternetAddress("jongmin@deu.ac.kr"));

            // setRecipient() can be called repeatedly if ';' or ',' exists
            if (this.to.indexOf(';') != -1) {
                this.to = this.to.replaceAll(";", ",");
            }
            msg.setRecipients(Message.RecipientType.TO, this.to);  // 200102 LJM - 수정
//            msg.setRecipients(Message.RecipientType.TO, new String("지니<genie@localhost>"));

//            if (!getCc().equals("")) {
//                msg.setRecipients(Message.RecipientType.CC, this.cc);
//            }
            if (this.cc.length() > 1) {
                if (this.cc.indexOf(';') != -1) {
                    this.cc = this.cc.replaceAll(";", ",");
                }
                msg.setRecipients(Message.RecipientType.CC, this.cc);
            }

            //msg.setSubject(s);
//            msg.setSubject(MimeUtility.encodeText(this.subj, "euc-kr", "B"));
            msg.setSubject(this.subj);

            //msg.setHeader("Content-Type", "text/plain; charset=utf-8");
            msg.setHeader("User-Agent", "LJM-WM/0.1");
            //msg.setHeader("Content-Transfer-Encoding", "8bit");
            //msg.setAllow8bitMIME(true);

            // body
            MimeBodyPart mbp = new MimeBodyPart();
            // Content-Type, Content-Transfer-Encoding 설정 의미 없음.
            // 자동으로 설정되는 것 같음. - LJM 041202
            //mbp.setHeader("Content-Type", "text/plain; charset=euc-kr");
            //mbp.setHeader("Content-Transfer-Encoding", "8bit");
            mbp.setText(this.body);
            mp.addBodyPart(mbp);

            /*if (this.file1 != null) {
                logger.info("tlqkf1");
                MimeBodyPart a1 = new MimeBodyPart();
                DataSource src = new FileDataSource(this.file1);
                a1.setDataHandler(new DataHandler(src));
                int index = this.file1.lastIndexOf('/');
                String fileName = this.file1.substring(index + 1);
                a1.setFileName(MimeUtility.encodeText(fileName, "UTF-8", "B"));
                mp.addBodyPart(a1);
            }

            if (this.file2 != null) {
                logger.info("tlqkf2");
                MimeBodyPart a2 = new MimeBodyPart();
                DataSource src2 = new FileDataSource(this.file2);
                a2.setDataHandler(new DataHandler(src2));
                int index2 = this.file2.lastIndexOf('/');
                String fileName2 = this.file2.substring(index2 + 1);
                a2.setFileName(MimeUtility.encodeText(fileName2, "UTF-8", "B"));
                mp.addBodyPart(a2);
            }*/

            // 첨부 파일 추가
            t.join();
            t2.join();
            msg.setContent(mp);

            // 메일 전송
            Transport.send(msg);

            // 메일 전송 완료되었으므로 서버에 저장된
            // 첨부 파일 삭제함
            /*if (this.file1 != null) {
                File f = new File(this.file1);
                boolean sentinsertsuccess = savesentmail(f);
                logger.info("sent mail insert success = " + sentinsertsuccess);
                if (!f.delete()) {
                    logger.error(this.file1 + " not yet1.");
                }
            }
            if (this.file2 != null) {
                File f = new File(this.file2);
                if (!f.delete()){
                    logger.error(this.file2 + "not yet2");
                }
            }*/
            status = true;

        } catch (Exception ex) {
            logger.error("sendMessage() error: " + ex);
        } finally {
            return status;
        }
    }  // sendMessage()

    boolean savesentmail(File file) throws SQLException{

        Log log = LogFactory.getLog(SmtpAgent.class);
        Statement stmt = null;
        Connection conn = null;

        try{
            String userId = userid;
            String toAddress = to;
            String ccAddress = cc;
            String subject = subj;
            String text = body;
            String fname = file1;
            String filename = fname.substring(fname.lastIndexOf("/")+1);
            File attachedfile = file;
            int fileLength = (int) attachedfile.length();

            InputStream ins = new FileInputStream(attachedfile);


            //DBCP데이터베이스 기법 사용
            //데이터베이스 정보는 context.xml에 있음
            //Context 와 Datasource 검색
            log.info("try to connect the database to save sent mail...");

            String JNDIname = "java:/comp/env/jdbc/Webmail";
            log.info(userId);
            log.info(filename);

            javax.naming.Context ctx = new javax.naming.InitialContext();
            javax.sql.DataSource ds = (javax.sql.DataSource)ctx.lookup(JNDIname);

            //Connection 객체 생성
            conn = ds.getConnection();
            //Statement 객체 생성
            stmt = conn.createStatement();

            //SQL 질의 실행

            String sql = "INSERT INTO sent_mail_inbox (sender, recipients, CarbonCopy, message_name, message_body, file_name, file_body, saveDate) VALUES ("
                    + "HEX(AES_ENCRYPT('" + userId + "', 'sender')),"
                    + "HEX(AES_ENCRYPT('" + toAddress + "', 'recipient')),"
                    + "HEX(AES_ENCRYPT('" + ccAddress + "', 'CarbonCopy')),"
                    + "HEX(AES_ENCRYPT('" + subject + "', 'message_name')),"
                    + "HEX(AES_ENCRYPT('" + text + "', 'message_body')),"
                    + "?, ?, + now());";



            //String sql = "INSERT INTO sent_mail_inbox (sender, recipients, CarbonCopy, message_name, message_body, file_body, saveDate) VALUES(HEX(AES_ENCRYPT(?, ?)), HEX(AES_ENCRYPT(?, ?)), HEX(AES_ENCRYPT(?, ?)), HEX(AES_ENCRYPT(?, ?)),HEX(AES_ENCRYPT(?, ?)), ?, now());";
           // String sql = "INSERT INTO attachedfiletbl (filename, filebody) VALUES(?, ?);";

           java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
            /*
            pstmt.setString(1, userId);
            pstmt.setString(2, "sender");
            pstmt.setString(3, toAddress);
            pstmt.setString(4, "recipeint");
            pstmt.setString(5, ccAddress);
            pstmt.setString(6, "CarbonCopy");
            pstmt.setString(7, subject);
            pstmt.setString(8, "message_name");
            pstmt.setString(9, text);
            pstmt.setString(10, "message_body");
 */
            pstmt.setString(1, filename);
            pstmt.setBinaryStream(2, ins, fileLength);

            log.info(sql);

            pstmt.execute();
            /*
            if(count>0)
            {
                log.info(count);
                log.info("insert success");
                return true;
            }
            else
            {
                log.info(count);
                log.info("failed..");
                return false;
            }
            */
            log.info("database connect success");
            return true;
        }
        catch(Exception ex){
            log.info("database connect failed");
            log.info(ex.getMessage());
            return false;
        }
        finally{
             if (stmt != null)
                stmt.close();
             if (conn != null)
                 conn.close();

        }

    }


}
