package ru.sergon.song;

/**
 * Created by SerGon on 25.04.2016.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] nameAr,genresAr,tracksAr, smallAr, linkAr;

    public CustomListAdapter(Activity context, String[] nameAr,String[] genresAr,String[] tracksAr,String[] smallAr, String[] linkAr) {
        super(context, R.layout.item, nameAr);

        this.context=context;
        this.nameAr=nameAr;
        this.genresAr=genresAr;
        this.tracksAr=tracksAr;
        this.smallAr=smallAr;
        this.linkAr=linkAr;
    }

    public View getView(int position,View view,ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.item, null,true);

        //установанивем тексты и картинку в элемент списка
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imgSmall);
        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView genres = (TextView) rowView.findViewById(R.id.genres);
        TextView tracks = (TextView) rowView.findViewById(R.id.tracks);
        //загружаем картинку по ссылке, если картинка уже была загружена, берём её из кэша
        Picasso.with(context)
                .load(smallAr[position])
                .into(imageView);
        name.setText(nameAr[position]);
        genres.setText(genresAr[position]);
        tracks.setText(tracksAr[position]);
        return rowView;

    }
}