package com.example.root.blooddoonarapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

public class DonorListActivity extends AppCompatActivity {

    GridView gridView;
    java.util.concurrent.CopyOnWriteArrayList<BloodDonor> list;
    DonarListAdapter adapter = null;
    String name,bloodgroup,number;
    byte[] image;
    int id;
    private int RESULT_LOAD_IMAGE = 1;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_list);
        gridView = findViewById(R.id.my_gridview);
        list = new java.util.concurrent.CopyOnWriteArrayList<>();
        adapter = new DonarListAdapter(this, R.layout.donoritem, list);
        gridView.setAdapter(adapter);



        Cursor cursor = MainActivity.sqlLiteHelper.getdata("SELECT * FROM DONOR");
        list.clear();
        while (cursor.moveToNext()) {
            id = cursor.getInt(0);
            name = cursor.getString(1);
            bloodgroup = cursor.getString(2);
            number = cursor.getString(3);
            image = cursor.getBlob(4);
            list.add(new BloodDonor(id, name, bloodgroup, number, image));

        }

        adapter.notifyDataSetChanged();


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri uri = data.getData();
            // This profile image i am storing into a sharedpreference
            SharedPreferences.Editor editor = DonarListAdapter.sharedPreferences.edit();
            // save uri to shared preference
            editor.putString("profilePicUrl",uri.toString());
            editor.commit();
            DonarListAdapter.imageView.setImageURI(uri);

        }

        switch (requestCode) {

            case (7):
                if (resultCode == Activity.RESULT_OK) {

                    Uri uri;
                    Cursor cursor1, cursor2;
                    String TempNameHolder, TempNumberHolder, TempContactID, IDresult = "" ;
                    int IDresultHolder ;

                    uri = data.getData();

                    cursor1 = getContentResolver().query(uri, null, null, null, null);

                    if (cursor1.moveToFirst()) {

                        TempNameHolder = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        TempContactID = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));

                        IDresult = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        IDresultHolder = Integer.valueOf(IDresult) ;

                        if (IDresultHolder == 1) {

                            cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + TempContactID, null, null);

                            while (cursor2.moveToNext()) {

                                TempNumberHolder = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                               DonarListAdapter.editname.setText(TempNameHolder);

                                DonarListAdapter.editnumber.setText(TempNumberHolder);

                            }
                        }

                    }
                }
                break;
        }

    }
}





