/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.control;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import cse.maven_webmail.model.UserAdminAgent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jongmin
 */
public class UserAdminHandler extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    private static final Logger logger =  LoggerFactory.getLogger(UserAdminHandler.class);
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
//        PrintWriter out = response.getWriter();
        try (PrintWriter out = response.getWriter()) {
//        RequestDispatcher view = request.getRequestDispatcher("main_menu.jsp");
            // Validate if userid == "admin"
            HttpSession session = request.getSession();
            //String userid = "admin";
            String userid = (String) session.getAttribute("userid");
            if (userid == null){
                userid = "admin";
            }
            
            if (/*userid == null || */!userid.equals("admin")) {
                out.println("현재 사용자(" + userid + ")의 권한으로 수행 불가합니다.");
                //out.println("<a href=index.jsp> 초기 화면으로 이동 </a>");
                out.println("<a href=Login.do?menu=61> 초기 화면으로 이동 </a>");
                return;
            } else {

                request.setCharacterEncoding("UTF-8");
                int select = Integer.parseInt((String) request.getParameter("menu"));

                switch (select) {
                    case CommandType.ADD_USER_COMMAND:
                        addUser(request, response, out);
                        break;

                    case CommandType.DELETE_USER_COMMAND:
                        deleteUser(request, response, out);
                        break;

                    default:
                        out.println("없는 메뉴를 선택하셨습니다. 어떻게 이 곳에 들어오셨나요?");
                        break;
                }
            }
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
    }

    private void addUser(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        String server = "127.0.0.1";
        int port = 4555;
        try {
            UserAdminAgent agent = new UserAdminAgent(server, port, this.getServletContext().getRealPath("."));
            String userid = request.getParameter("id");  // for test
            //String userid = "admin";
            String password = request.getParameter("password");// for test
            //String password = "admin";
            
            //String useridfilter = XSSFilter.Filter("userid = " + userid + "<br>");
            //String userpasswordfilter = XSSFilter.Filter("password = " + password + "<br>");
            
            //String matchPtn = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,20}$";
            String matchId = "^[a-zA-Z0-9]*$"; 
            String matchBlacket = "^\\(\\)\\{\\}\\[\\]$";
            String matchTestPtn = "^(?=.*[0-9]).{6,20}$"; //개발 시 테스트 간단화를 위해 까다롭지 않은 정규표현식을 사용 
            
            String matchBlankPtn = "^.*\\s.*$";
            
            boolean regex = false;
            
            // hjk: 실제 사용시에는 out.println() 부분을 지우기
            logger.info("userid = " + userid);
            logger.info("password = " + password);
            
            //id 유효성 체크
            if(userid.equals("")){
                out.println(getUserRegistrationFailurePopUp("아이디를 입력해주세요."));
                return;
            }
            
            regex = Pattern.matches(matchId, userid);

            if(!regex){
                out.println(getUserRegistrationFailurePopUp("id에 영문과 숫자만을 사용해주세요."));
                return;
            }
            
            if(userid.length() <5 || userid.length() > 20){
                out.println(getUserRegistrationFailurePopUp("4자 이상 20자 이하의 아이디를 입력해주세요."));
                return;
            }
            
            // 비밀번호 유효성 체크 
            regex = Pattern.matches(matchTestPtn, password);
            if(!regex){
                out.println(getUserRegistrationFailurePopUp("숫자를 포함한 6자리 이상 20자리 이하의 패스워드를 입력해주세요."));
                return;
            }
            
            if(password.contains(userid)){
                out.println(getUserRegistrationFailurePopUp("아이디를 패스워드에 넣을 수 없습니다."));
                return;
            }
            
            regex = Pattern.matches(matchBlankPtn, password);
            if(regex){
                out.println(getUserRegistrationFailurePopUp("공백을 제외하고 입력해주세요."));
                return;
            }
            
            if(password.contains("(") || password.contains(")") ||password.contains("[") || password.contains("]")
                    || password.contains("{") || password.contains("}") || password.contains("<") || password.contains(">")){
                out.println(getUserRegistrationFailurePopUp("사용할 수 없는 문자가 포함되어 있습니다.."));
                return;
            }
            
            // if (addUser successful)  사용자 등록 성공 팝업창
            // else 사용자 등록 실패 팝업창
            if (agent.addUser(userid, password)) {
                out.println(getUserRegistrationSuccessPopUp());
            } else {
                out.println(getUserRegistrationFailurePopUp("사용자 등록에 실패하였습니다."));
            }
            
            out.flush();
        } catch (Exception ex) {
            logger.error(ex.toString());
            out.println("시스템 접속에 실패했습니다.");
        }
    }

    private String getUserRegistrationSuccessPopUp() {
        String alertMessage = "사용자 등록이 성공했습니다.";
        StringBuilder successPopUp = new StringBuilder();
        successPopUp.append("<html>");
        successPopUp.append("<head>");

        successPopUp.append("<title>메일 전송 결과</title>");
        successPopUp.append("<link type=\"text/css\" rel=\"stylesheet\" href=\"css/main_style.css\" />");
        successPopUp.append("</head>");
        successPopUp.append("<body onload=\"goMainMenu()\">");
        successPopUp.append("<script type=\"text/javascript\">");
        successPopUp.append("function goMainMenu() {");
        successPopUp.append("alert(\"");
        successPopUp.append(alertMessage);
        successPopUp.append("\"); ");
        successPopUp.append("window.location = \"index.jsp\"; ");
        successPopUp.append("}  </script>");
        successPopUp.append("</body></html>");
        return successPopUp.toString();
    }

    private String getUserRegistrationFailurePopUp(String alertMessage) {
        StringBuilder successPopUp = new StringBuilder();
        successPopUp.append("<html>");
        successPopUp.append("<head>");

        successPopUp.append("<title>메일 전송 결과</title>");
        successPopUp.append("<link type=\"text/css\" rel=\"stylesheet\" href=\"css/main_style.css\" />");
        successPopUp.append("</head>");
        successPopUp.append("<body onload=\"goMainMenu()\">");
        successPopUp.append("<script type=\"text/javascript\">");
        successPopUp.append("function goMainMenu() {");
        successPopUp.append("alert(\"");
        successPopUp.append(alertMessage);
        successPopUp.append("\"); ");
        successPopUp.append("window.location = \"signup.jsp\"; ");
        successPopUp.append("}  </script>");
        successPopUp.append("</body></html>");
        return successPopUp.toString();
    }

    
    private void deleteUser(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        String server = "127.0.0.1";
        int port = 4555;
        try {
            UserAdminAgent agent = new UserAdminAgent(server, port, this.getServletContext().getRealPath("."));
            String[] deleteUserList = request.getParameterValues("selectedUsers");
            agent.deleteUsers(deleteUserList);
            response.sendRedirect("admin_menu.jsp");
        } catch (Exception ex) {
            logger.error(" UserAdminHandler.deleteUser : exception = " + ex);
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
        processRequest(request, response);
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
        processRequest(request, response);
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
