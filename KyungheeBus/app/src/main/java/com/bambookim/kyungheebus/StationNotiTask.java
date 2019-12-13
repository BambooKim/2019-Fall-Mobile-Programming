package com.bambookim.kyungheebus;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class StationNotiTask extends AsyncTask<String, String, String> {

    public final static String TAG = "StationNotiTask";

    private Context context;

    private LinkedHashMap<String, String> routeIdMap = new LinkedHashMap<>();
    private String arrivalBusString = "";

    public StationNotiTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        routeIdMap.put("200000115", "5100번");
        routeIdMap.put("200000112", "7000번");
        routeIdMap.put("234000016", "1112번");
        routeIdMap.put("234001243", "M5107번");
        routeIdMap.put("200000103", "9번");
    }

    @Override
    protected String doInBackground(String... args) {
        try {
            HttpURLConnection con = null;
            BufferedReader reader = null;

            try{
                // 외국어대학 에 도착하는 버스 도착 정보
                URL url = new URL("http://openapi.gbis.go.kr/ws/rest/busarrivalservice/" +
                        "station?serviceKey=1234567890&stationId=228000723");

                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/text");
                con.setDoInput(true);
                con.connect();

                InputStream stream = con.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                return buffer.toString();

            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(con != null){
                    con.disconnect();
                }
                try {
                    if(reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(result));

            Document doc = builder.parse(inputSource);

            Element response = doc.getDocumentElement();
            NodeList msgHeader = response.getElementsByTagName("msgHeader");
            Element Header = (Element) msgHeader.item(0);
            NodeList resultMessageList = Header.getElementsByTagName("resultMessage");
            Element resultMessage = (Element) resultMessageList.item(0);
            String resultMessageStr = resultMessage.getTextContent();

            if (resultMessageStr.equals("결과가 존재하지 않습니다.")) {

            } else {
                NodeList msgBody = response.getElementsByTagName("msgBody");
                Element Body = (Element) msgBody.item(0);
                NodeList busArrivalList = Body.getElementsByTagName("busArrivalList");

                // item 태그 객체의 수만큼 반복한다. (routeId 즉 노선별로 반복)
                for (int i = 0; i < busArrivalList.getLength(); i++) {
                    // i번째 item 태그 객체를 추출한다.
                    Element item_tag = (Element) busArrivalList.item(i);

                    // 어느 노선??
                    NodeList routeId_list = item_tag.getElementsByTagName("routeId");
                    // 몇 정류장 전?
                    NodeList locationNo1_list = item_tag.getElementsByTagName("locationNo1");
                    // 도착 예정 시간
                    NodeList predictTime1_list = item_tag.getElementsByTagName("predictTime1");

                    Element routeId_tag = (Element) routeId_list.item(0);
                    Element locationNo1_tag = (Element) locationNo1_list.item(0);
                    Element predictTime1_tag = (Element) predictTime1_list.item(0);

                    final int locationNo1 = Integer.parseInt(locationNo1_tag.getTextContent());
                    final String routeId = routeId_tag.getTextContent();

                    // 직전 정류장 출발 노선을 따로 추가함
                    if (locationNo1 <= 1 && routeIdMap.get(routeId) != null) {
                        arrivalBusString = arrivalBusString + routeIdMap.get(routeId) + " ";
                        Log.d(TAG, routeIdMap.get(routeId));
                    }
                }

                if (arrivalBusString.length() != 0) {
                    arrivalBusString += "버스가 곧 도착합니다.";

                    showNotification(context, arrivalBusString);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showNotification(Context context, String str) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1");

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("[경희대정문-외국어대학]")
                .setContentText(str)
                .setAutoCancel(true);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                    new NotificationChannel("1", "channel", NotificationManager.IMPORTANCE_HIGH));
        }

        Notification notification = builder.build();

        manager.notify(123, notification);
    }
}
