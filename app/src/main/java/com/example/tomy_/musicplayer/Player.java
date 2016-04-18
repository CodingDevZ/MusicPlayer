package com.example.tomy_.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener {

    static MediaPlayer mp;
    ArrayList<File> mySongs;
    SeekBar seekBar;
    Button btnPlay;
    Button btnForw;
    Button btnBack;
    Button btnNext;
    Button btnPrev;
    int position;
    Uri u;
    Thread updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        if (mp != null) {
            mp.stop();
            mp.release();
        }

        updateSeekBar = new Thread() {
            @Override
            public void run() {
                int totalDuration = mp.getDuration();
                int currentPosition = 0;
                while (currentPosition < totalDuration) {
                    try {
                        sleep(500);
                        currentPosition = mp.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //super.run();
            }
        };

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mySongs = (ArrayList) bundle.getParcelableArrayList("songlist");
        position = bundle.getInt("pos", 0);

        u = Uri.parse(mySongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(), u);
        mp.start();
        seekBar.setMax(mp.getDuration());

        updateSeekBar.start();

        initWidgets();
        setupListeners();

    }

    private void initWidgets() {
        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnForw = (Button) findViewById(R.id.btnForw);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnPrev = (Button) findViewById(R.id.btnPrev);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
    }

    private void setupListeners() {

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnPlay:
                if (mp.isPlaying()) {
                    btnPlay.setText(">");
                    mp.pause();
                } else mp.start();
                btnPlay.setText("||");
                break;
            case R.id.btnForw:
                mp.seekTo(mp.getCurrentPosition() + 5000);
                break;
            case R.id.btnBack:
                mp.seekTo(mp.getCurrentPosition() - 5000);
                break;
            case R.id.btnNext:
                mp.stop();
                mp.release();
                position = (position + 1) & mySongs.size();
                u = Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);
                mp.start();
                seekBar.setMax(mp.getDuration());
                break;
            case R.id.btnPrev:
                mp.stop();
                mp.release();
                position = (position - 1 < 0) ? mySongs.size() - 1 : position - 1;
                /*if (position -1 < 0){
                    position = mySongs.size() - 1;
                }
                else{
                    position = position - 1;
                }*/
                u = Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);
                seekBar.setMax(mp.getDuration());
                mp.start();
                break;
        }
    }
}
