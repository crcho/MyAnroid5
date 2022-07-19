package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.databinding.ActivityDetailBinding;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DetailActivity extends Activity {

    ActivityDetailBinding activityDetailBinding;
    ArrayList<Detail> itemDetailArrayList;
    String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailBinding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(activityDetailBinding.getRoot());

        Intent detailIntent = getIntent();
        int position = detailIntent.getExtras().getInt("id");
        String value = Integer.toString(position);
        //title = detailIntent.getExtras().getString("title");

        try{
            NetworkTask networkTask = new NetworkTask("http://10.0.2.2:3300/v1/chart/detail/",value);
            networkTask.execute();
        }catch (Exception e){
            e.printStackTrace();
        }

        activityDetailBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initializeListView() {

        activityDetailBinding.title.setText(title);
        activityDetailBinding.title.setText(itemDetailArrayList.get(0).title);
        activityDetailBinding.singer.setText(itemDetailArrayList.get(0).singer);
        activityDetailBinding.melodizer.setText(itemDetailArrayList.get(0).melodizer);
        activityDetailBinding.lyricist.setText(itemDetailArrayList.get(0).lyricist);
        activityDetailBinding.genre.setText(itemDetailArrayList.get(0).genre);

        //Drawable 리소스를 String 으로 접근하는 법
//        String str = itemDetailArrayList.get(0).imageFile;
//        //제목으로만 접근해야 해서 substring 으로 뒤에 .jpeg 확장자를 제거 해준다.
//        String result = str.substring(0, str.length()-4);
//        int i = getApplicationContext().getResources().
//                getIdentifier(result,"drawable", getApplicationContext().getPackageName());
//        activityDetailBinding.image.setImageResource(i);

    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        String url;
        String values;
        Gson gson = new Gson();

        NetworkTask(String url, String values){
            this.url = url;
            this.values = values;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress bar를 보여주는 등등의 행위
        }

        @Override
        protected String doInBackground(Void... params) {
            String result;
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.HttpURLConnectionGet(url, values);
            return result; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
        }

        @Override
        protected void onPostExecute(String result) {
            // 통신이 완료되면 호출됩니다.
            // 결과에 따른 UI 수정 등은 여기서 합니다.

            try{
                itemDetailArrayList = new ArrayList<>();
                String json= result;

                JSONObject jsonObject = new JSONObject(json);
                Detail detail = gson.fromJson(jsonObject.get("chart").toString(), Detail.class);
                itemDetailArrayList.add(detail);

                initializeListView();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}