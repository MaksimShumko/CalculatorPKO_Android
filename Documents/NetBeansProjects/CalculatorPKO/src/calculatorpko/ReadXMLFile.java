/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculatorpko;

/**
 *
 * @author Maksim
 */

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ReadXMLFile {
    
    String[] months = {
        "Dec.", "Jan.", "Feb.", "Mar.", "Apr.", "May", "June", 
        "July", "Aug.", "Sept.", "Oct.", "Nov.", "Dec.", "Jan."
    };
    
    double sum = 0;
    String result = "";
    
    int year = 0, month = 1, week = 0, dayOfWeek = 0, day = 0;
    
    boolean switchWeek;

    public ReadXMLFile(String fileName, String typeName, 
            int timeInterval, int checkBox) {
        
        boolean emptyTypeName;
        emptyTypeName = typeName.isEmpty();

        try {

            File fXmlFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("operation");
            NodeList aList = doc.getElementsByTagName("amount");

            System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {

		Node nNode = nList.item(temp);
                Node aNode = aList.item(temp);

		//System.out.println("\nCurrent Element :" + nNode.getNodeName());

		if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    Element aElement = (Element) aNode;

                    //System.out.println("Order data : " + 
                    //        eElement.getElementsByTagName("order-date").item(0).getTextContent());
                    //System.out.println("Type : " + 
                    //        eElement.getElementsByTagName("type").item(0).getTextContent());
                    //System.out.println("Amount : " + 
                    //        eElement.getElementsByTagName("amount").item(0).getTextContent() + 
                    //        " " + aElement.getAttribute("curr") + "\n");
                    
                    String string = eElement.getElementsByTagName("exec-date")
                            .item(0).getTextContent();
                    DateTimeFormatter formatter = DateTimeFormatter
                            .ofPattern("yyyy-MM-dd", Locale.ENGLISH);
                    LocalDate date = LocalDate.parse(string, formatter);
                    System.out.println(date);
                    
                    double amount = Double.parseDouble(eElement.getElementsByTagName("amount")
                            .item(0).getTextContent());
                    
                    if(timeInterval == 0) {
                        if(year != date.getYear() || (temp + 1) == nList.getLength()) {
                            if((temp + 1) == nList.getLength()) {
                            year--;
                            } else year = date.getYear();
                            System.out.print(year + 1);
                            System.out.println(Integer.toString((int)Math.round(this.sum)));
                            if(sum != 0) { 
                                result += (year + 1) + "\t" + 
                                        Integer.toString((int)Math.round(this.sum)) + "\n";
                                sum = 0;
                            }
                        }
                    } else if(timeInterval == 1) {
                        if(month != date.getMonthValue() || (temp + 1) == nList.getLength()) {
                            if((temp + 1) == nList.getLength()) {
                            month--;
                            } else month = date.getMonthValue();
                            System.out.print(months[month + 1]);
                            System.out.println(Integer.toString((int)Math.round(this.sum)));
                            if(sum != 0) { 
                                result += date.getYear() + "-" + months[month + 1] +
                                        "\t" + Integer.toString((int)Math.round(this.sum)) + "\n";
                                sum = 0;
                            }
                        }
                    } else if(timeInterval == 2) {
                        switchWeek = false;
                        if(dayOfWeek < date.getDayOfWeek().getValue()) {
                            switchWeek = true;
                        }
                        
                        dayOfWeek = date.getDayOfWeek().getValue();
                        
                        if(switchWeek || (temp + 1) == nList.getLength()) {
                            week++;
                            if(year != date.getYear()) {
                                year = date.getYear();
                                week = 0;
                            }
                            System.out.print(year);
                            System.out.println(Integer.toString((int)Math.round(this.sum)));
                            if(sum != 0) { 
                                result += year + "-" + date.getMonthValue() + ", " + week + "\t" +
                                        Integer.toString((int)Math.round(this.sum)) + "\n";
                                sum = 0;
                            }
                        }
                    } else if(timeInterval == 3) {
                        if(day != date.getDayOfMonth() || (temp + 1) == nList.getLength()) {
                            if((temp + 1) == nList.getLength()) {
                            day--;
                            } else day = date.getDayOfMonth();
                            System.out.print(day);
                            System.out.println(Integer.toString((int)Math.round(this.sum)));
                            if(sum != 0) { 
                                result += date.getYear() + "-" + date.getMonthValue() +
                                        "-" + (day+1) + "\t" +
                                        Integer.toString((int)Math.round(this.sum)) + "\n";
                                sum = 0;
                            }
                        }
                    }
                    
                    if(eElement.getElementsByTagName("type")
                            .item(0).getTextContent()
                            .equals(typeName) || emptyTypeName) {
                        if(checkBox == 0) {
                            if(amount < 0) {
                                sum += amount;
                            }
                        }
                        if(checkBox == 1) {
                            sum += amount;
                        }
                        if(checkBox == 2) {
                            if(amount > 0) {
                                sum += amount;
                            }
                        }
                    }
		}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String getSum() {
        return result;
    }
}