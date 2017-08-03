package com.example.nicke.loginapplication.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.nicke.loginapplication.ActivityUserAccount;
import com.example.nicke.loginapplication.models.NavigationItems;
import com.example.nicke.loginapplication.R;

import java.util.ArrayList;

/**
 * Created by Nicke on 7/11/2017.
 */

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    Context context;
    ArrayList<NavigationItems> navigationItems;

    TextView title, subtitle;
    ImageView icon;

    ActivityUserAccount activityUserAccount;



    public NavigationDrawerAdapter(ActivityUserAccount activityUserAccount, ArrayList<NavigationItems> navigationItems) {
        Log.e("Adapter Constructor:", "Adapter konstruktor");
        this.activityUserAccount = activityUserAccount;
        this.navigationItems = navigationItems;

        Bundle bundle = new Bundle();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("onCreateViewHolder: ", "onCreateViewHolder metod");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Log.e("onBindViewHolder: ", "onBindViewHolder metod");

                holder.itemView.getTag();


                NavigationItems item = navigationItems.get(position);

                title.setText(item.getTitle());
                subtitle.setText(item.getSubtitle());

                icon.setBackgroundResource(item.getIcon());



    }

    @Override
    public int getItemCount() {
        Log.e("getItemCount: ", "getItemCount metod");
        return navigationItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(View itemView) {
            super(itemView);

            Log.e("MyViewHolder: ", "MyViewHolder Constuctor");
            title = (TextView) itemView.findViewById(R.id.title);
            subtitle =(TextView) itemView.findViewById(R.id.subtitle);

            icon = (ImageView) itemView.findViewById(R.id.icon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activityUserAccount.selectItem(getAdapterPosition());
                }
            });

        }
    }
}
