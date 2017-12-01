package com.example.root.blooddoonarapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by root on 11/5/17.
 */

public class DonarListAdapter extends BaseAdapter {
    private Context context;
    private  int layout;
    private java.util.concurrent.CopyOnWriteArrayList<BloodDonor> bloodDonors;
    Viewholder viewholder;

        static ImageView imageView;
   static EditText editname, editgroup, editnumber;
    Button Update_button,pick_btn;
    String name,bloodgroup,number;
    byte[] image;
    int id;
    static SharedPreferences sharedPreferences;
    private static final int SELECT_PICTURE = 1;






    public DonarListAdapter(Context context, int layout,
                            java.util.concurrent.CopyOnWriteArrayList<BloodDonor> bloodDonors) {
        this.context = context;
        this.layout = layout;
        this.bloodDonors = bloodDonors;
    }

    @Override
    public int getCount() {
        return bloodDonors.size();
    }

    @Override
    public Object getItem(int i) {
        return bloodDonors.get(i);
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



      BloodDonor bloodDonor = bloodDonors.get(i);
        viewholder.name.setText(bloodDonor.getName());
        viewholder.group.setText(bloodDonor.getGroup());
        viewholder.number.setText(bloodDonor.getNumber());
        byte[]DonarImage = bloodDonor.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(DonarImage,0,DonarImage.length);
        viewholder.imageView.setImageBitmap(bitmap);

        viewholder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                CharSequence [] items = {"Update","Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0){
                            Cursor c  = MainActivity.sqlLiteHelper.getdata("SELECT _id FROM DONOR");
                            ArrayList<Integer> arrId = new ArrayList<>();
                            while (c.moveToNext()){
                                arrId.add(c.getInt(0));

                            }

                            showDialogUpdate( v.getContext(),arrId.get(i));
                            Toast.makeText(v.getContext(), "Update .....",
                                    Toast.LENGTH_LONG).show();
                        }
                        else {
                            Cursor c  = MainActivity.sqlLiteHelper.getdata("SELECT _id FROM DONOR");
                            ArrayList<Integer> arrId = new ArrayList<>();
                            while (c.moveToNext()){
                                arrId.add(c.getInt(0));
                            }

                            showDialogDelete(arrId.get(i));

                            Toast.makeText(v.getContext(), "Delete .....",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                });
                dialog.show();

            }
        });
        return row;




    }


    private void showDialogUpdate(Context activity, final int Position){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_bloodonor_activity);
        dialog.setTitle("Update");
        imageView = dialog.findViewById(R.id.imageView3);
        editname = dialog.findViewById(R.id.editText);
        editgroup = dialog.findViewById(R.id.editText2);
        editnumber = dialog.findViewById(R.id.editText3);
        Update_button = dialog.findViewById(R.id.button);
        pick_btn = dialog.findViewById(R.id.get_button);

        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels*0.95);

        int height = (int) (activity.getResources().getDisplayMetrics().widthPixels*1.00);
        dialog.getWindow().setLayout(width,height);
        showPreviousData(id);
        dialog.show();


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences  = context.getSharedPreferences(context.getPackageName()+
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
                ((Activity) context). startActivityForResult(Intent.createChooser
                                (intent, "Select Picture"),
                        SELECT_PICTURE);
            }


        });

        pick_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              Intent  intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                ((Activity) context).startActivityForResult(intent, 7);

            }
        });


        Update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    MainActivity.sqlLiteHelper.updateData(
                            editname.getText().toString().trim(),
                            editgroup.getText().toString().trim(),
                            editnumber.getText().toString().trim(),
                            MainActivity.imageViewToByte(imageView),
                            Position

                    );

                    dialog.dismiss();
                    Toast.makeText(v.getContext(),"Update Successfully",Toast.LENGTH_LONG).show();

                }
                catch (Exception e){
                    Log.e("Update error",e.getMessage());
                }

                UpdateDonorList();

            }
        });




    }

    private void showDialogDelete(final int id){
        final  AlertDialog.Builder dialogDelete = new AlertDialog.Builder(context);
        dialogDelete.setTitle("Warning!!");
        dialogDelete.setMessage("Are you sure you want to this delete?");
        dialogDelete.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    MainActivity.sqlLiteHelper.deleteData(id);
                    Toast.makeText(context,"Delete Successfully",Toast.LENGTH_LONG).show();
                }
                catch (Exception e){
                    Log.e("error",e.getMessage());

                }

                UpdateDonorList();

            }
        });

        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialogDelete.show();

    }

    private void UpdateDonorList(){
        Cursor cursor = MainActivity.sqlLiteHelper.getdata("SELECT * FROM DONOR");
        bloodDonors.clear();
        while (cursor.moveToNext()) {
            id = cursor.getInt(0);
            name = cursor.getString(1);
            bloodgroup = cursor.getString(2);
            number = cursor.getString(3);
            image = cursor.getBlob(4);
            bloodDonors.add(new BloodDonor(id, name, bloodgroup, number, image));

        }

      notifyDataSetChanged();

    }

    private void showPreviousData(int id){
        BloodDonor bloodDonor = bloodDonors.get(id);
        editname.setText(bloodDonor.getName());
       editgroup.setText(bloodDonor.getGroup());
       editnumber.setText(bloodDonor.getNumber());
        byte[]DonarImage = bloodDonor.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(DonarImage,0,DonarImage.length);
        imageView.setImageBitmap(bitmap);


    }
    }














    





