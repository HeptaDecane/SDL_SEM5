package com.example.sdlmediaplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    Button btn_next;
    Button btn_prev;
    Button btn_pause;
    TextView songLabel;
    SeekBar songSeekBar;
    String sname;
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File>mySong;
    Thread updateSeekBar;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        getSupportActionBar().setTitle("Now Playing");
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         getSupportActionBar()  .setDisplayShowHomeEnabled(true);
        btn_next=(Button)findViewById(R.id.nextButton);
        btn_prev=(Button)findViewById(R.id.prevButton);
        btn_pause=(Button)findViewById(R.id.pause);
        songLabel=(TextView)findViewById(R.id.songLabel);
        songSeekBar=(SeekBar)findViewById(R.id.seekBar);
        updateSeekBar=new Thread(){
            @Override
            public void run() {
                int totalDuration=mediaPlayer.getDuration();
                int currentPosition=0;
                while (currentPosition<totalDuration){
                    try {
                        sleep(500)  ;
                        currentPosition=mediaPlayer.getCurrentPosition();
                        songSeekBar.setProgress(currentPosition);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        if(mediaPlayer!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        mySong=(ArrayList)bundle.getParcelableArrayList("songs");
        sname=mySong.get(position).getName().toString();
        String songName=i.getStringExtra("songName");
        songLabel.setText(songName);
        songLabel.setSelected(true);
        position=bundle.getInt("pos",0);
        Uri u= Uri.parse(mySong.get(position).toString());
        mediaPlayer=MediaPlayer.create(getApplicationContext(),u);
        songSeekBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.start();
        updateSeekBar.start();
        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(songSeekBar.getProgress());
            }
        });
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songSeekBar.setMax(mediaPlayer.getDuration());
                if(mediaPlayer.isPlaying())
                {
                    btn_pause.setBackgroundResource(R.drawable.icon_play);
                    mediaPlayer.pause();
                }
                else
                {
                    btn_pause.setBackgroundResource(R.drawable.icon_pause);
                    mediaPlayer.start();
                }
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = (position + 1) % mySong.size();
                Uri u = Uri.parse(mySong.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                sname = mySong.get(position).getName().toString();
                songSeekBar.setMax(mediaPlayer.getDuration());
                songSeekBar.setProgress(0);
                songLabel.setText(sname);
                mediaPlayer.start();

            }
        });
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position=((position-1)<0)? (mySong .size()-1):position-1;
                Uri u=Uri.parse(mySong.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                sname = mySong.get(position).getName().toString();
                songSeekBar.setProgress(0);
                songLabel.setText(sname);
                mediaPlayer.start();
                songSeekBar.setMax(mediaPlayer.getDuration());


            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}