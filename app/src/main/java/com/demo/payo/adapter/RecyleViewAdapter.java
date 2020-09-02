package com.demo.payo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.payo.R;
import com.demo.payo.model.SmsDto;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RecyleViewAdapter extends RecyclerView.Adapter<RecyleViewAdapter.ViewHolder> {

    private List<SmsDto> listdata;
    private Context context;

    public RecyleViewAdapter(Context _context,List<SmsDto> listdata) {
        this.context = _context;
        this.listdata = listdata;

    }
    public RecyleViewAdapter(Context context) {

        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.description.setText(listdata.get(position).getBody());
        double d=listdata.get(position).getAmount();
        holder.amount.setText(String.valueOf(d));
        holder.date.setText(listdata.get(position).getDate());

        if (listdata.get(position).getTransactionType() == 0) {
            holder.type.setText("Dr");
        } else {
            holder.type.setText("Cr");
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "click on item: " + listdata.get(position).getBody(), Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout relativeLayout;
        public TextView amount, description, date, type;

        public ViewHolder(View itemView) {
            super(itemView);

            this.amount =  itemView.findViewById(R.id.amount);
            this.description = itemView.findViewById(R.id.description);
            this.date = itemView.findViewById(R.id.date);
            this.type =  itemView.findViewById(R.id.type);
            relativeLayout =itemView.findViewById(R.id.relativeLayout);
        }
    }
}
