<%-- 
    Document   : test2
    Created on : 2020. 5. 24., 오후 4:28:24
    Author     : rodvk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>

<%
    //단순 DB연결과 값 전달만을 하는 페이지. 일단은 jsp파일로 생성해두었음
    request.setCharacterEncoding("UTF-8");

    String t_to = request.getParameter("to") == null ? "" : request.getParameter("to");
    String t_cc = request.getParameter("cc") == null ? "" : request.getParameter("cc");
    String t_subj = request.getParameter("subj") == null ? "" : request.getParameter("subj");
    String t_text = request.getParameter("body") == null ? "" : request.getParameter("body");

    Connection conn = null;
    Statement stmt = null;
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mail?serverTimezone=UTC", "root", "1463");
        if (conn == null) {
            throw new Exception("DB Connect Fail");
        }
        stmt = conn.createStatement();
        
        /* 
         * 널이나 공백 삽입 시 데이터 삽입 방지
         * 임시 보관함 내용을 수정없이 왔다갔다 해도 계속 같은 값이 저장되기 때문에 데이터베이스에 플래그를 설정하여 처음 쓸 때만 저장되고
         * 그 다음부터는 업데이트 구문을 추가하여 실행하도록 하는 쪽으로 구현해야 할 듯 함
         */
        if (((t_to == null) || (t_to == "")) && ((t_cc == null) || (t_cc == "")) && ((t_subj == null) || (t_subj == "")) && ((t_text == null) || (t_text == ""))) {

        } else {
            //암호화
            stmt.executeUpdate("INSERT INTO test (test_to, test_cc, test_subj, test_text) VALUES ("
                    + "HEX(AES_ENCRYPT('" + t_to + "', 'to')),"
                    + "HEX(AES_ENCRYPT('" + t_cc + "', 'cc')),"
                    + "HEX(AES_ENCRYPT('" + t_subj + "', 'subj')),"
                    + "HEX(AES_ENCRYPT('" + t_text + "', 'text')));");
        }
    } finally {
        try {
            stmt.close();
        } catch (Exception ignored) {

        }
        try {
            conn.close();
        } catch (Exception ignored) {

        }
    }
%>
