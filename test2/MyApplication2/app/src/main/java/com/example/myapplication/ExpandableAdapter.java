package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.example.myapplication.databinding.ItemSongBinding;
import com.example.myapplication.databinding.ItemTypeBinding;

import java.util.ArrayList;

public class ExpandableAdapter extends BaseExpandableListAdapter {
    ItemSongBinding itemSongBinding;
    ItemTypeBinding itemTypeBinding;
    ArrayList<ArrayList<Song>> songArrayList;
    ArrayList<Type> typeArrayList;

    @Override
    public int getGroupCount() {
        return typeArrayList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return songArrayList.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return typeArrayList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int position, boolean b, View view, ViewGroup parent) {
        itemTypeBinding = ItemTypeBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        itemTypeBinding.typeName.setText(typeArrayList.get(position).CountryType);
        return itemTypeBinding.getRoot();
    }

    @Override
    public View getChildView(int position, int i1, boolean b, View view, ViewGroup parent) {
        itemSongBinding = ItemSongBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        itemSongBinding.rank.setText(String.valueOf(songArrayList.get(position).get(i1).rank));
        itemSongBinding.songName.setText(songArrayList.get(position).get(i1).title);
        itemSongBinding.singer.setText(songArrayList.get(position).get(i1).singer);


        //Drawable 리소스를 String 으로 접근하는 법
        String str = songArrayList.get(position).get(i1).imageUrl;
        //제목으로만 접근해야 해서 substring 으로 뒤에 .jpeg 확장자를 제거 해준다.
        String result = StringUtil.getFileNameWithoutExt(str);
        int i = parent.getContext().getResources().
                getIdentifier(result,"drawable", parent.getContext().getPackageName());
        itemSongBinding.image.setImageResource(i);


        return itemSongBinding.getRoot();
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void setSongArrayList(ArrayList<ArrayList<Song>> songArrayList){
        this.songArrayList = songArrayList;
        notifyDataSetChanged();
    }

    public void setParentArrayList(ArrayList<Type> typeArrayList){
        this.typeArrayList = typeArrayList;
        notifyDataSetChanged();
    }
}
