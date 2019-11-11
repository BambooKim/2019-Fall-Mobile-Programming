package com.bambookim.kyungheebus;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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

public class StationListTask extends AsyncTask<String, String, String> {
    public final static String TAG = "StationListTask";

    ProgressDialog progressDialog;
    private Context mContext;
    private TextView textView;

    public StationListTask(Context context, TextView textView) {
        mContext = context;
        this.textView = textView;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("정류장 목록 가져오는 중...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(R.style.Widget_AppCompat_ProgressBar_Horizontal);

        progressDialog.show();

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... args) {
        try {
            HttpURLConnection con = null;
            BufferedReader reader = null;

            try{
                URL url = new URL("http://openapi.gbis.go.kr/ws/rest/busrouteservice/" + "\n" +
                                                "station?serviceKey=1234567890&routeId=" + args[0]);

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

        progressDialog.dismiss();


        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(result));

            Document doc = builder.parse(inputSource);

            Element response = doc.getDocumentElement();
            NodeList msgBody = response.getElementsByTagName("msgBody");
            Element Body = (Element) msgBody.item(0);
            NodeList StationList = Body.getElementsByTagName("busRouteStationList");

            textView.setText("");

            // item 태그 객체의 수만큼 반복한다.
            for (int i = 0; i < StationList.getLength(); i++) {
                // i번째 item 태그 객체를 추출한다.
                Element item_tag = (Element) StationList.item(i);

                // item태그 안의 태그들을 얻어온다.
                NodeList stationSeq_list = item_tag.getElementsByTagName("stationSeq");
                NodeList stationId_list = item_tag.getElementsByTagName("stationId");
                NodeList stationName_list = item_tag.getElementsByTagName("stationName");

                // 0번째 태그 객체를 얻어온다.
                Element stationSeq_tag = (Element) stationSeq_list.item(0);
                Element stationId_tag = (Element) stationId_list.item(0);
                Element stationName_tag = (Element) stationName_list.item(0);

                // 문자열 데이터 추출
                final String stationSeq = stationSeq_tag.getTextContent();
                final String stationId = stationId_tag.getTextContent();
                final String stationName = stationName_tag.getTextContent();

                textView.append(stationSeq + " ");
                textView.append(stationId + "  ");
                textView.append(stationName + "\n");
                textView.append("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}