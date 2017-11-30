package com.example.root.blooddoonarapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    EditText edtName, edtbloodgroup,editNumber;
    Button btnChose,btnAdd,btnShow,callButton,picknameandnumber;
    ImageView profilePicture;
    public  static  SqlLiteHelper sqlLiteHelper;
    private  static int RESULT_LOAD_IMAGE = 999;
    public  static final int RequestPermissionCode  = 1 ;
    SharedPreferences sharedPreferences;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        EnableRuntimePermission();
        sqlLiteHelper = new SqlLiteHelper(this, "DONARDB.sqlite",
                null, 1);

        btnChose.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                profilePicture = findViewById(R.id.imageView);
                sharedPreferences  = getSharedPreferences(getString(R.string.app_name)+
                        "_ProfileDetails",MODE_PRIVATE);

                Intent intent;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                } else {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                }
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("image/*");

                startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                        RESULT_LOAD_IMAGE);


            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sqlLiteHelper.insertData(
                            edtName.getText().toString().trim(),
                            edtbloodgroup.getText().toString().trim(),
                            editNumber.getText().toString().trim(),
                            imageViewToByte(profilePicture)

                    );
                    Toast.makeText(MainActivity.this,"Add successfuly",
                            Toast.LENGTH_LONG).show();

                    edtName.setText("");
                    edtbloodgroup.setText("");
                    editNumber.setText("");
                    profilePicture.setImageResource(R.mipmap.ic_launcher);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }




            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,DonarListActivity.class);
                startActivity(intent);

            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNumber();
            }
        });

        picknameandnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 7);

            }
        });
    }

    private byte[] imageViewToByte(ImageView profilePicture) {
        Bitmap bitmap = ((BitmapDrawable)profilePicture.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();
        return byteArray;
    }






    private void init(){
        edtName = findViewById(R.id.pName);
        edtbloodgroup = findViewById(R.id.pgroup);
        editNumber = findViewById(R.id.pNumber);
        btnChose = findViewById(R.id.chose);
        btnAdd = findViewById(R.id.add);
        btnShow = findViewById(R.id.show);
        callButton = findViewById(R.id.call);
        picknameandnumber = findViewById(R.id.btpick_contact);

    }

    private String getPhoneNumber() {

        return editNumber.getText().toString();
    }
    private void callNumber() {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" +  getPhoneNumber()));
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        startActivity(callIntent);
    }
    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.READ_CONTACTS))
        {

            Toast.makeText(MainActivity.this,"CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }
    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MainActivity.this,"Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(MainActivity.this,"Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri uri = data.getData();
            // This profile image i am storing into a sharedpreference
            SharedPreferences.Editor editor = sharedPreferences.edit();
            // save uri to shared preference
            editor.putString("profilePicUrl",uri.toString());
            editor.commit();
            profilePicture.setImageURI(uri);

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

                                edtName.setText(TempNameHolder);

                                editNumber.setText(TempNumberHolder);

                            }
                        }

                    }
                }
                break;
        }
    }


    }

