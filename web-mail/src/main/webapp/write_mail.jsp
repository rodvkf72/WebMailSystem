<%-- 
    Document   : write_mail.jsp
    Author     : jongmin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cse.maven_webmail.control.CommandType" %>
<%@page import="java.sql.*"%>

<!DOCTYPE html>

<%-- @taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" --%>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>메일 쓰기 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>
        <jsp:include page="header.jsp" />

        <div id="sidebar">
            <jsp:include page="sidebar_previous_menu.jsp" />
        </div>

        <div id="main">
            <%-- <jsp:include page="mail_send_form.jsp" /> --%>

            <form enctype="multipart/form-data" method="POST" name="frm"
                  action="WriteMail.do?menu=<%= CommandType.SEND_MAIL_COMMAND%>">
                <table>
                    <tr>
                        <td> 수신 </td>
                        <td> <input type="text" name="to" size="80" required
                                    value=<%=request.getParameter("recv") == null ? "" : request.getParameter("recv")%>>  </td>
                    </tr>
                    <tr>
                        <td>참조</td>
                        <td> <input type="text" name="cc" size="80">  </td>
                    </tr>
                    <tr>
                        <td> 메일 제목 </td>
                        <td> <input type="text" name="subj" size="80">  </td>
                    </tr>
                    <tr>
                        <td colspan="2">본  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 문</td>
                    </tr>
                    <tr>  <%-- TextArea    --%>
                        <td colspan="2">  <textarea rows="15" name="body" cols="80"></textarea> </td>
                    </tr>
                    <tr>
                        <td>첨부 파일</td>
                        <td> <input type="file" name="file1"  size="80">  </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input name="sbt" type="submit" value="메일 보내기" onclick="submitForm();">
                            <input type="reset" value="다시 입력">
                        </td>
                    </tr>
                </table>
            </form>

            <iframe name='ifrm' width='0' height='0' frameborder='0'></iframe>   

            <script type="text/javascript">
                function f_submit() {
                    /*form 에서의 multipart/form-data 때문에 test2.jsp 파일에서
                    * request.getParameter를 null로 받는 것을 방지
                     */
                    document.frm.encoding = "application/x-www-form-urlencoded";
                    //target을 iframe으로 하여 현재 폼의 submit을 페이지 변환 없이 값 전달
                    document.frm.target = 'ifrm';
                    //폼의 액션을 test2.jsp로 재지정
                    document.frm.action = 'test2.jsp';
                    //액션 위치가 변환된 폼 submit
                    document.frm.submit();
                }
                
                //전송 버튼을 눌렀을 때는 아래의 window.onbeforeunload가 사용되지 않도록 하기 위한 변수
                var submitted = true;

                //메일 보내기 버튼 클릭 시 submitted를 false로 전환하여 window.onbeforeunload가 실행되지 않도록 함
                function submitForm() {
                    submitted = false;
                }
                
                //페이지 전환 시 나갈 것인 지를 확인하는 문구를 출력
                window.onbeforeunload = function () {
                    if (submitted) {
                        f_submit();
                        /*Internet Explorer에서는 아래의 return에 작성한 값이 문구로 출력되지만
                         * Chrome 브라우저에서는 return에 작성한 값이 출력되지 않음.
                         * 찾아본 결과 브라우저의 차이라고 함.
                         */
                        return 'abc';
                    }
                }
            </script>
        </div>

        <jsp:include page="footer.jsp" />
    </body>
</html>
