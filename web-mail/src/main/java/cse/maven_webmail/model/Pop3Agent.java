/*
 * File: Pop3Agent.java
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.model;

import java.util.Properties;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jongmin
 */
public class Pop3Agent {

    private String host;
    private String userid;
    private String password;

    private int pageStart; // 첫 페이지 
    private int pageEnd; // 끝 페이지 
    private int pageNo;
    
    private Store store;

    private String exceptionType;

    private static final Logger logger =  LoggerFactory.getLogger(Pop3Agent.class);
    
    public Pop3Agent() {
    }

    public Pop3Agent(String host, String uid, String pad) {
        this.host = host;
        this.userid = uid;
        this.password = pad;
    }

    public boolean validate() {
        boolean status = false;

        try {
            status = connectToStore();
            store.close();
        } catch (Exception ex) {
            logger.error("Pop3Agent.validate() error : " + ex);
            status = false;  // for clarity
        } finally {
            return status;
        }
    }

    public boolean deleteMessage(int msgid, boolean really_delete) {
        boolean status = false;

        if (!connectToStore()) {
            return status;
        }

        try {
            // Folder 설정
//            Folder folder = store.getDefaultFolder();
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);

            // Message에 DELETED flag 설정
            Message msg = folder.getMessage(msgid);
            msg.setFlag(Flags.Flag.DELETED, really_delete);

            // 폴더에서 메시지 삭제
            // Message [] expungedMessage = folder.expunge();
            // <-- 현재 지원 안 되고 있음. 폴더를 close()할 때 expunge해야 함.
            folder.close(true);  // expunge == true
            store.close();
            status = true;
        } catch (Exception ex) {
            logger.error("deleteMessage() error: " + ex);
        } finally {
            return status;
        }
    }

    /*
     * 페이지 단위로 메일 목록을 보여주어야 함.
     */
    public String getMessageList(String ps, String pe, String no) {
        String result = "";
        Message[] messages = null;
        
        if (!connectToStore()) {  // 3.1
            logger.warn("POP3 connection failed!");
            return "POP3 연결이 되지 않아 메일 목록을 볼 수 없습니다.";
        }

        try {
            // 메일 폴더 열기
            Folder folder = store.getFolder("INBOX");  // 3.2
            folder.open(Folder.READ_ONLY);  // 3.3

            int total = folder.getMessageCount();
            
            if(total == 0){
                StringBuilder buffer = new StringBuilder();
                buffer.append("<strong>받은 메일이 없습니다.</strong>");
                result = buffer.toString();
                return buffer.toString();
            }
            
            pageStart = filterPs(ps, total);
            pageEnd = filterPe(pageStart, total);
            
            logger.info("pagestart:"+pageStart +" pageend : "+ pageEnd);
            // 현재 수신한 메시지 가져오기 (range값 입력)
            messages = folder.getMessages(pageStart, pageEnd);      // 3.4
            //Arrays.sort(messages, new CustomComparator());
            
            logger.info("messagecountT" + folder.getMessageCount());
            FetchProfile fp = new FetchProfile();
            // From, To, Cc, Bcc, ReplyTo, Subject & Date
            fp.add(FetchProfile.Item.ENVELOPE);
            folder.fetch(messages, fp);

            MessageFormatter formatter = new MessageFormatter(userid);  //3.5
            
            formatter.setPageNo(Integer.parseInt(no));
            formatter.setPageStart(pageStart);
            formatter.setPageEnd(pageEnd);
            formatter.setTotalMail(folder.getMessageCount());
            
            result = formatter.getMessageTable(messages);   // 3.6

            folder.close(true);  // 3.7
            store.close();       // 3.8
        } catch (Exception ex) {
            logger.error("Pop3Agent.getMessageList() : exception = " + ex);
            result = "Pop3Agent.getMessageList() : exception = " + ex;
        } finally {
            return result;
        }
    }

    private int filterPs(String pagestart, int total){
        int ps;
        
        if(pagestart == null || pagestart.length() == 0)
            ps = 1;
        else
            ps = Integer.parseInt(pagestart);
        
        ps = total - ps;
        
        if(ps < 0 || total < 9) {
            ps = 1;
        } else if(ps + 9 > total){
            ps = total - 9;
        }else {
            ps = ps;
        }
        return ps;
    }
    
    private int filterPe(int pageEnd, int total){
        int pe;
        
        pe = pageEnd+9;
        
        if(pe > total) {
            pe = total;
        } 
        
        return pe;
    }
 
    public String getMessage(int n) {
        String result = "POP3  서버 연결이 되지 않아 메시지를 볼 수 없습니다.";

        if (!connectToStore()) {
            logger.warn("POP3 connection failed!");
            return result;
        }

        try {
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            Message message = folder.getMessage(n);

            MessageFormatter formatter = new MessageFormatter(userid);
            result = formatter.getMessage(message);

            folder.close(true);
            store.close();
        } catch (Exception ex) {
            logger.error("Pop3Agent.getMessageList() : exception = " + ex);
            result = "Pop3Agent.getMessage() : exception = " + ex;
        } finally {
            return result;
        }
    }

    /**
     * 답장에 필요한 수신자, 참조자 문자열을 구하는 메소드입니다. 
     * 
     * @param n 메일번호
     * @return 수신자, 참조자 문자열 
     */
    public String getReply(int n) {
        String result = "POP3  서버 연결이 되지 않아 메시지를 볼 수 없습니다.";

        if (!connectToStore()) {
            logger.warn("POP3 connection failed!");
            return result;
        }

        try {
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            Message message = folder.getMessage(n);

            MessageFormatter formatter = new MessageFormatter(userid);
            result = formatter.getReplyParam(message);

            folder.close(true);
            store.close();
        } catch (Exception ex) {
            logger.error("Pop3Agent.getMessageList() : exception = " + ex);
            result = "Pop3Agent.getMessage() : exception = " + ex;
        } finally {
            return result;
        }
    }
    
    private boolean connectToStore() {
        boolean status = false;
        Properties props = System.getProperties();
        props.setProperty("mail.pop3.host", host);
        props.setProperty("mail.pop3.user", userid);
        props.setProperty("mail.pop3.apop.enable", "false");
        props.setProperty("mail.pop3.disablecapa", "true");  // 200102 LJM - added cf. https://javaee.github.io/javamail/docs/api/com/sun/mail/pop3/package-summary.html
        props.setProperty("mail.debug", "true");

        Session session = Session.getInstance(props);
        session.setDebug(true);

        try {
            store = session.getStore("pop3");
            store.connect(host, userid, password);
            status = true;
        } catch (Exception ex) {
            exceptionType = ex.toString();
        } finally {
            return status;
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pad) {
        this.password = pad;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
    

}  // class Pop3Agent

