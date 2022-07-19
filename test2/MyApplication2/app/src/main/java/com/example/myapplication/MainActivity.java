package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    ArrayList<Song> songList;
    ArrayList<ArrayList<Song>> songArrayList;
    ArrayList<Type> typeArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        String time = StringUtil.getCurrentDateTime();
        activityMainBinding.timeText.setText(time);


        songArrayList = new ArrayList<>();

        //initializeDomesticArrayList();
        initializeTotalArrayList();
        initializeListView();

    }


    public void initializeListView(){
//        SongAdapter songAdapter = new SongAdapter();
//        songAdapter.setSongArrayList(songList);


        ExpandableAdapter expandableAdapter = new ExpandableAdapter();
        expandableAdapter.setParentArrayList(typeArrayList);
        expandableAdapter.setSongArrayList(songArrayList);

        activityMainBinding.songChart.setAdapter(expandableAdapter);

        activityMainBinding.songChart.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Intent detailIntent = new Intent(getApplicationContext(), DetailActivity.class);
                detailIntent.putExtra("id", i1+1);
                startActivity(detailIntent);

                return false;
            }
        });


    }

    private void initializeDomesticArrayList() {
        //activityMainBinding.domesticButton.setTextColor(Color.RED);

        try{
            NetworkTask networkTask = new NetworkTask("http://10.0.2.2:3300/v1/chart/","domestic");
            networkTask.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initializeOverseasArrayList() {

        //activityMainBinding.foreignButton.setTextColor(Color.RED);
        try{
            NetworkTask networkTask = new NetworkTask("http://10.0.2.2:3300/v1/chart/","overseas");
            networkTask.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initializeTotalArrayList(){
        initializeDomesticArrayList();
        initializeOverseasArrayList();

        typeArrayList = new ArrayList<>();
        Type type = new Type();
        type.CountryType = "국내";
        typeArrayList.add(type);
        Type type2 = new Type();
        type2.CountryType = "해외";
        typeArrayList.add(type2);
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
                songList = new ArrayList<>();
                String json= result;

                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray("chartList");

                int index = 0;
                while (index < jsonArray.length()){
                    Song song = gson.fromJson(jsonArray.get(index).toString(),Song.class);
                    songList.add(song);

                    index++;
                }

                songArrayList.add(songList);

                //initializeListView();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}