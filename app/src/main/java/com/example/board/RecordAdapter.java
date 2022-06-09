package com.example.board;

import android.content.Context;


import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.List;



/**

 * Created by asafamir on 09/01/2019.

 */



public class RecordAdapter extends  RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {




    private final Context context;

    private final List<Record> recordList;//הרשימה של השיאים


    public RecordAdapter(Context context, List<Record> recordList)//פעולה בונה

    {
        this.context = context;
        this.recordList = recordList;
    }


    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {//יצירת holder בשביל הview הכללי של השיאים


        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.records_layuot, null);
        return new RecordViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecordViewHolder holder, int position) {//הכנסת כל שיא בRecyclerView


        Record record = recordList.get(position);

        holder.tvNum.setText(String.valueOf(position+1));
        holder.tvMove.setText(String.valueOf(record.getMove()));
        holder.tvTime.setText(record.getTime());
        holder.tvDate.setText(record.getDate());
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }//פעולה שמחזירה את הגודל של הרשימה

    class RecordViewHolder extends RecyclerView.ViewHolder {//הclass שיורש מViewHolder

        TextView tvNum, tvMove, tvTime,tvDate;

        public RecordViewHolder(View itemView) {//מאתחל את הview הנוכחי
            super(itemView);

            tvNum = itemView.findViewById(R.id.tvId);
            tvMove = itemView.findViewById(R.id.tvMoves);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }

}
