/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.control;

/**
 *
 * @author rodvk
 */
public class XSSFilter {
    public final static String Filter(String data){
       data = data
      .replaceAll("&#","")
      .replaceAll("&","&amp;")
      .replaceAll("<", "&lt;")
      .replaceAll(">", "&gt;")
      .replaceAll("\"", "&quot;")
      //.replaceAll("'", "&apos;")
      .replaceAll("'", "&#x27;")
      .replaceAll("/", "&#x2F;")
      .replaceAll(" ", "&nbsp;")
      .replaceAll("\\(", "&#40;")
      .replaceAll("\\)", "&#41;")
      .replaceAll("\n", "<br />");
      return data;
     }
}
