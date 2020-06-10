/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.model;

import cse.maven_webmail.control.DBInfo;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 책임: enctype이 multipart/form-data인 HTML 폼에 있는 각 필드와 파일 정보 추출
 *
 * @author jongmin
 */
public class FormParser {

    private HttpServletRequest request;
    private String toAddress = null;
    private String ccAddress = null;
    private String subject = null;
    private String body = null;
    private String fileName = null;
    private final String uploadTargetDir = DBInfo.uploadTempDir; // DBInfo 클래스에서 각자에 맞게 수정해주세요.

    private static final Logger logger =  LoggerFactory.getLogger(FormParser.class);
    
    public FormParser(HttpServletRequest request) {
        this.request = request;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCcAddress() {
        return ccAddress;
    }

    public void setCcAddress(String ccAddress) {
        this.ccAddress = ccAddress;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public void parse() {
        try {
            request.setCharacterEncoding("UTF-8");

            // 1. 디스크 기반 파일 항목에 대한 팩토리 생성
            DiskFileItemFactory diskFactory = new DiskFileItemFactory();
            // 2. 팩토리 제한사항 설정
            diskFactory.setSizeThreshold(10 * 1024 * 1024);
            diskFactory.setRepository(new File(this.uploadTargetDir));
            // 3. 파일 업로드 핸들러 생성
            ServletFileUpload upload = new ServletFileUpload(diskFactory);

            // 4. request 객체 파싱
            List fileItems = upload.parseRequest(request);
            Iterator i = fileItems.iterator();
            while (i.hasNext()) {
                FileItem fi = (FileItem) i.next();
                if (fi.isFormField()) {  // 5. 폼 필드 처리
                    String fieldName = fi.getFieldName();
                    String item = fi.getString("UTF-8");

                    if (fieldName.equals("to")) {
                        setToAddress(item);  // 200102 LJM - @ 이후의 서버 주소 제거
                    } else if (fieldName.equals("cc")) {
                        setCcAddress(item);
                    } else if (fieldName.equals("subj")) {
                        setSubject(item);
                    } else if (fieldName.equals("body")) {
                        setBody(item);
                    }
                } else {  // 6. 첨부 파일 처리
                    if (!(fi.getName() == null || fi.getName().equals(""))) {
                        String fieldName = fi.getFieldName();
                        logger.info("ATTACHED FILE : " + fieldName + " = " + fi.getName());

                        String newFileName = setFileDir(fi.getName());
                        logger.info("changed file name: " + newFileName);
                        
                        // 절대 경로 저장
                        setFileName(uploadTargetDir + "/" + newFileName);
                        // setFileName(uploadTargetDir + "/" + newFileName);
                        File fn = new File(fileName);
                        // upload 완료. 추후 메일 전송후 해당 파일을 삭제하도록 해야 함.
                        if (fileName != null) {
                            fi.write(fn);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("FormParser.parse() : exception = " + ex);
        }
    }  // parse()
    
    /**
     * 파일 이름을 파싱/ 데이터베이스 저장까지 한 후 바뀐 파일명만 리턴합니다. 
     * @param fi
     * @param fileName 
     */
    private String setFileDir(String fileName){
        String[] filenametmp = fileName.split("\\."); // 파일 이름을 확장자와 분리
        String newFileName= ""; // 새 파일이름을 저장할 String 변수 
        String sql;
        
        try{
            // 1. 데이터베이스 세팅
            final String JdbcDriver = "com.mysql.cj.jdbc.Driver";
            
            Class.forName(JdbcDriver);
            
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBInfo.projectName + "?serverTimezone=UTC", DBInfo.id, DBInfo.pw);;
            
            // 2. select 로 중복파일 있는지 찾기 
            sql = "SELECT COUNT(*) FROM file WHERE file_realname = \"" + fileName + "\";";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            // 3. 중복 파일의 개수 세기 
            int count = -1; 
            
            if(rs.next()) {
                count = rs.getInt(1);
            }
            
            logger.info("count: " + Integer.toString(count));
            
            // 3. 중복 파일의 개수 + 1만큼 넘버링을 해서 새로운 파일명을 만듦  
            for(int i=0;i<filenametmp.length;i++) {
                newFileName = newFileName + filenametmp[i];
                if(i+2 == filenametmp.length){
                    count = count + 1;
                    newFileName = newFileName + "_" + Integer.toString(count) + ".";
                }
            }
            
            // 4. 데이터베이스에 파일 정보 추가 
            sql = "INSERT INTO file(file_name, file_realname, user, upload_date) VALUES(?,?,?,?);";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            SimpleDateFormat format1 = new SimpleDateFormat ("YY-MM-dd HH:mm:ss");
            //SimpleDateFormat format2 = new SimpleDateFormat ("MMddHHmmss");
            
            Date time = new Date();	
            String updateTime = format1.format(time);
            //String formattingTime = format2.format(time);
            //newFileName = formattingTime + newFileName;
            
            logger.info("uploadTime : "+updateTime);
            logger.info("new FileName : "+newFileName);
            
            pstmt.setString(1, newFileName); // 파일이름 + 넘버링
            pstmt.setString(2, fileName); // 실제 파일 이름 ( 
            pstmt.setString(3, getToAddress()); // 수신자
            pstmt.setString(4, updateTime); // 전송 날짜 
            
            pstmt.executeUpdate();
            
            // 5. close 
            pstmt.close();
            rs.close();
            stmt.close();
            conn.close();
            
        }catch (Exception ex){
            logger.info("오류발생 + " + ex.toString());
            return "";
        }
        
        return newFileName;
    }
}
