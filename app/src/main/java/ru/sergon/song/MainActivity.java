package ru.sergon.song;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static String LOG_TAG = "my_log";
    Context context;

    SimpleAdapter sAdapter;
    ImageLoader imageLoader;
    ArrayList<HashMap<String, String>> item = new ArrayList<HashMap<String, String>>();
    int count=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new ParseTask().execute();
        context = getApplicationContext();
        imageLoader = ImageLoader.getInstance();
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


            ArrayList<Map<String, Object>> data;
            Map<String, Object> m;
            HashMap<String, String> field;

            try {
                JSONArray rootJSON = new JSONArray(new JSONTokener(strJson));
                // 2. перебираем и выводим контакты каждого друга
                data = new ArrayList<>();
                for (int i = 0; i < rootJSON.length(); i++) {
                    count++;
                    JSONObject artist = rootJSON.getJSONObject(i);
                    m = new HashMap<>();
                    field = new HashMap<>();

                    //проверка на наличие id
                    try {
                        id = artist.getInt("id");
                        field.put("ID",String.valueOf(id));
                        Log.d(LOG_TAG, "ID: " + id);
                    } catch (JSONException ignored){}

                    //проверка на наличие имени
                    try {
                        name = artist.getString("name");
                        Log.d(LOG_TAG, "Name: " + name);
                        field.put("NAME", name);
                        m.put("NAME",name);
                    } catch (JSONException ignored){}

                    //проверка на наличие жанров
                    try {
                        genresA = artist.getJSONArray("genres");
                        genres = "";
                        for (int j = 0; j < genresA.length(); j++) {
                            if (j==0) genres += genresA.getString(j);
                            else genres +=  ", " + genresA.getString(j) ;

                        }
                        Log.d(LOG_TAG, "Genres: " + genres);
                        m.put("GENRES",genres);
                        field.put("GENRES", genres);
                    } catch (JSONException ignored){}

                    //проверка на наличие песен
                    try {
                        tracks = artist.getInt("tracks");
                        Log.d(LOG_TAG, "Tracks: " + tracks);
                        field.put("TRACKS",String.valueOf(tracks));
                        m.put("TRACK", (tracks+" песен "));
                    } catch (JSONException ignored) {}

                    try {
                        albums = artist.getInt("albums");
                        m.put("ALBUMS", (albums+" альбомов "));
                        field.put("ALBUMS",String.valueOf(albums));
                        Log.d(LOG_TAG, "Albums: " + (albums+" альбомов "));
                    }catch (JSONException ignored){}

                    //проверка на наличие ссылки
                    try {
                        link = artist.getString("link");
                        Log.d(LOG_TAG, "Link: " + link);
                        field.put("LINK",link);
                    }catch (JSONException ignored){}


                    //проверка на наличие описания
                    try {
                        description = artist.getString("description");
                        Log.d(LOG_TAG, "Description: " + description);
                        field.put("DESCRIPTION",description);
                    } catch (JSONException ignored){}

                    //проверка на наличие картинок
                    try {
                        cover = artist.getJSONObject("cover");
                        //проверка на наличие маленькой
                        try {
                            small = cover.getString("small");
                            Log.d(LOG_TAG, "Small: " + small);
                            field.put("SMALL",small);
                            m.put("IMG",small);
//                           ImageView img = (ImageView) findViewById(R.id.imgSmall);
//
//                            imageLoader.displayImage(small, img);


                        } catch (JSONException ignored){}
                        //проверка на наличие большой
                        try {
                            big = cover.getString("big");
                            Log.d(LOG_TAG, "Big: " + big);
                            field.put("BIG",big);
                        } catch (JSONException ignored){}
                    } catch (JSONException ignored){}
                    item.add(field);
                    data.add(m);


                }
                // массив имен атрибутов, из которых будут читаться данные
                String[] from = { "IMG", "NAME", "GENRES","ALBUMS","TRACK" };
                // массив ID View-компонентов, в которые будут вставлять данные
                int[] to = {R.id.imgSmall,  R.id.name, R.id.genres, R.id.albums, R.id.tracksText};

                // создаем адаптер
                sAdapter = new SimpleAdapter(context, data, R.layout.item, from, to);
                artists = (ListView) findViewById(R.id.artistList);
                try {
                    assert artists != null;
                    artists.setAdapter(sAdapter);
                } catch (NullPointerException ignored){}


                artists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent intent = new Intent(MainActivity.this, FullActivity.class);
                        intent.putExtra("artist", item.get(position));
                        startActivity(intent);
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }



        }




    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("LOG_TAG", "Error getting bitmap", e);
        }
        return bm;
    }
}


