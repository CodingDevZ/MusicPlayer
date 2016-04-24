package com.example.tomy_.musicplayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lvPlaylist;
    TextView textView;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean Theme2= preferences.getBoolean("White", false);
        boolean Theme1= preferences.getBoolean("Black", false);

        if(Theme2) {
            setTheme(R.style.White);
        }
        else if(Theme1) {
            setTheme(R.style.Black);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initWidgets();

        final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];
        for (int i = 0; i < mySongs.size(); i++) {
            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.song_layout, R.id.textView, items);
        lvPlaylist.setAdapter(adapter);

        lvPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), Player.class);

                i.putExtra("pos", position);
                i.putExtra("songlist", mySongs);
                startActivity(i);
            }
        });
    }

    private void initWidgets() {

        lvPlaylist = (ListView) findViewById(R.id.lvPlaylist);
        textView = (TextView) findViewById(R.id.textView);
        Toolbar topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);

    }

    public ArrayList<File> findSongs(File root) {
        ArrayList<File> arrayList = new ArrayList<File>();
        File[] files = root.listFiles();
        if (files != null) {
            for (File singleFile : files) {
                if (singleFile.isDirectory() && !singleFile.isHidden()) {
                    arrayList.addAll(findSongs(singleFile));
                } else {
                    if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {
                        arrayList.add(singleFile);
                    }
                }
            }

        }
        return arrayList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent2 = new Intent(MainActivity.this,Preference.class);
            startActivity(intent2);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}





