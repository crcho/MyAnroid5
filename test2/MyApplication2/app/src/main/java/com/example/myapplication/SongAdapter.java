package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.myapplication.databinding.ItemSongBinding;

import java.util.ArrayList;

public class SongAdapter extends BaseAdapter {
    ArrayList<Song> songArrayList;
    ItemSongBinding itemSongBinding ;

    @Override
    public int getCount() {
        return songArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return songArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        itemSongBinding = ItemSongBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        itemSongBinding.rank.setText(String.valueOf(songArrayList.get(position).rank));
        itemSongBinding.songName.setText(songArrayList.get(position).title);
        itemSongBinding.singer.setText(songArrayList.get(position).singer);


        //Drawable 리소스를 String 으로 접근하는 법
        String str = songArrayList.get(position).imageUrl;
        //제목으로만 접근해야 해서 substring 으로 뒤에 .jpeg 확장자를 제거 해준다.
        String result = StringUtil.getFileNameWithoutExt(str);
        int i = parent.getContext().getResources().
                getIdentifier(result,"drawable", parent.getContext().getPackageName());
        itemSongBinding.image.setImageResource(i);


        return itemSongBinding.getRoot();
    }

    public void setSongArrayList(ArrayList<Song> songArrayList){
        this.songArrayList = songArrayList;
        notifyDataSetChanged();
    }
}
