package ru.sergon.song;

/**
 * Created by SerGon on 24.04.2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    Context context;

    ListView artists;
    ArrayList<HashMap<String, String>> item = new ArrayList<HashMap<String, String>>();
    String[] nameAr= new String[1000];
    String[]genresAr= new String[1000];
    String[]tracksAr=  new String[1000];
    String[]smallAr= new String[1000];
    String[] linkAr = new String[1000];
    int count;
    Activity main = this;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
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
                //считываем все строки с сервера
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            //возвращаем полученную строку
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

            String name, link, description, small, big, genres, tr="",al="";
            int id, tracks, albums;
            JSONArray genresA;
            JSONObject cover,artist;

            HashMap<String, String> field;


            try {
                //получаем искходный массив объектов
                JSONArray rootJSON = new JSONArray(new JSONTokener(strJson));
                //перебираем все объекты из json-массива
                count=-1;
                for (int i = 0; i < rootJSON.length(); i++) {
                    count++;
                    //извлекаем каждый объект
                    artist = rootJSON.getJSONObject(i);
                    field = new HashMap<>();

                    //проверка на наличие id
                    try {
                        id = artist.getInt("id");
                        field.put("ID",String.valueOf(id));
                    } catch (JSONException ignored){}

                    //проверка на наличие имени
                    try {
                        name = artist.getString("name");
                        field.put("NAME", name);
                        nameAr[count]=name;
                    } catch (JSONException ignored){}

                    //проверка на наличие жанров
                    try {
                        genresA = artist.getJSONArray("genres");
                        genres = "";
                        for (int j = 0; j < genresA.length(); j++) {
                            if (j==0) genres += genresA.getString(j);
                            else genres +=  ", " + genresA.getString(j) ;

                        }
                        field.put("GENRES", genres);
                        genresAr[count]=genres;
                    } catch (JSONException ignored){}

                    //проверка на наличие песен
                    tracks=0;

                    try {
                        tracks = artist.getInt("tracks");
                        switch (tracks%100){
                            case 10:
                            case 11:
                            case 12:
                            case 13:
                            case 14:
                            case 15:
                            case 16:
                            case 17:
                            case 18:
                            case 19:
                            case 20: tr=" песен";
                                break;
                            default:
                                switch (tracks % 10) {
                                    case 1:
                                        tr = " песня ";
                                        break;
                                    case 2:
                                    case 3:
                                    case 4:
                                        tr = " песни";
                                        break;
                                    case 5:
                                    case 6:
                                    case 7:
                                    case 8:
                                    case 9:
                                        tr = " песен";
                                        break;
                                    case 0:
                                        tr = " песен";
                                        break;
                                }
                                break;

                        }

                        field.put("TRACKS"," "+String.valueOf(tracks)+tr);
                    } catch (JSONException ignored) {}
                    albums=0;
                    try {
                        albums = artist.getInt("albums");
                        switch (albums%100){
                            case 10:
                            case 11:
                            case 12:
                            case 13:
                            case 14:
                            case 15:
                            case 16:
                            case 17:
                            case 18:
                            case 19:
                            case 20:al=" альбомов ";
                                break;
                            default:
                                switch (albums%10) {
                                    case 1:
                                        al = " альбом ";
                                        break;
                                    case 2:
                                    case 3:
                                    case 4:
                                        al = " альбома ";
                                        break;
                                    case 5:
                                    case 6:
                                    case 7:
                                    case 8:
                                    case 9:
                                        al = " альбомов ";
                                        break;
                                    case 0:
                                        al = " альбомов ";
                                        break;
                                }
                                break;

                        }
                        field.put("ALBUMS",String.valueOf(albums)+al+"•");
                    }catch (JSONException ignored){}

                    tracksAr[count]=String.valueOf(albums)+al+", "+ String.valueOf(tracks)+tr;

                    //проверка на наличие ссылки
                    try {
                        link = artist.getString("link");
                        field.put("LINK",link);
                        linkAr[count]=link;
                    }catch (JSONException ignored){}


                    //проверка на наличие описания
                    try {
                        description = artist.getString("description");
                        field.put("DESCRIPTION",description);
                    } catch (JSONException ignored){}

                    //проверка на наличие картинок
                    try {
                        cover = artist.getJSONObject("cover");
                        //проверка на наличие маленькой
                        try {
                            small = cover.getString("small");
                            field.put("SMALL",small);
                            smallAr[count]=small;


                        } catch (JSONException ignored){}
                        //проверка на наличие большой
                        try {
                            big = cover.getString("big");
                            field.put("BIG",big);
                        } catch (JSONException ignored){}
                    } catch (JSONException ignored){}
                    item.add(field);


                }
                try {
                    CustomListAdapter adapter = new CustomListAdapter(main, nameAr, genresAr, tracksAr, smallAr, linkAr);
                    artists = (ListView) findViewById(R.id.artistList);
                    if (artists != null) {
                        artists.setAdapter(adapter);
                    }
                    progressBar.setVisibility(ProgressBar.INVISIBLE);

                    artists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            Intent intent = new Intent(MainActivity.this, FullActivity.class);
                            intent.putExtra("artist", item.get(position));
                            startActivity(intent);
                        }

                    });
                } catch (NullPointerException ignored){}



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void onProgressClick(View view){
        Toast.makeText(context,"Подождите, идёт загрузка!", Toast.LENGTH_LONG).show();
    }

}


