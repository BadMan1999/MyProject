package com.example.myproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_Home extends RecyclerView.Adapter<Adapter_Home.Holder> {
    ArrayList<Item_Old_Statues> AdapterIteamChaletLists;
    OnItemClickListener onItemClickListener;
    Context context;
    ViewGroup g;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public Adapter_Home(ArrayList<Item_Old_Statues> adapterIteamChaletLists, Context context) {
        this.AdapterIteamChaletLists = adapterIteamChaletLists;
        this.context = context;

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null, false);
        Holder holder = new Holder(v);
        g = parent;

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Item_Old_Statues A_Iteam_C = AdapterIteamChaletLists.get(position);


    }

    @Override
    public int getItemCount() {
        return AdapterIteamChaletLists.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView text_date, text_w, text_l;


        public Holder(@NonNull View itemView) {


            super(itemView);
            text_date = itemView.findViewById(R.id.text_date);
            text_w = itemView.findViewById(R.id.text_w);
            text_l = itemView.findViewById(R.id.text_l);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onItemClick(position);

                        }
                    }
                }
            });


        }
    }

}
