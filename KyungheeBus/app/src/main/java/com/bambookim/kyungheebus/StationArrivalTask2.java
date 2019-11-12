package com.bambookim.kyungheebus;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class StationArrivalTask2 extends AsyncTask<String, String, String> {
    public final static String TAG = "StationArrivalTask2";

    ProgressDialog progressDialog;

    private Context mContext;
    private TextView arrival_info;

    private String inputStationId;
    private String inputRouteId;

    static String [] stationList = new String [] {"228000723", "228000710", "228000709", "228000708",
            "228001174", "228000704", "228000703", "203000125"};
    static int arrayPtr = 1;

    public StationArrivalTask2(Context context, TextView arrival_info) {
        mContext = context;
        this.arrival_info = arrival_info;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("도착 정보 가져오는 중...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(R.style.Widget_AppCompat_ProgressBar_Horizontal);

        // progressDialog.show();

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... args) {
        try {
            inputStationId = args[0];
            inputRouteId = args[1];

            HttpURLConnection con = null;
            BufferedReader reader = null;

            try{
                URL url = new URL("http://openapi.gbis.go.kr/ws/rest/busarrivalservice/" +
                        "station?serviceKey=1234567890&stationId=" + args[0]);

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

        // progressDialog.dismiss();


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
                arrival_info.setText("도착 예정 정보 없음");
            } else {
                NodeList msgBody = response.getElementsByTagName("msgBody");
                Element Body = (Element) msgBody.item(0);
                NodeList busArrivalList = Body.getElementsByTagName("busArrivalList");

                arrival_info.setText("도착 예정 정보 없음");

                // item 태그 객체의 수만큼 반복한다.
                for (int i = 0; i < busArrivalList.getLength(); i++) {
                    // i번째 item 태그 객체를 추출한다.
                    Element item_tag = (Element) busArrivalList.item(i);

                    NodeList routeId_list = item_tag.getElementsByTagName("routeId");
                    Element routeId_tag = (Element) routeId_list.item(0);
                    String routeId = routeId_tag.getTextContent();

                    if (!inputRouteId.equals(routeId)) {
                        continue;
                    } else {
                        // 전전 버스
                        NodeList locationNo2_list = item_tag.getElementsByTagName("locationNo2");
                        NodeList plateNo2_list = item_tag.getElementsByTagName("plateNo2");
                        NodeList lowPlate2_list = item_tag.getElementsByTagName("lowPlate2");
                        NodeList predictTime2_list = item_tag.getElementsByTagName("predictTime2");
                        NodeList remainSeatCnt2_list = item_tag.getElementsByTagName("remainSeatCnt2");

                        // 전 버스
                        NodeList locationNo1_list = item_tag.getElementsByTagName("locationNo1");
                        NodeList plateNo1_list = item_tag.getElementsByTagName("plateNo1");
                        NodeList lowPlate1_list = item_tag.getElementsByTagName("lowPlate1");
                        NodeList predictTime1_list = item_tag.getElementsByTagName("predictTime1");
                        NodeList remainSeatCnt1_list = item_tag.getElementsByTagName("remainSeatCnt1");

                        Element locationNo2_tag = (Element) locationNo2_list.item(0);
                        Element plateNo2_tag = (Element) plateNo2_list.item(0);
                        Element lowPlate2_tag = (Element) lowPlate2_list.item(0);
                        Element predictTime2_tag = (Element) predictTime2_list.item(0);
                        Element remainSeatCnt2_tag = (Element) remainSeatCnt2_list.item(0);

                        Element locationNo1_tag = (Element) locationNo1_list.item(0);
                        Element plateNo1_tag = (Element) plateNo1_list.item(0);
                        Element lowPlate1_tag = (Element) lowPlate1_list.item(0);
                        Element predictTime1_tag = (Element) predictTime1_list.item(0);
                        Element remainSeatCnt1_tag = (Element) remainSeatCnt1_list.item(0);

                        // 문자열 데이터 추출
                        boolean isNo1Empty = locationNo1_tag.getTextContent().length() == 0;
                        boolean isNo2Empty = locationNo2_tag.getTextContent().length() == 0;

                        arrival_info.setText("");

                        if (isNo2Empty) {
                            if (isNo1Empty) {       // 막차 아예 끊김
                                arrival_info.append("도착 예정 정보 없음\n");
                                arrival_info.append("도착 예정 정보 없음");

                            } else {                // 막차 한대 남음
                                final int locationNo1 = Integer.parseInt(locationNo1_tag.getTextContent());
                                final String plateNo1 = plateNo1_tag.getTextContent();
                                final int lowPlate1 = Integer.parseInt(lowPlate1_tag.getTextContent());
                                final int predictTime1 = Integer.parseInt(predictTime1_tag.getTextContent());
                                final int remainSeatCnt1 = Integer.parseInt(remainSeatCnt1_tag.getTextContent());

                                arrival_info.append(locationNo1 + "전 " +
                                        predictTime1 + "분 후 도착 예정 " + remainSeatCnt1 + "석 남음\n");
                                arrival_info.append("도착 예정 정보 없음");
                            }
                        } else {                    // 평상시
                            final int locationNo2 = Integer.parseInt(locationNo2_tag.getTextContent());
                            final String plateNo2 = plateNo2_tag.getTextContent();
                            final int lowPlate2 = Integer.parseInt(lowPlate2_tag.getTextContent());
                            final int predictTime2 = Integer.parseInt(predictTime2_tag.getTextContent());
                            final int remainSeatCnt2 = Integer.parseInt(remainSeatCnt2_tag.getTextContent());

                            final int locationNo1 = Integer.parseInt(locationNo1_tag.getTextContent());
                            final String plateNo1 = plateNo1_tag.getTextContent();
                            final int lowPlate1 = Integer.parseInt(lowPlate1_tag.getTextContent());
                            final int predictTime1 = Integer.parseInt(predictTime1_tag.getTextContent());
                            final int remainSeatCnt1 = Integer.parseInt(remainSeatCnt1_tag.getTextContent());

                            arrival_info.append(locationNo1 + "전 " +
                                    predictTime1 + "분 후 도착 예정 " + remainSeatCnt1 + "석 남음\n");
                            arrival_info.append(locationNo2 + "전 " +
                                    predictTime2 + "분 후 도착 예정 " + remainSeatCnt2 + "석 남음");
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}