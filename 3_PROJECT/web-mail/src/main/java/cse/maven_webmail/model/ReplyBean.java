/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.model;

/**
 *답장 기능 구현을 위한 클래스입니다.
 * 
 * @author 김희정
 *  
 */
public class ReplyBean {
    private String fromValue;
    private String ccValue;
    private String url;
    
    public String getFromValue() {
        return fromValue;
    }

    public void setFromValue(String fromValue) {
        this.fromValue = fromValue;
    }

    public String getCcValue() {
        return ccValue;
    }

    public void setCcValue(String ccValue) {
        this.ccValue = ccValue;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    /**
     * get형식으로 받는사람, 참조자 파라미터를 보내서 
     * 답장 기능을 구현하게 해줍니다. 
     * 
     * @param orgrecp 
     */
    public void parseUrl(String orgrecp){
        String recp[] = orgrecp.split(":");
                
        if(recp.length == 2){
            url = "write_mail.jsp?to="+recp[0]+"&cc="+recp[1].replaceAll(" ","");
        }else{
            url = "write_mail.jsp?to="+recp[0];
        }
    }
}
