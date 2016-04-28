package ru.sergon.song.activity;

/**
 * Created by SerGon on 25.04.2016.
 */
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import ru.sergon.song.R;
import ru.sergon.song.model.Artist;


public class FullActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_full);
        String artistString = getIntent().getStringExtra("artist");
        ProgressBar progressBarFull = (ProgressBar) findViewById(R.id.progressBarFull);
        progressBarFull.setVisibility(ProgressBar.VISIBLE);



        Artist artist;
        try {
            ObjectMapper mapper = new ObjectMapper();
            artist = mapper.readValue(artistString, Artist.class);


            getSupportActionBar().setDisplayHomeAsUpEnabled(true);



            TextView genres, track, bio, link;

            ImageView big = (ImageView) findViewById(R.id.bigImg);
            genres = (TextView) findViewById(R.id.genersText);
            track = (TextView) findViewById(R.id.tracksText);
            bio = (TextView) findViewById(R.id.bioText);
            link = (TextView) findViewById(R.id.linkText);

            //устанавливаем полученные значения
            setTitle(artist.getName());

            Picasso.with(getApplicationContext())
                    .load(artist.getCover().getBig())
                    .into(big);
            genres.setText(artist.style());
            track.setText(artist.music());
            bio.setText(artist.getDescription());
            link.setText(artist.getLink());
            progressBarFull.setVisibility(ProgressBar.INVISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view){
        TextView link = (TextView) findViewById(R.id.linkText);
        Intent browserIntent = null;
        if (link != null) {
            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link.getText().toString()));
        }
        startActivity(browserIntent);
    }

    //кнопка "Домой" - как кнопка "Назад" чтобы не скролить заного вниз листа
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return(super.onOptionsItemSelected(item));
    }
}
