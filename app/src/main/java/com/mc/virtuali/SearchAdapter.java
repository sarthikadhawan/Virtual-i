package com.mc.virtuali;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;
    ArrayList<String> medname;
//    ArrayList<String> userNameList;
    HashMap<String ,medicine> med;


    class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView full_name, user_name;

        public SearchViewHolder(View itemView) {
            super(itemView);

            full_name = (TextView) itemView.findViewById(R.id.full_name);
            //user_name = (TextView) itemView.findViewById(R.id.user_name);
        }
    }

    public SearchAdapter(Context context, HashMap<String,medicine> med,ArrayList<String>medname) {
        this.context = context;
        this.med=med;
        this.medname=medname;

    }

    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_list, parent, false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchViewHolder holder, int position) {

        holder.full_name.setText(medname.get(position));
        //holder.user_name.setText(userNameList.get(position));


        holder.full_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Toast.makeText(context, "Full Name Clicked", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(context, SearchResultsActivity.class);
                intent1.putExtra("dosage",med.get(holder.full_name.getText()).dosage);
                intent1.putExtra("prescription",med.get(holder.full_name.getText()).prescription);
                intent1.putExtra("intake",med.get(holder.full_name.getText()).intake);
                intent1.putExtra("medicine_name",holder.full_name.getText());

                context.startActivity(intent1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medname.size();
    }
}
