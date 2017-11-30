package com.example.root.blooddoonarapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.GridView;

public class DonarListActivity extends AppCompatActivity {

    GridView gridView;
    java.util.concurrent.CopyOnWriteArrayList<BloodDonar> list;
    DonarListAdapter adapter = null;
    String name,bloodgroup,number;
    byte[] image;
    int id;
    Button delete;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donar_list);
        gridView = findViewById(R.id.my_gridview);
        delete = findViewById(R.id.deleteitem);
        list = new java.util.concurrent.CopyOnWriteArrayList<>();
        adapter = new DonarListAdapter(this, R.layout.donaritem, list);
        gridView.setAdapter(adapter);

        Cursor cursor = MainActivity.sqlLiteHelper.getdata("SELECT * FROM DONAR");
        list.clear();
        while (cursor.moveToNext()) {
            id = cursor.getInt(0);
            name = cursor.getString(1);
            bloodgroup = cursor.getString(2);
            number = cursor.getString(3);
            image = cursor.getBlob(4);
            list.add(new BloodDonar(id, name, bloodgroup, number, image));

        }




    }
        public void  deleteItem(){
                MainActivity.sqlLiteHelper.delete(id);
        }


    }





