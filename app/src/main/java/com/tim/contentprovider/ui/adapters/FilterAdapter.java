package com.tim.contentprovider.ui.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.tim.contentprovider.R;
import com.tim.contentprovider.db.DBContentProvider;
import com.tim.contentprovider.model.Person;
import com.tim.contentprovider.ui.MainActivity;
import com.tim.contentprovider.ui.activities.DetailsPersonsActivity;
import com.tim.contentprovider.utils.Utility;

import java.util.ArrayList;
import java.util.List;

//import com.tim.contentprovider.db.CRUDSharedPreferences;

/**
 * Created by Tim on 22.01.2017.
 */

public class FilterAdapter extends RecyclerView.Adapter<ViewHolderPerson> implements Filterable {

    private Context mContext;
    private List<Person> personsList;
    private List<Person> origPersonList;
    private Filter personFilter;
    public  String TAG = "MY_LOG_ADAPTER";

    public FilterAdapter(Context mContext, ArrayList<Person> personsList) {
        super();
        this.mContext = mContext;
        this.personsList = personsList;
        this.origPersonList = personsList;
    }

    public void resetData() {
        personsList = origPersonList;
    }

    @Override
    public ViewHolderPerson onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        ViewHolderPerson holder = new ViewHolderPerson(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderPerson holder, final int position) {

        final Person person = personsList.get(position);

        holder.cvPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Card", "onClick");
                int idPerson = person.getmId();
                Bundle bundle = new Bundle();
                bundle.putInt("IdPersonToDetailActivity", idPerson);
                Intent intent = new Intent(mContext, DetailsPersonsActivity.class);
                intent.putExtras(bundle);
//                Person.selectedPerson = person;
                Log.d(TAG, "idPerson = " + idPerson);
                mContext.startActivity(intent);
            }
        });
        holder.tvName.setText(person.getmName());
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idPerson = person.getmId();
                Bundle bundle = new Bundle();
                bundle.putInt("IdPersonToDetailActivity", idPerson);
                Intent intent = new Intent(mContext, DetailsPersonsActivity.class);
                intent.putExtras(bundle);
//                Person.selectedPerson = person;
                Log.d(TAG, "idPerson = " + idPerson);
                mContext.startActivity(intent);
            }
        });
        holder.tvPhone.setText(person.getmPhone());
        holder.tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + person.getmPhone()));
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mContext.startActivity(intent);
            }
        });

        Bitmap bitmap = Utility.decodeBase64(person.getmProfile());
        holder.civProfile.setImageBitmap(bitmap);
    }

    public static AppCompatActivity getActivityFromContext(Context context) {
        if (context instanceof Activity) {
            return (AppCompatActivity) context;
        }
        if (context instanceof ContextWrapper &&
                ((ContextWrapper) context).getBaseContext() instanceof Activity) {
            return (AppCompatActivity) ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return personsList.size();
    }

    private void openDeleteDialog(final Person personItem) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("Delete contact " + personItem.getmName() + " " + personItem.getmSurname() + "?")
                          .setIcon(R.mipmap.ic_delete_forever_black_24dp);

        alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (!personsList.isEmpty()) {
                    personsList.remove(personItem);
                    mContext.getContentResolver().delete(Uri.parse(DBContentProvider.PERSONS_CONTENT_URI + "/" + personItem.getmId()), null, null);
                    sendNotification(personItem);
                    notifyDataSetChanged();
                }
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

    private void sendNotification(final Person personItem) {
        final int NOTIFY_ID = 101;
        Context context = mContext.getApplicationContext();
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Delete Contact " + personItem.getmSurname() + " " + personItem.getmName())
                .setContentText("Follow to Main Activity")
                .setAutoCancel(false);
        Notification notification = builder.getNotification();
        nm.notify(NOTIFY_ID, notification);
    }

    private class PersonFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = origPersonList;
                results.count = origPersonList.size();
            } else {
                List<Person> newPersonList = new ArrayList<Person>();

                for (Person p : personsList) {
                    if (p.getmName().toUpperCase().contains(constraint.toString().toUpperCase())) {
                        newPersonList.add(p);
                    }
                }
                results.values = newPersonList;
                results.count = newPersonList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0) {
                personsList = (List<Person>) results.values;
                notifyDataSetChanged();
                Toast.makeText(mContext, "Совпадений не найдено", Toast.LENGTH_SHORT).show();
            } else {
                personsList = (List<Person>) results.values;
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public Filter getFilter() {
        if (personFilter == null)
            // creating new PersonFilter if nit exists
            personFilter = new PersonFilter();
        return personFilter;
    }
}
