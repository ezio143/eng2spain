package com.example.dell.myapplication;

import static android.R.attr.id;

/**
 * Created by DELL on 03-02-2017.
 */

public class word {

    private String mDefaultTranslation;
    private  String mspanishTranslation;
    private int id = NO_IMAGE_PROVIDED;
    private static  final int NO_IMAGE_PROVIDED =-1;
    private int audioid;

    //constructors
    public word(String defaultTranslation,String spanishTranslation,int audioid){
        mDefaultTranslation = defaultTranslation;
        mspanishTranslation = spanishTranslation;
        this.audioid = audioid;
    }



    //constructor two
    public word(String defaultTranslation,String spanishTranslation,int resourceid,int audioid){
        mDefaultTranslation = defaultTranslation;
        mspanishTranslation = spanishTranslation;
        this.id = resourceid;
        this.audioid = audioid;
    }

    public int getAudioid() {
        return audioid;
    }

    public void setAudioid(int audioid) {
        this.audioid = audioid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getmDefaultTranslation() {
        return mDefaultTranslation;
    }

    public String getMspanishTranslation() {
        return mspanishTranslation;
    }

    public void setmDefaultTranslation(String mDefaultTranslation) {
        this.mDefaultTranslation = mDefaultTranslation;
    }

    public void setMspanishTranslation(String mspanishTranslation) {
        this.mspanishTranslation = mspanishTranslation;
    }

//return whether the word has image or not
        public boolean hasImage(){
            return (id != NO_IMAGE_PROVIDED);
        }
}
