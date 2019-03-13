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

    public static void main(String[] args) throws IOException {

        String question = readImage("img\\question.jpeg");
        String answer1 = readImage("img\\answer1.jpeg");
        String answer2 = readImage("img\\answer2.jpeg");
        String answer3 = readImage("img\\answer3.jpeg");

        //remove line breaks
        question = question.replace("\n", "").replace("\r", "");
        answer1 = answer1.replace("\n", "").replace("\r", "");
        answer2 = answer2.replace("\n", "").replace("\r", "");
        answer3 = answer3.replace("\n", "").replace("\r", "");
        
        List<String> answersList = new ArrayList();
        answersList.add(answer1);
        answersList.add(answer2);
        answersList.add(answer3);
        
        search(question, answersList);

    }

    public static void search(String question, List<String> answersList) throws UnsupportedEncodingException, IOException {

        String google = "http://www.google.com/search?q=";
        
        for(int i = 0; i < answersList.size(); i++){
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
