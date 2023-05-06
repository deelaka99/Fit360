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
import com.deelaka.appfit360.models.FoodRecord;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private Context mContext;
    private List<FoodRecord> mList;


    public FoodAdapter(Context mContext, List<FoodRecord> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflating the item layout and creating a new viewHolder
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_food,parent,false);
        return new FoodViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        // Binding the data from WaterRecord to the views in the ViewHolder
        FoodRecord foodRecord= mList.get(position);

        Timestamp stamp = new Timestamp(foodRecord.time);
        Date date = new Date(stamp.getTime());

        //Formatting date as a string
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = dateFormat.format(date);
        //Formatting time as a string
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        String timeString = timeFormat.format(date);
        //Display date and time strings
        holder.tv.setText("Calories count: "+foodRecord.caloriesCount+"calories\nTime: "+timeString+"\nDate: "+dateString);
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setmList(List<FoodRecord> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.text_view);
        }
    }
}
