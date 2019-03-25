package ua.pt.solapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    private ArrayList<String> data;
    private Context mContext;
    private Activity mActivity;

    CityAdapter(Context context, ArrayList<String> data, Activity mActivity) {
        this.data = data;
        this.mContext = context;
        this.mActivity = mActivity;
    }

    public void setData(ArrayList<String> data) {this.data = data;}

    @NonNull
    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.city_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CityAdapter.ViewHolder holder, int position) {
        if (data != null){
            if(position % 2 == 0)
            {
                //holder.rootView.setBackgroundColor(Color.BLACK);
                holder.itemView.setBackgroundColor(Color.rgb(230, 230, 230));
            }
            else
            {
                //holder.rootView.setBackgroundColor(Color.WHITE);
                holder.itemView.setBackgroundResource(R.color.cardview_light_background);
            }
            String city = data.get(position);
            holder.bindTo(city);
        }
    }

    @Override
    public int getItemCount() {
        if(data != null)
            return data.size();
        else
            return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        // Member Variables for the TextViews
        private TextView textview;

        public ViewHolder(View itemView)
        {
            super(itemView);
            // Initialize the views.
            textview = itemView.findViewById(R.id.city);

            // Set the OnClickListener to the entire view.
            itemView.setOnClickListener(this);
        }

        void bindTo(String c){
            textview.setText(c);
        }

        public void onClick(View view)
        {
            String c = data.get(getAdapterPosition());
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.putExtra("city", c);
            mActivity.startActivity(intent);
        }

    }
}
