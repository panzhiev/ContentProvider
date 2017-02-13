package com.tim.contentprovider.ui.activities;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.tim.contentprovider.R;
import com.tim.contentprovider.db.DBContentProvider;
import com.tim.contentprovider.db.PersonContract;
import com.tim.contentprovider.model.Person;
import com.tim.contentprovider.ui.adapters.FilterAdapter;

import java.util.ArrayList;

import static com.tim.contentprovider.utils.Utility.encodeToBase64;


public class ListPersonActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    RecyclerView rvPerson;
    ArrayList<Person> listOfPersons;
    FilterAdapter adapter;
    private EditText tvSelect;
    final String TAG = "MYLOGS";
    final int REQUEST_CODE_PHOTO = 2;
    private static int RESULT_LOAD_IMAGE = 2;
    public static String newPhoto;

    //отработка метода при создании активности
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //размещаем вью activity_recycler_view_person на активити
        setContentView(R.layout.activity_recycler_view_person);
        //инициализация listOfPersons пустым ArrayList
        listOfPersons = new ArrayList<>();
        //инициализация лоадера
        getLoaderManager().initLoader(0, null, this);

        //инициализируем lvPerson с помощью list_view_Person
        rvPerson = (RecyclerView) findViewById(R.id.recycler_view_person);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvPerson.setLayoutManager(llm);

        String selection = null;
        String sortOrder = null;

        Cursor cursor = getContentResolver().query(DBContentProvider.PERSONS_CONTENT_URI, null, selection, null, sortOrder);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(PersonContract.KEY_ID));
            String name = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_NAME));
            String surname = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_SURNAME));
            String phone = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_PHONE));
            String mail = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_MAIL));
            String skype = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_SKYPE));
            String profile = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_PROFILE));
            listOfPersons.add(new Person(id, name, surname, phone, mail, skype, profile));
        }
        //инициализация adapter, передаем в конструктор текущий контекст и список персон
        adapter = new FilterAdapter(this, listOfPersons);
        //кладем адаптер в rvPerson
        rvPerson.setAdapter(adapter);

        // finding a search field in the markup
        tvSelect = (EditText) findViewById(R.id.text_view_list_select);
        // binding the listener to the search field
        tvSelect.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < before) {
                    // if the search text is shorter - reload filterAdapter
                    adapter.resetData();
                }
                // else - use a filter
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && intent != null) {
            if (requestCode == REQUEST_CODE_PHOTO) {
                if (resultCode == RESULT_OK) {
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
//                                ivPhotoEdit.setImageBitmap(bitmap);
                                newPhoto = encodeToBase64(bitmap);
                            }
                        }
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Log.d(TAG, "Canceled");
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_selection_by_id:
                getPersonId();
                break;
            case R.id.action_delete_all_persons:
                getContentResolver().delete(DBContentProvider.PERSONS_CONTENT_URI, null, null);
//                adapter.dropAllPesons();
                break;
            case R.id.action_close_list_person:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getPersonId() {
        final LayoutInflater getPersonDialogInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewPerson = getPersonDialogInflater.inflate(R.layout.dialog_selection_by_id, null);

        final EditText etSelectPersonById = (EditText) viewPerson.findViewById(R.id.et_select_person_by_id);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);//запуск диалогового окна, параметр контекст откуда будет запущено диалоговое окно
        builder.setView(viewPerson);//вью как контент для использования
        builder.setMessage("Enter id person:");//Задает под заглавие диалогового окна
        builder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int idPerson = Integer.parseInt(etSelectPersonById.getText().toString());
                Bundle bundle = new Bundle();
                bundle.putInt("SelectPersonById", idPerson);

//                Cursor cursor = getContentResolver().query(DBContentProvider.PERSONS_CONTENT_URI, null, null, null, null);
//                if (cursor.moveToFirst()) {
//                    do {
//                        if (Integer.parseInt(etSelectPersonById.getText().toString()) == cursor.getInt(cursor.getColumnIndex(PersonContract.KEY_ID)))
//                        {
//                            int id = cursor.getInt(cursor.getColumnIndex(PersonContract.KEY_ID));
//                            String name = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_NAME));
//                            String surname = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_SURNAME));
//                            String phone = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_PHONE));
//                            String mail = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_MAIL));
//                            String skype = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_SKYPE));
//                            String profile = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_PROFILE));
//                            ArrayList<Person> list = new ArrayList<Person>();
//                            list.add(new Person(id, name, surname, phone, mail, skype, profile));
//                            adapter.notifyDataSetChanged();
//                        }
//                    } while (cursor.moveToNext());
//                    cursor.close();
//                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
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

        if (bundle != null) {
            selection = "SelectPersonById" + bundle.getInt("SelectPersonById");
        }

        CursorLoader loader = new CursorLoader(this, DBContentProvider.PERSONS_CONTENT_URI, null, selection, null, sortOrder);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        listOfPersons.clear();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(PersonContract.KEY_ID));
            String name = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_NAME));
            String surname = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_SURNAME));
            String phone = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_PHONE));
            String mail = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_MAIL));
            String skype = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_SKYPE));
            String profile = cursor.getString(cursor.getColumnIndex(PersonContract.KEY_PROFILE));
            listOfPersons.add(new Person(id, name, surname, phone, mail, skype, profile));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}