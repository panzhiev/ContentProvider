package com.tim.contentprovider.ui.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.tim.contentprovider.R;
import com.tim.contentprovider.model.Person;

/**
 * Created by Tim on 21.01.2017.
 */
public class DetailsPersonsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);

        TextView tvName = (TextView)findViewById(R.id.text_view_details_name);
        TextView tvSurname = (TextView)findViewById(R.id.text_view_details_surname);
        TextView tvPhone = (TextView)findViewById(R.id.text_view_details_phone);
        TextView tvMail = (TextView)findViewById(R.id.text_view_details_mail);
        TextView tvSkype = (TextView)findViewById(R.id.text_view_details_skype);
        ImageView ivProfile = (ImageView) findViewById(R.id.image_view_main_profile);

        tvName.setText(Person.selectedPerson.getmName());
        tvSurname.setText(Person.selectedPerson.getmSurname());
        tvPhone.setText(Person.selectedPerson.getmPhone());
        tvMail.setText(Person.selectedPerson.getmMail());
        tvSkype.setText(Person.selectedPerson.getmSkype());

        String decodedPhoto = Person.selectedPerson.getmProfile();
        ivProfile.setImageBitmap(decodeBase64(decodedPhoto));
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodeByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
    }
}
