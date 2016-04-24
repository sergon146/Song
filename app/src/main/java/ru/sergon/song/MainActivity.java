package ru.sergon.song;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static String LOG_TAG = "my_log";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new ParseTask().execute();
        context = getApplicationContext();
    }

    private class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                URL url = new URL("http://cache-novosibrt05.cdn.yandex.net/download.cdn.yandex.net/mobilization-2016/artists.json");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

            String name, link, description, small, big;
            int id, tracks, albums;
            JSONArray genresA;
            JSONObject cover;
            String genres;
            ListView artists;
            SimpleAdapter sAdapter;
            ArrayList<Map<String, Object>> data;
            Map<String, Object> m;

            try {
                JSONArray rootJSON = new JSONArray(new JSONTokener(strJson));
                // 2. перебираем и выводим контакты каждого друга
                data = new ArrayList<>();
                for (int i = 0; i < rootJSON.length(); i++) {
                    JSONObject artist = rootJSON.getJSONObject(i);
                    m = new HashMap<>();
                    Artist item = new Artist();

                    //проверка на наличие id
                    try {
                        id = artist.getInt("id");
                        item.setId(id);
                        Log.d(LOG_TAG, "ID: " + id);
                    } catch (JSONException ignored){}

                    //проверка на наличие имени
                    try {
                        name = artist.getString("name");
                        item.setName(name);
                        Log.d(LOG_TAG, "Name: " + name);
                        m.put("NAME",name);
                    } catch (JSONException ignored){}

                    //проверка на наличие жанров
                    try {
                        genresA = artist.getJSONArray("genres");
                        genres = "";
                        for (int j = 0; j < genresA.length(); j++) {
                            genres += genresA.getString(j) + " ";
                        }
                        Log.d(LOG_TAG, "Genres: " + genres);
                        item.setGenres(genres);
                        m.put("GENRES",genres);
                    } catch (JSONException ignored){}

                    //проверка на наличие песен
                    try {
                        tracks = artist.getInt("tracks");
                        Log.d(LOG_TAG, "Tracks: " + tracks);
                        m.put("TRACK", (tracks+" песен "));
                    } catch (JSONException ignored) {}
                    try {
                        albums = artist.getInt("albums");
                        m.put("ALBUMS", (albums+" альбомов "));
                        Log.d(LOG_TAG, "Albums: " + (albums+" альбомов "));
                    }catch (JSONException ignored){}

                    //проверка на наличие ссылки
                    try {
                        link = artist.getString("link");

                        Log.d(LOG_TAG, "Link: " + link);
                    }catch (JSONException ignored){}


                    //проверка на наличие описания
                    try {
                        description = artist.getString("description");
                        Log.d(LOG_TAG, "Description: " + description);
                        item.setDescription(description);
                    } catch (JSONException ignored){}

                    //проверка на наличие картинок
                    try {
                        cover = artist.getJSONObject("cover");
                        //проверка на наличие маленькой
                        try {
                            small = cover.getString("small");
                            Log.d(LOG_TAG, "Small: " + small);
                            item.setSmall(small);
                            m.put("IMG",Picasso.with(getApplicationContext()).load(small).resize(100, 100) );

                        } catch (JSONException ignored){}
                        //проверка на наличие большой
                        try {
                            big = cover.getString("big");
                            Log.d(LOG_TAG, "Big: " + big);
                            item.setBig(big);
                        } catch (JSONException ignored){}
                    } catch (JSONException ignored){}

                    data.add(m);


                }
                // массив имен атрибутов, из которых будут читаться данные
                String[] from = { "IMG", "NAME", "GENRES","ALBUMS","TRACK" };
                // массив ID View-компонентов, в которые будут вставлять данные
                int[] to = {R.id.ImgSmall,  R.id.name, R.id.genres, R.id.albums, R.id.tracks };

                // создаем адаптер
                sAdapter = new SimpleAdapter(context, data, R.layout.item, from, to);
                artists = (ListView) findViewById(R.id.artistList);
                try {
                    artists.setAdapter(sAdapter);
                } catch (NullPointerException ignored){}

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}