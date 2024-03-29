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
import cse.maven_webmail.model.FormParser;
import cse.maven_webmail.model.SmtpAgent;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jongmin
 */
public class WriteMailHandler extends HttpServlet {
  
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    private static final Logger logger =  LoggerFactory.getLogger(WriteMailHandler.class);
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
//        PrintWriter out = response.getWriter();
        PrintWriter out = null;

        try {
            request.setCharacterEncoding("UTF-8");
            int select = Integer.parseInt((String) request.getParameter("menu"));

            switch (select) {
//                case CommandType.WRITE_MENU:  // 메일 쓰기 화면
//                    out = response.getWriter();
//                    response.sendRedirect(homeDirectory + "write_mail.jsp");
//                    break;


                case CommandType.SEND_MAIL_COMMAND: // 실제 메일 전송하기
                    long start = System.currentTimeMillis();
                    out = response.getWriter();
                    boolean status = sendMessage(request);
                    out.print(getMailTransportPopUp(status));
                    long end = System.currentTimeMillis();
                    logger.info("소요시간 : " + (end - start)/1000.0);
//                    out.flush();
                    break;

                default:
                    out = response.getWriter();
                    out.println("없는 메뉴를 선택하셨습니다. 어떻게 이 곳에 들어오셨나요?");
                    break;
            }
        } catch (Exception ex) {
            logger.info(ex.toString());
        } finally {
            out.close();
        }
    }

    private boolean sendMessage(HttpServletRequest request) throws SQLException {
        boolean status = false;

        // 1. toAddress, ccAddress, subject, body, file1 정보를 파싱하여 추출
        FormParser parser = new FormParser(request);
        parser.parse();

        // 2.  request 객체에서 HttpSession 객체 얻기
        HttpSession session = (HttpSession) request.getSession();

        // 3. HttpSession 객체에서 메일 서버, 메일 사용자 ID 정보 얻기
        String host = (String) session.getAttribute("host");
        String userid = (String) session.getAttribute("userid");

        // 4. SmtpAgent 객체에 메일 관련 정보 설정
        SmtpAgent agent = new SmtpAgent(host, userid);
        String fileName = parser.getFileName();
        String fileName2 = parser.getFileName2();
        logger.info("file name check : " + fileName);

        agent.setTo(parser.getToAddress());
        agent.setCc(parser.getCcAddress());
        agent.setSubj(parser.getSubject());
        agent.setBody(parser.getBody());
        logger.debug("WriteMailHandler.sendMessage() : fileName = " + fileName);

        if (fileName != null) {
            agent.setFile1(fileName);

        }
        if (fileName2 != null) {
            agent.setFile2(fileName2);
        }

        // 5. 메일 전송 권한 위임
        //sendMessage()와 savesentmail(agent)가 둘다 true일 때
        if (agent.sendMessage()){
            status = true;
            //status를 true로 하고 보냄 메일함에 데이터 저장      
        }
        return status;
    }  // sendMessage()
    

    private String getMailTransportPopUp(boolean success) {
        String alertMessage = null;
        if (success) {
            alertMessage = "메일 전송이 성공했습니다.";
        } else {
            alertMessage = "메일 전송이 실패했습니다.";
        }

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
        successPopUp.append("window.location = \"main_menu.jsp?ps=1&pe=10&no=1\"; ");
        successPopUp.append("}  </script>");
        successPopUp.append("</body></html>");
        return successPopUp.toString();
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
