package com.example.spatel.giphyexample.customviews.ui;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nikunjrapariya.interview.customviews.EndlessRecyclerviewAdapter;
import com.example.nikunjrapariya.interview.model.ContactItem;

import java.util.List;

/**
 * Created by spatel on 25-03-2018.
 */

public class ContactEndlessAdapter extends EndlessRecyclerviewAdapter<ContactItem> {
    public interface ContactEndlessAdapterListener{
        void onItemClick(ContactItem contactItem);
    }

    private ContactEndlessAdapterListener mContactEndlessAdapterListener;
    public ContactEndlessAdapter(List<ContactItem> list, ContactEndlessAdapterListener contactEndlessAdapterListener) {
        super(list);
        mContactEndlessAdapterListener = contactEndlessAdapterListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof EndlessViewHolder) {
            EndlessViewHolder endlessViewHolder = (EndlessViewHolder) holder;
            endlessViewHolder.mContactName.setText(mList.get(position).name);
            endlessViewHolder.mContactEmail.setText(mList.get(position).email);
            endlessViewHolder.mContactPhone.setText(mList.get(position).phone.mobile);
            endlessViewHolder.mCardview.setTag(position);
            endlessViewHolder.mCardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContactEndlessAdapterListener.onItemClick(mList.get((Integer) view.getTag()));
                }
            });
        }
    }

    @Override
    protected RecyclerView.ViewHolder getEndlessViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contact_list, parent, false);
        return new EndlessViewHolder(view);
    }

    private class EndlessViewHolder extends RecyclerView.ViewHolder {

        private TextView mContactName;
        private TextView mContactEmail;
        private TextView mContactPhone;
        private CardView mCardview;

        public EndlessViewHolder(View view) {
            super(view);
            mContactName = view.findViewById(R.id.contact_name_textview);
            mContactEmail = view.findViewById(R.id.contact_email_textview);
            mContactPhone = view.findViewById(R.id.contact_phone_textview);
            mCardview = view.findViewById(R.id.cardview);
        }
    }
}
