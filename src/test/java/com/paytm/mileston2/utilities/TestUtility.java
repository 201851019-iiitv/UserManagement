package com.paytm.mileston2.utilities;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;


public class TestUtility {

    public static Object getObjectFromFile(String jsonFileName,Class clss) throws IOException {
        File file = new File("src/test/resources/SampleData/"+jsonFileName);
        String content = readFile(file);
        return new Gson().fromJson(content, clss);
    }

    public static String getJsonStringFromFile(String jsonFileName) throws IOException {
        File file = new File("src/test/resources/SampleData/"+jsonFileName);
        String content = readFile(file);
        return content;
    }

    public static Object getObjectFromjsonString(String content ,Class clss) {
        return new Gson().fromJson(content, clss);
    }

    public static String  readFile(File file) throws  IOException{
        BufferedReader br = null;
        FileReader fr = null;
        StringBuilder content = new StringBuilder();

        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                content.append(sCurrentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return content.toString();
    }

}
