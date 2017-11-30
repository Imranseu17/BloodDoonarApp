package com.example.root.blooddoonarapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by root on 11/5/17.
 */

public class DonarListAdapter extends BaseAdapter{
    private Context context;
    private  int layout;
    private java.util.concurrent.CopyOnWriteArrayList<BloodDonar> bloodDonars;
    Viewholder viewholder;





    public DonarListAdapter(Context context, int layout,
                            java.util.concurrent.CopyOnWriteArrayList<BloodDonar> bloodDonars) {
        this.context = context;
        this.layout = layout;
        this.bloodDonars = bloodDonars;
    }

    @Override
    public int getCount() {
        return bloodDonars.size();
    }

    @Override
    public Object getItem(int i) {
        return bloodDonars.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class  Viewholder{
        ImageView imageView;
        TextView name,group,number;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup){
        View row = view;
       viewholder = new Viewholder();
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);
            viewholder.name = row.findViewById(R.id.name);
            viewholder.group = row.findViewById(R.id.group);
            viewholder.number = row.findViewById(R.id.number);
            viewholder.imageView = row.findViewById(R.id.imageView2);
            row.setTag(viewholder);

        }

        else {
            viewholder = (Viewholder) row.getTag();
        }



      BloodDonar bloodDonar = bloodDonars.get(i);
        viewholder.name.setText(bloodDonar.getName());
        viewholder.group.setText(bloodDonar.getGroup());
        viewholder.number.setText(bloodDonar.getNumber());
        byte[]DonarImage = bloodDonar.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(DonarImage,0,DonarImage.length);
        viewholder.imageView.setImageBitmap(bitmap);

        viewholder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (BloodDonar position : bloodDonars){
                    bloodDonars.remove(position);
                }
                notifyDataSetChanged();





            }
        });


        return row;




    }

    




}
