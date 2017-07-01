package com.example.js.test;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private URL Url;
    private String strUrl,strCookie,result;
    NetworkTask task;
    ArrayList<String> items;
    ArrayAdapter adapter;
    ListView listview;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 빈 데이터 리스트 생성.
        items = new ArrayList<String>() ;
        // ArrayAdapter 생성. 아이템 View를 선택(single choice)가능하도록 만듦.
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        listview = (ListView) findViewById(R.id.list_item) ;
        listview.setAdapter(adapter) ;







    }

    public class NetworkTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로딩테스트~~~~");

            // show dialog
            asyncDialog.show();

            super.onPreExecute();
            //http://ip.jsontest.com
            //http://ebidkorea.com/do/board/notice/list?page=1&limit=30&searchType=1
            strUrl = "http://ebidkorea.com/do/board/notice/list?limit=20&searchType=1"; //탐색하고 싶은 URL이다.
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{

                HttpURLConnection   conn    = null;

                OutputStream os   = null;
                InputStream is   = null;
                ByteArrayOutputStream baos = null;



                String url = strUrl + "&page=" + Integer.toString(page);
                Url = new URL(url);  // URL화 한다.
                conn = (HttpURLConnection) Url.openConnection(); // URL을 연결한 객체 생성.
                conn.setRequestMethod("GET"); // get방식 통신
                //conn.setDoOutput(true);       // 쓰기모드 지정
                //conn.setDoInput(true);        // 읽기모드 지정
                conn.setUseCaches(false);     // 캐싱데이터를 받을지 안받을지
                conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정

                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type", "application/json");
                //conn.setRequestProperty("Accept", "application/json");

                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                //conn.setRequestProperty("Host", "ebidkorea.com");


                //conn.connect();
                // 타입길이 설정(Request Body 전달시 Data Type의 길이를 정함.)
                //conn.setRequestProperty("Content-Length", "length");

                // User-Agent 값 설정
               // conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");






                JSONObject job = new JSONObject();
                try{
                    job.put("phoneNum", "01000000000");
                    job.put("name", "test name");
                    job.put("address", "test address");
                }catch(JSONException e){
                    e.printStackTrace();
                }

                //os = conn.getOutputStream();
                //os.write(job.toString().getBytes());
                //os.flush();
                //os.close();


                String response;

                int responseCode = conn.getResponseCode();



                Log.i("다음다음","============="+responseCode + "============" +HttpURLConnection.HTTP_OK);
                if(responseCode == HttpURLConnection.HTTP_OK) {

                    is = conn.getInputStream();
                    baos = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    byte[] byteData = null;
                    int nLength = 0;
                    while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                        baos.write(byteBuffer, 0, nLength);
                    }
                    byteData = baos.toByteArray();

                    response = new String(byteData);

                    try{
                        JSONObject responseJSON = new JSONObject(response);
                        JSONArray list = responseJSON.getJSONArray("noticelist");
                        for(int i=0; i < list.length(); i++) {
                            JSONObject jObject = list.getJSONObject(i);  // JSONObject 추출
                            String starred = jObject.getString("starred");

                            if(!starred.equals("1")){
                                String title = jObject.getString("title");
                                Log.i("title====", title);
                                items.add(title);
                            }

                          //adapter.notifyDataSetChanged();

                        }

                        page++;


                        //int count;
                       // count = adapter.getCount();

                        // 아이템 추가.


                        // listview 갱신
                       // adapter.notifyDataSetChanged();

                    }catch(JSONException e){
                        e.printStackTrace();
                    }



                    Log.i("js====", "DATA response = " + response);
                }


                conn.disconnect();


            }catch(MalformedURLException   exception) {
                exception.printStackTrace();
            }catch(IOException io){
                io.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
            asyncDialog.dismiss();
            super.onPostExecute(aVoid);


            System.out.println(result);
        }
    }
    public void click1(View v){



        task = new NetworkTask();
        task.execute();
//     Log.i("다음다음","=============");
       // Intent myIntenet = new Intent(getApplicationContext(), Main2Activity.class);
      //  startActivityForResult(myIntenet,0);

        //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // startActivity(intent);
    }





    }
