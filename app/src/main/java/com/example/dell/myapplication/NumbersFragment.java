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

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class NumbersFragment extends Fragment {

    private MediaPlayer mediaPlayer ;
    private AudioManager audioManager;


    public NumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_words, container, false);
        // Inflate the layout for this fragment


        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        //array of string
        final ArrayList<word> words = new ArrayList<word>();
        words.add(new word("one","uno",R.drawable.number_one,R.raw.one));
        words.add(new word("two","dos",R.drawable.number_two,R.raw.two));
        words.add(new word("three","tres",R.drawable.number_three,R.raw.three));
        words.add(new word("four","cuatro",R.drawable.number_four,R.raw.four));
        words.add(new word("five","cinco",R.drawable.number_five,R.raw.five));
        words.add(new word("six","seis",R.drawable.number_six,R.raw.six));
        words.add(new word("seven","seite",R.drawable.number_seven,R.raw.seven));
        words.add(new word("eight","ocho",R.drawable.number_eight,R.raw.eight));
        words.add(new word("nine","nueve",R.drawable.number_nine,R.raw.nine));
        words.add(new word("ten","diez",R.drawable.number_ten,R.raw.ten));

        //create a array adapter to store the words and recycle the same views
        //ArrayAdapter can have different data types
        //takes three parameters to create , (context,simple list _item_1 provided by android,arraylist
        wordAdapter wordsAdapter = new wordAdapter(getActivity(),words,R.color.category_numbers);
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
}
