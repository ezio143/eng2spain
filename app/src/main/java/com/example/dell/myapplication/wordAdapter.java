package com.example.dell.myapplication;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.attr.resource;
import static android.R.string.no;

/**
 * Created by DELL on 03-02-2017.
 */

public class wordAdapter extends ArrayAdapter<word> {
    private int color_id;

    public wordAdapter(Activity context, ArrayList<word> resource, int category) {
        super(context,0, resource);
        this.color_id=category;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.my_listitem,parent,false);
        }

        word currentWord = getItem(position);

        TextView defaultText = (TextView) listItemView.findViewById(R.id.default_textview);
        defaultText.setText(currentWord.getmDefaultTranslation());

        TextView spanishText = (TextView) listItemView.findViewById(R.id.spanish_textview);
        spanishText.setText(currentWord.getMspanishTranslation());
        ImageView iconView = (ImageView) listItemView.findViewById(R.id.word_icon);
            if(currentWord.hasImage()) {
                iconView.setImageResource(currentWord.getId());
                iconView.setVisibility(View.VISIBLE);

            }
        else{
                iconView.setVisibility(View.GONE);
            }

        View textContainer = listItemView.findViewById(R.id.text_view_layout);
        int color = ContextCompat.getColor(getContext(),color_id);
        textContainer.setBackgroundColor(color);
    return listItemView;
    }
}
