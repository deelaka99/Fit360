package com.deelaka.appfit360.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.deelaka.appfit360.R;
import com.deelaka.appfit360.models.WaterRecord;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WaterAdapter extends RecyclerView.Adapter<WaterAdapter.WaterViewHolder> {
    private Context mContext;
    private List<WaterRecord> mList;


    public WaterAdapter(Context mContext, List<WaterRecord> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public WaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflating the item layout and creating a new viewHolder
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
        return new WaterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WaterViewHolder holder, int position) {
        // Binding the data from WaterRecord to the views in the ViewHolder
        WaterRecord waterRecord= mList.get(position);

        Timestamp stamp = new Timestamp(waterRecord.time);
        Date date = new Date(stamp.getTime());

        //Formatting date as a string
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = dateFormat.format(date);
        //Formatting time as a string
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        String timeString = timeFormat.format(date);
        //Display date and time strings
        holder.tv.setText("Capacity: "+waterRecord.capacity+"ml\nTime: "+timeString+"\nDate: "+dateString);
      }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setmList(List<WaterRecord> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public static class WaterViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;

        public WaterViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.text_view);
        }
    }
}
