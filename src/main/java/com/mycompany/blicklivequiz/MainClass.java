package com.mycompany.blicklivequiz;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.tess4j.*;
import net.sourceforge.tess4j.util.LoadLibs;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainClass {

    static List<String> linksFinal;

    public static void main(String[] args) throws IOException {

        getLinks("Wie alt ist Messi?");
        countAnswerAmount("31");
        countAnswerAmount("10");
        countAnswerAmount("1000");

//        String question = readImage("img\\question.jpeg");
//        String answer1 = readImage("img\\answer1.jpeg");
//        String answer2 = readImage("img\\answer2.jpeg");
//        String answer3 = readImage("img\\answer3.jpeg");
//
//        //remove line breaks
//        question = question.replace("\n", " ");
//
//        List<String> answersList = new ArrayList();
//        answersList.add(answer1);
//        answersList.add(answer2);
//        answersList.add(answer3);
//
//        search(question, answersList);
    }

    public static void getLinks(String question) throws UnsupportedEncodingException, IOException {

        //create the url
        String google = "http://www.google.com/search?q=";
        String searchUrl = google + question;

        System.out.println(searchUrl);

        //html site from the search
        Document document = Jsoup.connect(searchUrl).get();

        //get the list of results
        Elements resultDiv = document.getElementsByClass("srg");
        List<Elements> linkObjects = new ArrayList();
        linksFinal = new ArrayList();

        for (Element divClass : resultDiv) {
            linkObjects.add(divClass.select("a"));
        }

        for (int i = 0; i < linkObjects.size(); i++) {
            for (Element singleLink : linkObjects.get(i)) {
                String linkFinal = singleLink.attr("abs:href");

                if (linkFinal.length() > 1) {
                    linksFinal.add(linkFinal);
                }
            }
        }

    }

    public static List<Integer> countAnswerAmount(String answer) throws IOException {
        List<Integer> occurrences = new ArrayList();
        
        for (int i = 0; i < 5; i++) {
            Document document = Jsoup.connect(linksFinal.get(i)).get();

            String documentAsString = document.toString();

            //count occurrences of answer
            int lastIndex = 0;
            int count = 0;

            while (lastIndex != -1) {
                lastIndex = documentAsString.indexOf(answer, lastIndex);

                if (lastIndex != -1) {
                    count++;
                    lastIndex += answer.length();
                    occurrences.add(count);
                }
            }
            System.out.println(count);

        }
        return occurrences;

    }

    public static void search(String question, List<String> answersList) throws UnsupportedEncodingException, IOException {

        String google = "http://www.google.com/search?q=";

        for (int i = 0; i < answersList.size(); i++) {
            String url = google + question + answersList.get(i);

            System.out.println("url: " + url);

            Document document = Jsoup.connect(url).get();
            Element searchResults = document.getElementById("resultStats");
            System.out.println(question + answersList.get(i));
            System.out.println(searchResults.toString());
            System.out.println("-------------");
        }

    }

    public static String readImage(String filePath) {

        File imageFile = new File(filePath);
        ITesseract instance = new Tesseract();
        File tessDataFolder = LoadLibs.extractTessResources("tessdata");
        instance.setDatapath(tessDataFolder.getAbsolutePath());

        try {
            String result = instance.doOCR(imageFile);
            return result;

        } catch (TesseractException e) {
            System.err.println(e.getMessage());
            return "Error while reading image";

        }

    }

}
