/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.control;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author rodvk
 */
public class TemporaryHandler extends HttpServlet {
    
    private static final org.slf4j.Logger logger =  LoggerFactory.getLogger(TemporaryHandler.class);

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        Properties props = new Properties();
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            request.setCharacterEncoding("UTF-8");

            String t_user = (String) session.getAttribute("userid");

            String t_number = request.getParameter("number") == null ? "" : request.getParameter("number");
            String t_to = request.getParameter("to") == null ? "" : request.getParameter("to");
            String t_cc = request.getParameter("cc") == null ? "" : request.getParameter("cc");
            String t_subj = request.getParameter("subj") == null ? "" : request.getParameter("subj");
            String t_text = request.getParameter("body") == null ? "" : request.getParameter("body");
            String temporary_file = request.getParameter("temp") == null ? "" : request.getParameter("temp");

            Connection conn = null;
            Statement stmt = null;
            //PreparedStatement pstmt = null;

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBInfo.projectName + "?serverTimezone=UTC", DBInfo.id, DBInfo.pw);
                //이건 개인 디비에 맞게 쓸 때 수정하셔야 합니다.
                // id/ 비밀번호 입력 부분을 변경하였습니다. DBInfo 클래스에서 사용자 관련 내용을 수정하실 수 있습니다. 

                if (conn == null) {
                    throw new Exception("DB Connect Fail");
                }
                stmt = conn.createStatement();
                //String preparedupdate = "UPDATE test SET test_user=?, test_to=?, test_cc=?, test_subj=?, test_text=? WHERE test_number=?;";
                //pstmt = conn.prepareStatement(preparedupdate);
                //String preparetest = "UPDATE test SET test_to=?;";
                //pstmt = conn.prepareStatement(preparetest);
                /* 
         * 널이나 공백 삽입 시 데이터 삽입 방지
         * 임시 보관함 내용을 수정없이 왔다갔다 해도 계속 같은 값이 저장되기 때문에 데이터베이스에 플래그를 설정하여 처음 쓸 때만 저장되고
         * 그 다음부터는 업데이트 구문을 추가하여 실행하도록 하는 쪽으로 구현해야 할 듯 함
                 */
                if (((t_to == null) || (t_to.equals(""))) && ((t_cc == null) || (t_cc.equals(""))) && ((t_subj == null) || (t_subj.equals(""))) && ((t_text == null) || (t_text.equals("")))) {

                } else if (temporary_file.equals("TR")) {
                    /*String test = "1234";
                    pstmt.setString(1, test);
                    pstmt.execute();
                    String statement_user = "HEX(AES_ENCRYPT('" + t_user + "', 'userid'))";
                    String statement_to = "HEX(AES_ENCRYPT('" + t_to + "', 'to'))";
                    String statement_cc = "HEX(AES_ENCRYPT('" + t_cc + "', 'cc'))";
                    String statement_subj = "HEX(AES_ENCRYPT('" + t_subj + "', 'subj'))";
                    String statement_text = "HEX(AES_ENCRYPT('" + t_text + "', 'text'))";
                    String statement_number = "'" + t_number + "';";

                    pstmt.setString(1, statement_user);
                    pstmt.setString(2, statement_to);
                    pstmt.setString(3, statement_cc);
                    pstmt.setString(4, statement_subj);
                    pstmt.setString(5, statement_text);
                    pstmt.setString(6, statement_number);
                    pstmt.executeUpdate();*/
                    stmt.executeUpdate("UPDATE test SET test_user = HEX(AES_ENCRYPT('" + t_user + "', 'userid')),"
                            + "test_to = HEX(AES_ENCRYPT('" + t_to + "', 'to')),"
                            + "test_cc = HEX(AES_ENCRYPT('" + t_cc + "', 'cc')),"
                            + "test_subj = HEX(AES_ENCRYPT('" + t_subj + "', 'subj')),"
                            + "test_text = HEX(AES_ENCRYPT('" + t_text + "', 'text')) WHERE test_number='" + t_number + "';");
                    
                } else {
                    //암호화
                    stmt.executeUpdate("INSERT INTO test (test_user, test_to, test_cc, test_subj, test_text) VALUES ("
                            + "HEX(AES_ENCRYPT('" + t_user + "', 'userid')),"
                            + "HEX(AES_ENCRYPT('" + t_to + "', 'to')),"
                            + "HEX(AES_ENCRYPT('" + t_cc + "', 'cc')),"
                            + "HEX(AES_ENCRYPT('" + t_subj + "', 'subj')),"
                            + "HEX(AES_ENCRYPT('" + t_text + "', 'text')));");
                }
            } finally {
                try {
                    stmt.close();
                    //pstmt.close();
                } catch (Exception ignored) {

                }
                try {
                    conn.close();
                    //pstmt.close();
                } catch (Exception ignored) {

                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
//            Logger.getLogger(TemporaryHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
//            Logger.getLogger(TemporaryHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
