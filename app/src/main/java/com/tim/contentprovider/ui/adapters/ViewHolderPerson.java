package com.tim.contentprovider.ui.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tim.contentprovider.R;

/**
 * Created by Tim on 22.01.2017.
 */

public class ViewHolderPerson extends RecyclerView.ViewHolder {

    public CardView cvPerson;
    public TextView tvName;
    public TextView tvPhone;
    public ImageButton ibEdit;
    public ViewHolderPerson(View itemView) {
        super(itemView);
        this.cvPerson = (CardView) itemView.findViewById(R.id.card_view_person);
        this.tvName = (TextView) itemView.findViewById(R.id.text_view_item_short_name);
        this.tvPhone = (TextView) itemView.findViewById(R.id.text_view_item_short_phone_number);
        this.ibEdit = (ImageButton) itemView.findViewById(R.id.ib_short_edit);
    }
}
