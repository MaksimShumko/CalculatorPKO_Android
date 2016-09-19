package com.maksim.calculatorpko;

/**
 * Created by Maksim on 2016-09-16.
 */
import android.util.Log;

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

    int year = 0, month = 1, week = 0, dayOfWeek = 0, day = 0;

    boolean switchWeek;

    public ReadXMLFile(String fileName, SettingsData data) {

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
                    System.out.println(calendar);

                    double amount = Double.parseDouble(eElement.getElementsByTagName("amount")
                            .item(0).getTextContent());

                    switch(data.getInterval()) {
                        case SettingsData.YEAR:
                            if(year != calendar.get(Calendar.YEAR) || (temp + 1) == nList.getLength()) {
                                if((temp + 1) == nList.getLength()) {
                                    year--;
                                } else year = calendar.get(Calendar.YEAR);
                                if(sum != 0) {
                                    results.add((year + 1) + "\t\t" +
                                            Integer.toString((int)Math.round(this.sum)) + " " + data.getCurr());
                                    sum = 0;
                                }
                            }
                            break;
                        case SettingsData.MONTH:
                            if(month != calendar.get(Calendar.MONTH) || (temp + 1) == nList.getLength()) {
                                if((temp + 1) == nList.getLength()) {
                                    month--;
                                } else month = calendar.get(Calendar.MONTH);
                                if(sum != 0) {
                                    results.add(calendar.get(Calendar.YEAR) + "-" + months[month + 1] +
                                            "\t\t" + Integer.toString((int)Math.round(this.sum)) + " " + data.getCurr());
                                    sum = 0;
                                }
                            }
                            break;
                        case SettingsData.WEEK:
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
                                    results.add(year + "-" + (calendar.get(Calendar.MONTH)+1) + ", " +
                                            calendar.get(Calendar.WEEK_OF_YEAR) + "\t\t" +
                                            Integer.toString((int)Math.round(this.sum)) + " " + data.getCurr());
                                    sum = 0;
                                }
                            }
                            break;
                        case SettingsData.DAY:
                            if(day != calendar.get(Calendar.DAY_OF_MONTH) || (temp + 1) == nList.getLength()) {
                                if((temp + 1) == nList.getLength()) {
                                    day--;
                                } else day = calendar.get(Calendar.DAY_OF_MONTH);
                                if(sum != 0) {
                                    results.add(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) +
                                            "-" + (day+1) + "\t\t" +
                                            Integer.toString((int)Math.round(this.sum)) + " " + data.getCurr());
                                    sum = 0;
                                }
                            }
                            break;
                        default:
                            break;
                    }

                    if((eElement.getElementsByTagName("type")
                            .item(0).getTextContent()
                            .equals(data.getOperation()) || data.getOperation().isEmpty()) &&
                            (aElement.getAttribute("curr").equals(data.getCurr()) || data.getCurr().isEmpty())) {
                        if(data.getAmount() == data.ONLY_NEGATIVE) {
                            if(amount < 0) {
                                sum += amount;
                            }
                        }
                        if(data.getAmount() == data.ALL) {
                            sum += amount;
                        }
                        if(data.getAmount() == data.ONLY_POSITIVE) {
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