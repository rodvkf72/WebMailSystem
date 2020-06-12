/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.model;

import cse.maven_webmail.control.CommandType;
import javax.mail.Message;

/**
 *
 * @author jongmin
 */
public class MessageFormatter {

    private String userid;  // 파일 임시 저장 디렉토리 생성에 필요

    public MessageFormatter(String userid) {
        this.userid = userid;
    }

    //getMessageList()에서 사용
    public String getMessageTable(Message[] messages) {
        StringBuilder buffer = new StringBuilder();

        // 메시지 제목 보여주기
        buffer.append("<table>");  // table start
        buffer.append("<tr> "
                + " <th> No. </td> "
                + " <th> 보낸 사람 </td>"
                + " <th> 제목 </td>     "
                + " <th> 보낸 날짜 </td>   "
                + " <th> 삭제 </td>   "
                + " </tr>");

        for (int i = messages.length - 1; i >= 0; i--) {
            MessageParser parser = new MessageParser(messages[i], userid);
            parser.parse(false);  // envelope 정보만 필요
            // 메시지 헤더 포맷
            // 추출한 정보를 출력 포맷 사용하여 스트링으로 만들기
            buffer.append("<tr> "
                    + " <td id=no>" + (i + 1) + " </td> "
                    + " <td id=sender>" + parser.getFromAddress() + "</td>"
                    + " <td id=subject> "
                    + " <a href=show_message.jsp?msgid=" + (i + 1) + " title=\"메일 보기\"> "
                    + parser.getSubject() + "</a> </td>"
                    + " <td id=date>" + parser.getSentDate() + "</td>"
                    + " <td id=delete>"
                    + "<a href=ReadMail.do?menu="
                    + CommandType.DELETE_MAIL_COMMAND
                    + "&msgid=" + (i + 1) + "> 삭제 </a>" + "</td>"
                    + " </tr>");
        }
        buffer.append("</table>");

        return buffer.toString();
//        return "MessageFormatter 테이블 결과";
    }

    //show_message.jsp에서 사용
    public String getMessage(Message message) {
        StringBuilder buffer = new StringBuilder();

        MessageParser parser = new MessageParser(message, userid);
        parser.parse(true);

        buffer.append("보낸 사람: " + parser.getFromAddress() + " <br>");
        buffer.append("받은 사람: " + parser.getToAddress() + " <br>");
        buffer.append("Cc &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; : " + parser.getCcAddress() + " <br>");
        buffer.append("보낸 날짜: " + parser.getSentDate() + " <br>");
        buffer.append("제 &nbsp;&nbsp;&nbsp;  목: " + parser.getSubject() + " <br> <hr>");

        buffer.append(parser.getBody());

        String attachedFile = parser.getFileName();
        if (attachedFile != null) {
            buffer.append("<br> <hr> 첨부파일: <a href=ReadMail.do?menu="
                    + CommandType.DOWNLOAD_COMMAND
                    + "&userid=" + this.userid
                    + "&filename=" + attachedFile.replaceAll(" ", "%20")
                    + " target=_top> " + attachedFile + "</a> <br>");
        }

        return buffer.toString();
    }
    
    /**
     * 답장 기능에 필요한 수신자, 참조자 문자열을 얻는 메소드입니다. 
     * 
     * @param message
     * @return 수신자(to), 참조자(cc) 정보를 반환합니다. 
     * 두 문자열은 :으로 구분되어있으며 나중에 write_mail로 이어지는 url을 만들 때 
     * :을 기준으로 수신자인지 참조자인지 결정합니다. 
     */
    public String getReplyParam(Message message) {
        StringBuilder buffer = new StringBuilder();

        MessageParser parser = new MessageParser(message, userid);
        parser.parse(true);

        buffer.append(parser.getFromAddress()); // to
        buffer.append(":");
        buffer.append(parser.getCcAddress()); // cc
        
        return buffer.toString();
    }
}
