package com.tim.contentprovider.ui.activities;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tim.contentprovider.R;
import com.tim.contentprovider.db.DBContentProvider;
import com.tim.contentprovider.db.PersonContract;
import com.tim.contentprovider.model.Person;
import com.tim.contentprovider.utils.DatabaseTasks;
import com.tim.contentprovider.utils.Utility;

/**
 * Created by Tim on 21.01.2017.
 */
public class DetailsPersonsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView tvName, tvSurname, tvPhone, tvMail, tvSkype;
    private ImageView ivProfile;
    private ImageButton ibPhotoEdit;
    private ImageButton ibEdit;
    public String TAG = "MY_LOG_DetailsPersonsActivity";
    final int REQUEST_CODE_PHOTO = 2;
    public int idPerson;
    public Person person;
    private DatabaseTasks dbt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        idPerson = bundle.getInt("IdPersonToDetailActivity");
        getLoaderManager().initLoader(0, bundle, this);

        dbt = new DatabaseTasks(DetailsPersonsActivity.this);

        person = new Person();

        setContentView(R.layout.activity_person_details);
        ibEdit = (ImageButton) findViewById(R.id.ib_edit);
        ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditDialog(person);
            }
        });
        ibPhotoEdit = (ImageButton) findViewById(R.id.ib_photo_edit);
        ibPhotoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_PHOTO);
            }
        });

        tvName = (TextView) findViewById(R.id.text_view_details_name);
        tvSurname = (TextView) findViewById(R.id.text_view_details_surname);
        tvPhone = (TextView) findViewById(R.id.text_view_details_phone);
        tvMail = (TextView) findViewById(R.id.text_view_details_mail);
        tvSkype = (TextView) findViewById(R.id.text_view_details_skype);
        ivProfile = (ImageView) findViewById(R.id.image_view_main_profile);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        String selection = null;
        String sortOrder = null;

        if (bundle != null) {
            int idPerson = bundle.getInt("IdPersonToDetailActivity");
            selection = "ID = " + idPerson;
            Log.d(TAG, "idPerson = " + idPerson);
        } else {
            Log.d(TAG, "bundle == null");
        }

        CursorLoader loader = new CursorLoader(this, DBContentProvider.PERSONS_CONTENT_URI, null, selection, null, sortOrder);

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        while (cursor.moveToNext()) {

            //достаем из БД данные, заполняем TextView в DetailsPersonsActivity;
            tvName.setText("Name: " + cursor.getString(cursor.getColumnIndex(PersonContract.KEY_NAME)));
            tvSurname.setText("Surname: " + cursor.getString(cursor.getColumnIndex(PersonContract.KEY_SURNAME)));
            tvPhone.setText("Phone: " + cursor.getString(cursor.getColumnIndex(PersonContract.KEY_PHONE)));
            tvMail.setText("Mail: " + cursor.getString(cursor.getColumnIndex(PersonContract.KEY_MAIL)));
            tvSkype.setText("Skype: " + cursor.getString(cursor.getColumnIndex(PersonContract.KEY_SKYPE)));
            ivProfile.setImageBitmap(Utility.decodeBase64(cursor.getString(cursor.getColumnIndex(PersonContract.KEY_PROFILE))));

            //достаем из БД данные, заполняем поля персоны;
            person.setmId(cursor.getInt(cursor.getColumnIndex(PersonContract.KEY_ID)));
            person.setmName(cursor.getString(cursor.getColumnIndex(PersonContract.KEY_NAME)));
            person.setmSurname(cursor.getString(cursor.getColumnIndex(PersonContract.KEY_SURNAME)));
            person.setmPhone(cursor.getString(cursor.getColumnIndex(PersonContract.KEY_PHONE)));
            person.setmMail(cursor.getString(cursor.getColumnIndex(PersonContract.KEY_MAIL)));
            person.setmSkype(cursor.getString(cursor.getColumnIndex(PersonContract.KEY_SKYPE)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_PHOTO) {
                if (intent == null) {
                    Log.d(TAG, "Intent is null");
                } else {
                    Log.d(TAG, "Photo uri: " + intent.getData());
                    Bundle bndl = intent.getExtras();
                    if (bndl != null) {
                        Object obj = intent.getExtras().get("data");
                        if (obj instanceof Bitmap) {
                            Bitmap bitmap = (Bitmap) obj;
                            Log.d(TAG, "bitmap" + bitmap.getWidth() + " x " + bitmap.getHeight());

                            String imageIncodedString = Utility.encodeToBase64(bitmap);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(PersonContract.KEY_PROFILE, imageIncodedString);

                            dbt.execute(DatabaseTasks.UPDATE, idPerson, contentValues);
                        }
                    }
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            Log.d(TAG, "Canceled");
        }
    }

    private void openEditDialog(final Person person) {
        LayoutInflater dialogInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = dialogInflater.inflate(R.layout.dialog_edit, null);

        final TextView etDialogId = (TextView) view.findViewById(R.id.tv_text_dialog_id);
        final EditText etDialogName = (EditText) view.findViewById(R.id.edit_text_dialog_name);
        final EditText etDialogSurname = (EditText) view.findViewById(R.id.edit_text_dialog_surname);
        final EditText etDialogPhone = (EditText) view.findViewById(R.id.edit_text_dialog_phone_number);
        final EditText etDialogMail = (EditText) view.findViewById(R.id.edit_text_dialog_mail);
        final EditText etDialogSkype = (EditText) view.findViewById(R.id.edit_text_dialog_skype);

        etDialogId.setText(String.valueOf(person.getmId()));
        etDialogName.setText(person.getmName());
        etDialogSurname.setText(person.getmSurname());
        etDialogPhone.setText(person.getmPhone());
        etDialogMail.setText(person.getmMail());
        etDialogSkype.setText(person.getmSkype());

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setMessage("Edit Contact");

        alertDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ContentValues contentValues = new ContentValues();
                contentValues.put(PersonContract.KEY_NAME, etDialogName.getText().toString());
                contentValues.put(PersonContract.KEY_SURNAME, etDialogSurname.getText().toString());
                contentValues.put(PersonContract.KEY_PHONE, etDialogPhone.getText().toString());
                contentValues.put(PersonContract.KEY_MAIL, etDialogMail.getText().toString());
                contentValues.put(PersonContract.KEY_SKYPE, etDialogSkype.getText().toString());
                dbt.execute(DatabaseTasks.UPDATE, person.getmId(), contentValues);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.cancel();
            }
        });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.create();
        alertDialogBuilder.show();
    }
}
