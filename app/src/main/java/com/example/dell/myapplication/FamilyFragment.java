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
public class FamilyFragment extends Fragment {


    public FamilyFragment() {
        // Required empty public constructor
    }

    private MediaPlayer mediaPlayer ;
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

        audioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
        //array of string
        final ArrayList<word> words = new ArrayList<word>();
        words.add(new word("Father","Padre",R.drawable.family_father,R.raw.padre));
        words.add(new word("Mother","Madre",R.drawable.family_mother,R.raw.madre));
        words.add(new word("GrandFather","Abuelo",R.drawable.family_grandfather,R.raw.grandfather));
        words.add(new word("GrandMother","Abuela",R.drawable.family_grandmother,R.raw.grandmother));
        words.add(new word("Brother","Hermano",R.drawable.family_older_brother,R.raw.brother));
        words.add(new word("Sister","hermana",R.drawable.family_older_sister,R.raw.sister));
        words.add(new word("son","primo",R.drawable.family_son,R.raw.son));
        words.add(new word("daughter","prima",R.drawable.family_daughter,R.raw.daughter));
        words.add(new word("Friend","amigo",R.raw.friend));

        //create a array adapter to store the words and recycle the same views
        //ArrayAdapter can have different data types
        //takes three parameters to create , (context,simple list _item_1 provided by android,arraylist
        wordAdapter wordsAdapter = new wordAdapter(getActivity(),words, R.color.category_family);
        //create a list view
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(wordsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                word w = words.get(position);

                releaseMediaPlayer();//release before it is created

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
