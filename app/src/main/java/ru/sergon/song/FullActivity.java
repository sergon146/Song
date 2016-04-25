package ru.sergon.song;

/**
 * Created by SerGon on 25.04.2016.
 */
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class FullActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ProgressBar progressBarFull = (ProgressBar) findViewById(R.id.progressBarFull);
        progressBarFull.setVisibility(ProgressBar.VISIBLE);
        HashMap<String, String> item = (HashMap<String, String>) getIntent()
                .getSerializableExtra("artist");
        TextView genres,track,bio,link;
        ImageView big = (ImageView) findViewById(R.id.bigImg);
        genres = (TextView) findViewById(R.id.genersText);
        track = (TextView)findViewById(R.id.tracksText);
        bio = (TextView) findViewById(R.id.bioText);
        link = (TextView) findViewById(R.id.linkText);
        setTitle(item.get("NAME"));
        Picasso.with(getApplicationContext())
            .load(item.get("BIG"))
            .into(big);

        if (genres != null) {
            genres.setText(item.get("GENRES"));
        }

        String tr = item.get("ALBUMS")  + item.get("TRACKS");

        if (track != null) {
            track.setText(tr);
        }

        if (bio != null) {
            bio.setText(item.get("DESCRIPTION"));
        }
        if (link!=null){
            link.setText(item.get("LINK"));
        }
        progressBarFull.setVisibility(ProgressBar.INVISIBLE);
    }

    public void onClick(View view){
        TextView link = (TextView) findViewById(R.id.linkText);
        Intent browserIntent = null;
        if (link != null) {
            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link.getText().toString()));
        }
        startActivity(browserIntent);
    }
}
