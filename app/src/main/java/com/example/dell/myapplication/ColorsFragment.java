package com.example.dell.myapplication;

import android.app.Fragment;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DELL on 24-02-2017.
 */

public class ColorsFragment extends android.support.v4.app.Fragment{


    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    //onCompletion methor to release the resources
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    //method to implement the audio focus change listner
    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        //if focus is lost permanently or temporarily then play the audio from beginning
                        mediaPlayer.pause();
                        mediaPlayer.seekTo(0);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        mediaPlayer.start();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        releaseMediaPlayer();
                    }
                }
            };


    //constructor
    public ColorsFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.activity_words,container,false);

        //request audio focus of the system's audio service
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        //array of string
        final ArrayList<word> words = new ArrayList<word>();
        words.add(new word("red", "rojo", R.drawable.color_red, R.raw.red));
        words.add(new word("yellow", "amarillo", R.drawable.color_mustard_yellow, R.raw.mustardyellow));
        words.add(new word("green", "verde", R.drawable.color_green, R.raw.green));
        words.add(new word("dusty yellow", "Amarillo polvoriento", R.drawable.color_dusty_yellow, R.raw.dustyyellow));
        words.add(new word("gray", "gris", R.drawable.color_gray, R.raw.gray));
        words.add(new word("brown", "marrón", R.drawable.color_brown, R.raw.brown));
        words.add(new word("purple", "púrpura", R.raw.purple));
        words.add(new word("black", "negro", R.drawable.color_black, R.raw.black));
        words.add(new word("white", "blanco", R.drawable.color_white, R.raw.white));

        //create a array adapter to store the words and recycle the same views
        //ArrayAdapter can have different data types
        //takes three parameters to create , (context,simple list _item_1 provided by android,arraylist
        wordAdapter wordsAdapter = new wordAdapter(getActivity(), words, R.color.category_colors);
        //create a list view
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(wordsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                word w = words.get(position);
                releaseMediaPlayer();
                //request audio focus for playback
                int result = audioManager.requestAudioFocus(onAudioFocusChangeListener,
                        //use the music stream.
                        AudioManager.STREAM_MUSIC,
                        //Request temporary focus
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //start playback


                    mediaPlayer = mediaPlayer.create(getActivity(), w.getAudioid());
                    mediaPlayer.start();

                    //set up our own global completion listener
                    mediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }

        });
return rootView;

    }

    @Override
    public void onStop() {
        super.onStop();
        //relase the resources when stopped
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            //release the resources
            mediaPlayer.release();

            mediaPlayer = null;
            //abandon the audio focus if any...and
            //unregister the onAudioFocusChangeListener so we don't get anymore callbacks
            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }


}
