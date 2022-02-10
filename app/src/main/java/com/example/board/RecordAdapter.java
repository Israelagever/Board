package com.example.board;

import android.content.Context;


import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.List;



/**

 * Created by asafamir on 09/01/2019.

 */



public class RecordAdapter extends  RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {



    //this context we will use to inflate the layout

    private Context mCtx;

    //we are storing all the products in a list

    private List<Record> recordList;

    //getting the context and product list with constructor

    public RecordAdapter(Context mCtx, List<Record> recordList)

    {

        this.mCtx = mCtx;

        this.recordList = recordList;

    }



    @Override

    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //inflating and returning our view holder

        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View view = inflater.inflate(R.layout.records_layuot, null);

        return new RecordViewHolder(view);

    }



    @Override

    public void onBindViewHolder(RecordViewHolder holder, int position) {

        //getting the product of the specified position

        Record record = recordList.get(position);

        //binding the data with the viewholder views

        holder.tvId.setText(String.valueOf(record.getRecordId()));

        holder.tvMove.setText(String.valueOf(record.getMove()));

        holder.tvTime.setText(record.getTime());

        holder.tvDate.setText(record.getDate());

    }

    @Override

    public int getItemCount() {

        return recordList.size();

    }



    class RecordViewHolder extends RecyclerView.ViewHolder {



        TextView tvId, tvMove, tvTime,tvDate;



        public RecordViewHolder(View itemView) {

            super(itemView);

            tvId = itemView.findViewById(R.id.tvId);

            tvMove = itemView.findViewById(R.id.tvMoves);

            tvTime = itemView.findViewById(R.id.tvTime);

            tvDate = itemView.findViewById(R.id.tvDate);

        }

    }



}
