package com.progettoMP2018.clashers.worldbank.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.Filterable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.progettoMP2018.clashers.worldbank.R;
import com.progettoMP2018.clashers.worldbank.entity.Indicator;


import java.util.ArrayList;
import java.util.List;


public class IndicatorAdapter extends RecyclerView.Adapter<IndicatorAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Indicator> indicatorList;
    private List<Indicator> indicatorListFiltered;
    private IndicatorAdapterListener listener;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, sourceNotes;
        public ImageView thumbnail;


        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            sourceNotes = view.findViewById(R.id.sourceNote);
            thumbnail = view.findViewById(R.id.thumbnail);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected topic in callback
                    listener.onIndicatorSelected(indicatorListFiltered.get(getAdapterPosition()));

                }
            });
        }
    }


    public IndicatorAdapter(Context context, List<Indicator> indicatorList, IndicatorAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.indicatorList = indicatorList;
        this.indicatorListFiltered = indicatorList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Indicator indicator = indicatorListFiltered.get(position);
        holder.name.setText(indicator.getName());
        Drawable myDrawable = context.getResources().getDrawable(R.drawable.indicator);
        holder.thumbnail.setImageDrawable(myDrawable);
    }

    @Override
    public int getItemCount() {
        return indicatorListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty() || charSequence.length() < 3) {
                    indicatorListFiltered = indicatorList;
                } else { //il filtro sulla ricerca inizia quando i caratteri inseriti sono almeno 3
                    List<Indicator> filteredList = new ArrayList<>();
                    for (Indicator row : indicatorList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    indicatorListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = indicatorListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                indicatorListFiltered = (ArrayList<Indicator>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public interface IndicatorAdapterListener {
        void onIndicatorSelected(Indicator indicator);
    }
}
