/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.model;

/**
 *
 * @author unsky
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
    
    public void parseUrl(String tmp){
        String recp[] = tmp.split(":");
                
        if(recp.length == 2){
            url = "write_mail.jsp?recv="+recp[0]+"&cbcp="+recp[1].replaceAll(" ","");
        }else{
            url = "write_mail.jsp?recv="+recp[0];
        }
    }
}
