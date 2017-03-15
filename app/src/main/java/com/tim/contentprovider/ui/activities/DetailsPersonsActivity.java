package com.tim.contentprovider.ui.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.tim.contentprovider.R;
import com.tim.contentprovider.db.DBContentProvider;
import com.tim.contentprovider.db.PersonContract;
import com.tim.contentprovider.utils.Utility;

/**
 * Created by Tim on 21.01.2017.
 */
public class DetailsPersonsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
    TextView tvName, tvSurname, tvPhone, tvMail, tvSkype;
    ImageView ivProfile;
    public  String TAG = "MY_LOG_DetailsPersonsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);
        getLoaderManager().initLoader(0, null, this);

        tvName = (TextView)findViewById(R.id.text_view_details_name);
        tvSurname = (TextView)findViewById(R.id.text_view_details_surname);
        tvPhone = (TextView)findViewById(R.id.text_view_details_phone);
        tvMail = (TextView)findViewById(R.id.text_view_details_mail);
        tvSkype = (TextView)findViewById(R.id.text_view_details_skype);
        ivProfile = (ImageView) findViewById(R.id.image_view_main_profile);

//        tvName.setText(Person.selectedPerson.getmName());
//        tvSurname.setText(Person.selectedPerson.getmSurname());
//        tvPhone.setText(Person.selectedPerson.getmPhone());
//        tvMail.setText(Person.selectedPerson.getmMail());
//        tvSkype.setText(Person.selectedPerson.getmSkype());

//        String decodedPhoto = Person.selectedPerson.getmProfile();
//        ivProfile.setImageBitmap(decodeBase64(decodedPhoto));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        String selection = null;
        String sortOrder = null;

        if (bundle != null)
        {
            int idPerson = bundle.getInt("IdPersonToDetailActivity");
            selection = "IdPersonToDetailActivity = " + idPerson;
            Log.d(TAG, "idPerson = " + idPerson);
        } else {
            Log.d(TAG, "bundle == null");
        }

        CursorLoader loader = new CursorLoader(this, DBContentProvider.PERSONS_CONTENT_URI, null, selection, null, sortOrder);

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        while (cursor.moveToNext())
        {
            tvName.setText(cursor.getString(cursor.getColumnIndex(PersonContract.KEY_NAME)));
            tvSurname.setText(cursor.getString(cursor.getColumnIndex(PersonContract.KEY_SURNAME)));
            tvPhone.setText(cursor.getString(cursor.getColumnIndex(PersonContract.KEY_PHONE)));
            tvMail.setText(cursor.getString(cursor.getColumnIndex(PersonContract.KEY_MAIL)));
            tvSkype.setText(cursor.getString(cursor.getColumnIndex(PersonContract.KEY_SKYPE)));
            ivProfile.setImageBitmap(Utility.decodeBase64(cursor.getString(cursor.getColumnIndex(PersonContract.KEY_PROFILE))));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
