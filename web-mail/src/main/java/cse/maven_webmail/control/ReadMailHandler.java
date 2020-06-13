/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import cse.maven_webmail.model.Pop3Agent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jongmin
 */
public class ReadMailHandler extends HttpServlet {

    private static final Logger logger =  LoggerFactory.getLogger(ReadMailHandler.class);
    
    private final String homeDirectory = "/maven_webmail/";
    private final String uploadTempDir = DBInfo.uploadTempDir;
    private final String uploadTargetDir = DBInfo.uploadTempDir;

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        request.setCharacterEncoding("UTF-8");
        int select = Integer.parseInt((String) request.getParameter("menu"));

        switch (select) {
            case CommandType.DELETE_MAIL_COMMAND:
                try (PrintWriter out = response.getWriter()) {
                    deleteMessage(request);
                    response.sendRedirect("main_menu.jsp?ps=1&pe=10&no=1");
                }
                break;

            case CommandType.DOWNLOAD_COMMAND: // 파일 다운로드 처리
                download(request, response);
                break;

            default:
                try (PrintWriter out = response.getWriter()) {
                    out.println("없는 메뉴를 선택하셨습니다. 어떻게 이 곳에 들어오셨나요?");
                }
                break;

        }
    }

    private void download(HttpServletRequest request, HttpServletResponse response) { //throws IOException {
        response.setContentType("application/octet-stream");

        ServletOutputStream sos = null;

        try {
            /* TODO output your page here */
            request.setCharacterEncoding("UTF-8");
            // LJM 041203 - 아래와 같이 해서 한글파일명 제대로 인식되는 것 확인했음.
            String fileName = request.getParameter("filename");
            //String outfileName = XSSFilter.Filter(">>>>>> DOWNLOAD: file name = " + fileName);

            String sentDate = request.getParameter("date");
            //String outfileName = XSSFilter.Filter(">>>>>> DOWNLOAD: file name = " + fileName);
            //위 소스 있으니까 에러가 생겨서 주석처리 했습니다ㅜㅜ 

            //logger.info(outfileName);
            // fileName에 있는 ' '는 '+'가 파라미터로 전송되는 과정에서 변한 것이므로
            // 다시 변환시켜줌.
//            fileName = fileName.replaceAll(" ", "+");
            String userid = request.getParameter("userid");
            //String fileName = URLDecoder.decode(request.getParameter("filename"), "utf-8");

            // download할 파일 읽기

            // LJM 090430 : 수정해야 할 부분 - start ------------------
            // 리눅스 서버 사용시
            //String downloadDir = "/var/spool/webmail/download/";

            // 윈도우즈 환경 사용시
            String downloadDir = DBInfo.downloadTempDir;
            // LJM 090430 : 수정해야 할 부분 - end   ------------------

//            response.setHeader("Content-Disposition", "attachment; filename=" +
//                    MimeUtility.encodeText(fileName) + ";");

//            response.setHeader("Content-Disposition", "attachment; filename=" +
//                    MimeUtility.encodeText(fileName, "UTF-8", "B") + ";");

            response.setHeader("Content-Disposition", "attachment; filename="
                    + URLEncoder.encode(fileName, "UTF-8") + ";");

            //String newFileName = getNewFileName(fileName, sentDate);
            
            logger.info(downloadDir + userid + "/" + fileName);
            File f = new File(downloadDir + userid + "/" + fileName);
            byte[] b = new byte[(int) f.length()];
            // try-with-resource 문은 fis를 명시적으로 close해 주지 않아도 됨.
            try (FileInputStream fis = new FileInputStream(f)) {
                fis.read(b);
            };

            // 다운로드
            sos = response.getOutputStream();
            sos.write(b);
            sos.flush();
            sos.close();
        } catch (Exception ex) {
            logger.error("====== DOWNLOAD exception : " + ex);
        } finally {
            // 다운로드후 파일 삭제
            //f.delete();
        }
    }
    
    private String getNewFileName(String fileName, String sentdate){
        String newFileName= ""; // 새 파일이름을 저장할 String 변수 
        String sql;
        
        try{
            // 1. 데이터베이스 세팅
            final String JdbcDriver = "com.mysql.cj.jdbc.Driver";
            
            Class.forName(JdbcDriver);
            
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBInfo.projectName + "?serverTimezone=UTC", DBInfo.id, DBInfo.pw);;
            
            sentdate = sentdate.replaceAll("-", "");
            String tmpfileName = sentdate + fileName;
            
            // 2. select 로 파일명 찾기 
            sql = "SELECT file_realname FROM file WHERE file_name = \"" + tmpfileName + "\";";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            // 3. directory에 저장된 filename 확인 
            if(rs.next()) {
                newFileName = rs.getString("file_realname");
            }
            
            logger.info("download newFilename: " + newFileName);
            
        }catch (Exception ex){
            logger.info("오류발생 + " + ex.toString());
            return "";
        }
        
        return newFileName;
    }

    private boolean deleteMessage(HttpServletRequest request) {
        int msgid = Integer.parseInt((String) request.getParameter("msgid"));

        HttpSession httpSession = request.getSession();
        String host = (String) httpSession.getAttribute("host");
        String userid = (String) httpSession.getAttribute("userid");
        String password = (String) httpSession.getAttribute("password");

        Pop3Agent pop3 = new Pop3Agent(host, userid, password);
        boolean status = pop3.deleteMessage(msgid, true);
        return status;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
