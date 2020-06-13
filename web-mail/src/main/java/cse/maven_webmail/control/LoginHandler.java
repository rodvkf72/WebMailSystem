/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.control;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import cse.maven_webmail.model.Pop3Agent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.NamingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jongmin
 */
public class LoginHandler extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static final Logger logger =  LoggerFactory.getLogger(LoginHandler.class);
    private final String ADMINISTRATOR = "admin";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        Log log = LogFactory.getLog(LoginHandler.class);

        int selected_menu = Integer.parseInt((String) request.getParameter("menu"));


        try {
            switch (selected_menu) {
                case CommandType.LOGIN:
                    String host = (String) request.getSession().getAttribute("host");
                    String userid = request.getParameter("userid");
                    String password = request.getParameter("passwd");
                    
                    // Check the login information is valid using <<model>>Pop3Agent.
                    Pop3Agent pop3Agent = new Pop3Agent(host, userid, password);
                    boolean isLoginSuccess = pop3Agent.validate();
//                    boolean isLoginSuccess = false;

                    // Now call the correct page according to its validation result.
                    if (isLoginSuccess) {
                        if (isAdmin(userid)) {
                            // HttpSession 객체에 userid를 등록해 둔다.
                            session.setAttribute("userid", userid);
                            response.sendRedirect("admin_menu.jsp");
                        } else {
                            // HttpSession 객체에 userid와 password를 등록해 둔다.
                            session.setAttribute("userid", userid);
                            session.setAttribute("password", password);
                            
                            //비밀번호 마지막 변경 날짜를 받아오는 함수 호출
               
                            int resultDate = getPwdChangedDate(request, response, out, userid, log);
                            
                            //마지막 변경 후 지난 날짜가 90일 이상일경우
                            if(resultDate > 90)
                            {
                                //비밀번호 변경 요청 페이지로 이동
                                response.sendRedirect("change_pwd.jsp");
                                log.info("change the password");
                            }
                            else
                            {
                                //90일 이하이거나 0일경우 메인 메뉴 페이지로 이동
                                log.info("main_menu.jsp");
                                response.sendRedirect("main_menu.jsp");
                            }
                        }
                    } else {
                        RequestDispatcher view = request.getRequestDispatcher("login_fail.jsp");
                        view.forward(request, response);
//                        response.sendRedirect("login_fail.jsp");
                    }
                    break;
                case CommandType.LOGOUT:
                    out = response.getWriter();
                    session.invalidate();
//                    response.sendRedirect(homeDirectory);
                    response.sendRedirect(getServletContext().getInitParameter("HomeDirectory"));
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            logger.error("LoginCheck - LOGIN error : " + ex);
        } finally {
            out.close();
        }
    }

    protected boolean isAdmin(String userid) {
        boolean status = false;

        if (userid.equals(this.ADMINISTRATOR)) {
            status = true;
        }

        return status;
    }
    

    //데이터베이스에서 마지막으로 비밀번호를 변경한 날짜로부터 몇일이 지났는지 받아온다.

    int getPwdChangedDate(HttpServletRequest request, HttpServletResponse response, PrintWriter out, String userid, Log log) throws SQLException{
       Connection conn = null;
        try{
            //DBCP데이터베이스 기법 사용
            //데이터베이스 정보는 context.xml에 있음

            //Context 와 Datasource 검색
            log.info("database connect");
            String JNDIname = "java:/comp/env/jdbc/Webmail";
            log.info(userid);
            String userID = userid;
            javax.naming.Context ctx = new javax.naming.InitialContext();
            javax.sql.DataSource ds = (javax.sql.DataSource)ctx.lookup(JNDIname);
            
            //Connection 객체 생성
            conn = ds.getConnection();
            //Statement 객체 생성
            Statement stmt = conn.createStatement();
            
            //SQL 질의 실행
            String sql = "select datediff(now(), mDate), mDate from backup_usersupdate where username = "
                    + "'" + userID + "'"
                    +" order by mDate desc limit 1";
            log.info(sql);
            ResultSet rs = stmt.executeQuery(sql);
            
            int resultdate;
            
            if(rs.next())
            {
                resultdate = rs.getInt(1);
                log.info(resultdate);
            }
            else
                resultdate=0;
            
            stmt.close();
            conn.close();
            
            
            return resultdate;
        }
        catch(SQLException | NamingException ex){
            log.info("database connect failed");
            log.info(ex.getMessage());
            out.println("오류가 발생했습니다.(발생 오류:" + ex.getMessage()+")");
            return 0;
        } finally {
            conn.close();
        }
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
            throws ServletException,
            IOException {
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
            throws ServletException,
            IOException {
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
