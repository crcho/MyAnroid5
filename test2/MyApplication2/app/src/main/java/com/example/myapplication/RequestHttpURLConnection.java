package com.example.myapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class RequestHttpURLConnection {
    //샘플용 코드 제작

    public String HttpURLConnectionGet(String strURL, String strParams) {
        String resultData = "";

        try {
            URL url = new URL(strURL + strParams); //get 방식은 parameter를 URL에 묶어서 보낸다.
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setRequestProperty("Accept-Language", "ko-kr");
            conn.setRequestProperty("Access-Control-Allow-Origin", "*");
            conn.setRequestProperty("Content-Type", "application/json");

            conn.setConnectTimeout(10000); // 커넥션 timeout 10초
            conn.setReadTimeout(5000); //컨텐츠 조회시 timeout 5초

            Charset charset = Charset.forName("UTF-8");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            resultData = response.toString();

        } catch (MalformedURLException e) {
            //URL
            e.printStackTrace();
        } catch (IOException e) {
            // HttpURLConnection
            e.printStackTrace();
        }


        return resultData;
    }
}