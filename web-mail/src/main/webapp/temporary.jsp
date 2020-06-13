<%-- 
    Document   : temporary
    Created on : 2020. 5. 11., 오후 3:20:59
    Author     : 김광호
--%>

<%@page import="cse.maven_webmail.control.DBInfo"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<%@page import="java.net.URLEncoder"%>

<!DOCTYPE html>
<% request.setCharacterEncoding("UTF-8"); %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>임시 보관함</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>

    <body>
        <jsp:include page="header.jsp" />

        <div id="sidebar">
            <jsp:include page="sidebar_menu.jsp" />
        </div>

        <div id="main">
            <table>
                <tr>
                <th> 수신자 </td>
                <th> 참조 </td>
                <th> 메일 제목 </td>
                <th> 본문 </td>
                <th> 수정</td>
                </tr>
            
            <%
                Connection conn = null;
                Statement stmt = null;
                String number = null;
                String to = null;
                String cc = null;
                String subj = null;
                String text = null;
                String t_user = (String) session.getAttribute("userid");

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBInfo.projectName + "?serverTimezone=UTC", DBInfo.id, DBInfo.pw);
                    //이건 개인 디비에 맞게 쓸 때 수정하셔야 합니다.
                    // id/ 비밀번호 입력 부분을 변경하였습니다. DBInfo 클래스에서 사용자 관련 내용을 수정하실 수 있습니다. 

                    if (conn == null) {
                        throw new Exception("DB Connect Fail");
                    }
                    stmt = conn.createStatement();
                    //복호화
                    //ResultSet rs = stmt.executeQuery("SELECT * FROM test WHERE t_user");
                    ResultSet decrypt_rs = stmt.executeQuery("SELECT test_number,"
                            + "CAST(AES_DECRYPT(UNHEX(test_to), 'to') AS CHAR), "
                            + "CAST(AES_DECRYPT(UNHEX(test_cc), 'cc') AS CHAR), "
                            + "CAST(AES_DECRYPT(UNHEX(test_subj), 'subj') AS CHAR), "
                            + "CAST(AES_DECRYPT(UNHEX(test_text), 'text') AS CHAR) FROM test WHERE CAST(AES_DECRYPT(UNHEX(test_user), 'userid') AS CHAR)='" + t_user + "';");
                    
                    while (decrypt_rs.next()) {
                        number = decrypt_rs.getString("test_number");
                        to = decrypt_rs.getString("CAST(AES_DECRYPT(UNHEX(test_to), 'to') as char)");
                        cc = decrypt_rs.getString("CAST(AES_DECRYPT(UNHEX(test_cc), 'cc') as char)");
                        subj = decrypt_rs.getString("CAST(AES_DECRYPT(UNHEX(test_subj), 'subj') as char)");
                        text = decrypt_rs.getString("CAST(AES_DECRYPT(UNHEX(test_text), 'text') as char)");%>
   
            <form method="POST" action="write_mail.jsp">
               <tr>
                   <input type="text" name="number" value="<%=number == null ? "" : number%>" hidden>
                   <td id = to><input type="text" name="to" value="<%=to == null ? "" : to%>" readonly style="background-color:transparent;border:0 solid black;text-align:center;width:100px;"> &nbsp;</td>
                   <td id = cc><input type="text" name="cc" value="<%=cc == null ? "" : cc%>" readonly style="background-color:transparent;border:0 solid black;"> &nbsp;</td>
                   <td id = subj><input type="text" name="subj" value="<%=subj == null ? "" : subj%>" readonly style="background-color:transparent;border:0 solid black;"> &nbsp;</td>
                   <td id = body><input type="text" name="text" value="<%=text == null ? "" : text%>" readonly style="background-color:transparent;border:0 solid black;"> &nbsp;</td>
                   <input type="text" name="temporary" value="TR" hidden>
                   <td id = submit><input type="submit" value="수정"></td>
               </tr>
            </form>

            <%}
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
            </table>
        </div>

        <jsp:include page="footer.jsp" />

    </body>
</html>
