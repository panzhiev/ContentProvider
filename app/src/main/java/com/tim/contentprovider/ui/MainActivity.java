package com.tim.contentprovider.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tim.contentprovider.R;
import com.tim.contentprovider.db.DBContentProvider;
import com.tim.contentprovider.db.PersonContract;
import com.tim.contentprovider.ui.activities.ListPersonActivity;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

//    Person person;
//    private EditText etId;
    private EditText etName;
    private EditText etSurname;
    private EditText etPhone;
    private EditText etMail;
    private EditText etSkype;
    private Button btnSave;
    private Button btnTakePhoto;
    private ImageView ivPerson;

    final String TAG = "MYLOGS";
    final int REQUEST_CODE_PHOTO = 1;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //связываем кнопки с макетом
//        etId = (EditText) findViewById(R.id.et_id);
        etName = (EditText) findViewById(R.id.et_name);
        etSurname = (EditText) findViewById(R.id.et_surname);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etMail = (EditText) findViewById(R.id.et_mail);
        etSkype = (EditText) findViewById(R.id.et_skype);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnTakePhoto = (Button) findViewById(R.id.btn_take_photo);
        ivPerson = (ImageView) findViewById(R.id.image_view_main_profile);
        btnSave.setOnClickListener(this);
        btnTakePhoto.setOnClickListener(this);

//        btnTakePhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:

                Bitmap bitmap = ((BitmapDrawable) ivPerson.getDrawable()).getBitmap();
                String imageIncodedString = encodeToBase64(bitmap);

                ContentValues contentValues = new ContentValues();
                contentValues.put(PersonContract.KEY_NAME, etName.getText().toString());
                contentValues.put(PersonContract.KEY_SURNAME, etSurname.getText().toString());
                contentValues.put(PersonContract.KEY_PHONE, etPhone.getText().toString());
                contentValues.put(PersonContract.KEY_MAIL, etMail.getText().toString());
                contentValues.put(PersonContract.KEY_SKYPE, etSkype.getText().toString());
                contentValues.put(PersonContract.KEY_PROFILE, imageIncodedString);
                getContentResolver().insert(DBContentProvider.PERSONS_CONTENT_URI, contentValues);
                followToListPersons();
                clear();
                break;
            case R.id.btn_take_photo:
                try{
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CODE_PHOTO);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            default: break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && intent != null){
            if (requestCode == REQUEST_CODE_PHOTO){
                if(resultCode == RESULT_OK){
                    if (intent == null)
                    {
                        Log.d(TAG, "Intent is null");
                    } else {
                        Log.d(TAG, "Photo uri: " + intent.getData());
                        Bundle bndl = intent.getExtras();
                        if(bndl != null){
                            Object obj = intent.getExtras().get("data");
                            if(obj instanceof Bitmap){
                                Bitmap bitmap = (Bitmap) obj;
                                Log.d(TAG, "bitmap" + bitmap.getWidth() + " x " + bitmap.getHeight());
                                ivPerson.setImageBitmap(bitmap);
                            }
                        }
                    }
                }else if(resultCode == RESULT_CANCELED)
                {
                    Log.d(TAG, "Canceled");
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_follow_to_listPersonActivity) {
            followToListPersons();
            return true;
        }
        return true;
    }

    private void clear()
    {
//        etId.setText("");
        etName.setText("");
        etSurname.setText("");
        etPhone.setText("");
        etMail.setText("");
        etSkype.setText("");
        ivPerson.setImageBitmap(null);
    }
    private void followToListPersons()
    {
        Intent intent = new Intent(MainActivity.this, ListPersonActivity.class);
        startActivity(intent);
    }

    public static String encodeToBase64(Bitmap image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image was encoded: ", imageEncoded);
        return imageEncoded;
    }
}
