package com.example.dell.myapplication;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhrasesFragment extends Fragment {


    public PhrasesFragment() {
        // Required empty public constructor
    }

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };


    //method to implement the audio focus change listner
    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener(){
                public void onAudioFocusChange(int focusChange){
                    if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ){
                        //if focus is lost permanently or temporarily then play the audio from beginning
                        mediaPlayer.pause();
                        mediaPlayer.seekTo(0);
                    }
                    else if(focusChange == AudioManager.AUDIOFOCUS_GAIN){
                        mediaPlayer.start();
                    }
                    else if(focusChange == AudioManager.AUDIOFOCUS_LOSS){
                        releaseMediaPlayer();
                    }
                }
            };

    private void releaseMediaPlayer(){
        if(mediaPlayer != null){
            //release the resources
            mediaPlayer.release();

            mediaPlayer = null;
            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.activity_words,container,false);

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        //array of string
        final ArrayList<word> words = new ArrayList<word>();
        words.add(new word("Hello","Hola",R.raw.hello));
        words.add(new word("si","yes",R.raw.yes));
        words.add(new word("How are you?","cómo estás",R.raw.hru));
        words.add(new word("I am fine thankyou","Estoy bien gracias",R.raw.thankyou));
        words.add(new word("what are your doing","Qué estás haciendo",R.raw.whtdoing));
        words.add(new word("Thank you","gracias",R.raw.thankyou));
        words.add(new word("did you had dinner","Tu cenaste",R.raw.dinner));
        words.add(new word("I like you","me gustas",R.raw.like));
        words.add(new word("come","ven",R.raw.come));
        words.add(new word("go","ir",R.raw.go));
        //create a array adapter to store the words and recycle the same views
        //ArrayAdapter can have different data types
        //takes three parameters to create , (context,simple list _item_1 provided by android,arraylist
        wordAdapter wordsAdapter = new wordAdapter(getActivity(),words, R.color.category_phrases);
        //create a list view
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(wordsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                word w = words.get(position);
                releaseMediaPlayer();
                int result = audioManager.requestAudioFocus(onAudioFocusChangeListener,
                        //use the music stream.
                        AudioManager.STREAM_MUSIC,
                        //Request temporary focus
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //start playback


                    mediaPlayer = mediaPlayer.create(getActivity(), w.getAudioid());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });


        return rootView;
    }

}
