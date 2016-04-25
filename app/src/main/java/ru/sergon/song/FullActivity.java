package ru.sergon.song;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class FullActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full);
        HashMap<String, String> item = (HashMap<String, String>) getIntent()
                .getSerializableExtra("artist");
        TextView genres,track,bio;
        ImageView big = (ImageView) findViewById(R.id.bigImg);
        genres = (TextView) findViewById(R.id.genersText);
        track = (TextView)findViewById(R.id.tracksText);
        bio = (TextView) findViewById(R.id.bioText);
        setTitle(item.get("NAME"));
        Picasso.with(getApplicationContext()).load(item.get("BIG")).into(big);

        if (genres != null) {
            genres.setText(item.get("GENRES"));
        }

        String tr = item.get("ALBUMS") + " альбомов " + item.get("TRACKS") + " песен ";

        if (track != null) {
            track.setText(tr);
        }

        if (bio != null) {
            bio.setText(item.get("DESCRIPTION"));
        }

    }
}
