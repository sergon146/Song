package ru.sergon.song.activity;

/**
 * Created by SerGon on 24.04.2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.sergon.song.R;
import ru.sergon.song.recycler.RVAdapter;
import ru.sergon.song.recycler.RecyclerItemClickListener;
import ru.sergon.song.model.Artist;

public class MainActivity extends AppCompatActivity {

    public Context context;

    RecyclerView artistsView;
    public Artist[] artists;
    Activity main = this;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//      отображаем индикатор загрузки
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        context = getApplicationContext();
//      вызываем парсер
        new ParseTask().execute();
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
            ObjectMapper mapper = new ObjectMapper();
            //считываем данные из строки в класс
            try {
                artists = mapper.readValue(strJson,
                        Artist[].class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            artistsView=(RecyclerView)findViewById(R.id.artistList);
            LinearLayoutManager llm = new LinearLayoutManager(main);
            artistsView.setLayoutManager(llm);
            //фиксируем размер элементов для увеличения производительности
            artistsView.setHasFixedSize(true);
            initializeAdapter();
            //скрываем индикатор загрузки после вывода
            progressBar.setVisibility(ProgressBar.INVISIBLE);

            //обработчик нажатий на элемент
            artistsView.addOnItemTouchListener(
                    new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {
                            Intent intent = new Intent(MainActivity.this, FullActivity.class);
                            ObjectMapper mapper = new ObjectMapper();
                            String artistString = null;
                            try {
                                artistString = mapper.writeValueAsString(artists[position]);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //передаём объект как json-строку
                            intent.putExtra("artist", artistString);
                            startActivity(intent);
                        }
                    })
            );



        }
        private void initializeAdapter(){
            RVAdapter adapter = new RVAdapter(main, context, artists);
            artistsView.setAdapter(adapter);
        }


    }


//вывод сообщения при нажатии на индикатор загрузки
    public void onProgressClick(View view){
        Toast.makeText(context,"Подождите, идёт загрузка!", Toast.LENGTH_LONG).show();
    }
}


