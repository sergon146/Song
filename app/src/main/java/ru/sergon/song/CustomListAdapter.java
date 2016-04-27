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

import java.util.ArrayList;


public class CustomListAdapter extends ArrayAdapter<String> {

    private String[] mData;
    private final Activity context;
    private final ArrayList<String> nameAr,genresAr,tracksAr, smallAr;
    private int item;

    static class ViewHolder {
        TextView nameV;
        TextView genresV;
        TextView tracksV;
        ImageView smallV;
    }

    @Override
    public String getItem(int i) {
        return mData[i];
    }

    public CustomListAdapter(Activity context, int item, ArrayList<String> nameAr, ArrayList<String> genresAr, ArrayList<String> tracksAr,ArrayList<String> smallAr) {
        super(context, item, nameAr);

        this.item=item;
        this.context=context;
        this.nameAr=nameAr;
        this.genresAr=genresAr;
        this.tracksAr=tracksAr;
        this.smallAr=smallAr;
    }

    public View getView(int position,View view,ViewGroup parent) {
        ViewHolder viewHolder;

        if (view==null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(item, null, true);
            viewHolder = new ViewHolder();
            viewHolder.nameV = (TextView) view.findViewById(R.id.name);
            viewHolder.genresV = (TextView) view.findViewById(R.id.genres);
            viewHolder.tracksV = (TextView) view.findViewById(R.id.tracks);
            viewHolder.smallV = (ImageView) view.findViewById(R.id.imgSmall);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Picasso.with(context)
                .load(smallAr.get(position))
                .into(viewHolder.smallV);
        viewHolder.nameV.setText(nameAr.get(position));
        viewHolder.genresV.setText(genresAr.get(position));
        viewHolder.tracksV.setText(tracksAr.get(position));
        return view;

    }

}