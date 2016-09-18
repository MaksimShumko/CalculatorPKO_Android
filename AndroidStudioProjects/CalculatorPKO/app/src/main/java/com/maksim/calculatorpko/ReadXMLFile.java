package com.maksim.calculatorpko;

/**
 * Created by Maksim on 2016-09-16.
 */
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReadXMLFile {

    String[] months = {
            "Jan.", "Feb.", "Mar.", "Apr.", "May", "June",
            "July", "Aug.", "Sept.", "Oct.", "Nov.", "Dec.", "Jan."
    };

    double sum = 0;
    List<String> results = new ArrayList<String>();
    String result = "";

    int year = 0, month = 1, week = 0, dayOfWeek = 0, day = 0;

    boolean switchWeek;

    public ReadXMLFile(String fileName, String typeName,
                       int timeInterval, int checkBox, String curr) {

        boolean emptyTypeName;
        emptyTypeName = typeName.isEmpty();

        boolean emptyCurr;
        emptyCurr = curr.isEmpty();

        try {

            File fXmlFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("operation");
            NodeList aList = doc.getElementsByTagName("amount");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);
                Node aNode = aList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    Element aElement = (Element) aNode;

                    String string = eElement.getElementsByTagName("exec-date")
                            .item(0).getTextContent();
                    //DateTimeFormatter formatter = DateTimeFormatter
                    //        .ofPattern("yyyy-MM-dd", Locale.ENGLISH);
                    //LocalDate date = LocalDate.parse(string, formatter);

                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    Date date = df.parse(string);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);

                    double amount = Double.parseDouble(eElement.getElementsByTagName("amount")
                            .item(0).getTextContent());

                    if(timeInterval == 0) {
                        if(year != calendar.get(Calendar.YEAR) || (temp + 1) == nList.getLength()) {
                            if((temp + 1) == nList.getLength()) {
                                year--;
                            } else year = calendar.get(Calendar.YEAR);
                            if(sum != 0) {
                                result += (year + 1) + "\t" +
                                        Integer.toString((int)Math.round(this.sum)) + " " + curr;
                                sum = 0;
                            }
                        }
                    } else if(timeInterval == 1) {
                        if(month != calendar.get(Calendar.MONTH) || (temp + 1) == nList.getLength()) {
                            if((temp + 1) == nList.getLength()) {
                                month--;
                            } else month = calendar.get(Calendar.MONTH);
                            if(sum != 0) {
                                results.add(calendar.get(Calendar.YEAR) + "-" + months[month + 1] +
                                        "\t" + Integer.toString((int)Math.round(this.sum)) + " " + curr);
                                sum = 0;
                            }
                        }
                    } else if(timeInterval == 2) {
                        switchWeek = false;
                        if(dayOfWeek < calendar.get(Calendar.DAY_OF_WEEK)) {
                            switchWeek = true;
                        }

                        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                        if(switchWeek || (temp + 1) == nList.getLength()) {
                            week++;
                            if(year != calendar.get(Calendar.YEAR)) {
                                year = calendar.get(Calendar.YEAR);
                                week = 0;
                            }
                            if(sum != 0) {
                                result += year + "-" + calendar.get(Calendar.MONTH) + ", " + week + "\t" +
                                        Integer.toString((int)Math.round(this.sum)) + " " + curr;
                                sum = 0;
                            }
                        }
                    } else if(timeInterval == 3) {
                        if(day != calendar.get(Calendar.DAY_OF_MONTH) || (temp + 1) == nList.getLength()) {
                            if((temp + 1) == nList.getLength()) {
                                day--;
                            } else day = calendar.get(Calendar.DAY_OF_MONTH);
                            if(sum != 0) {
                                result += calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) +
                                        "-" + (day+1) + "\t" +
                                        Integer.toString((int)Math.round(this.sum)) + " " + curr;
                                sum = 0;
                            }
                        }
                    }

                    if((eElement.getElementsByTagName("type")
                            .item(0).getTextContent()
                            .equals(typeName) || emptyTypeName) &&
                            (aElement.getAttribute("curr").equals(curr) || emptyCurr)) {
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

    List<String> getResults() {
        return results;
    }
}