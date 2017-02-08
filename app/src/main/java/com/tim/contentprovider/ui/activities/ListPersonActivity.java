package com.tim.contentprovider.ui.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.tim.contentprovider.R;
import com.tim.contentprovider.db.DBContentProvider;
import com.tim.contentprovider.db.PersonContract;
import com.tim.contentprovider.model.Person;
import com.tim.contentprovider.ui.adapters.FilterAdapter;

import java.util.ArrayList;

public class ListPersonActivity extends AppCompatActivity {

    RecyclerView rvPerson;
    ArrayList<Person> listOfPersons;
    FilterAdapter adapter;
    private EditText tvSelect;

    //отработка метода при создании активности
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //размещаем вью activity_recycler_view_person на активити
        setContentView(R.layout.activity_recycler_view_person);
        //инициализация listOfPersons пустым ArrayList
        listOfPersons = new ArrayList<>();
        //инициализируем lvPerson с помощью list_view_Person
        rvPerson = (RecyclerView) findViewById(R.id.recycler_view_person);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvPerson.setLayoutManager(llm);

        String selection = null;
        String sortOrder = null;

        Cursor cursor = getContentResolver().query(DBContentProvider.PERSONS_CONTENT_URI, null, selection, null, sortOrder);

        while (cursor.moveToNext())
        {
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}