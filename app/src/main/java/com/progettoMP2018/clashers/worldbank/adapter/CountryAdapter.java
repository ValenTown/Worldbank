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
import com.progettoMP2018.clashers.worldbank.entity.Country;

import java.util.ArrayList;
import java.util.List;


public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Country> countryList;
    private List<Country> countryListFiltered;
    private CountryAdapterListener listener;


    public class MyViewHolder extends RecyclerView.ViewHolder { //viewholder viene usato per ridurre le invocazioni al metodo findviewbyid, si riciclano il
                                                                //più possibile le view usate per visualizzare elementi, e il viewholder conserva i riferimenti
                                                                //ai widget interni ad ogni elemento
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
                    //country selezionata
                    listener.onCountrySelected(countryListFiltered.get(getAdapterPosition()));

                }
            });
        }
    }


    public CountryAdapter(Context context, List<Country> countryList, CountryAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.countryList = countryList;
        this.countryListFiltered = countryList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //momento in cui un elemento della RecyclerView viene creato
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item, parent, false); //Se attachToRoot è impostato su false, il file di layout specificato nel primo parametro viene
                                                                       //gonfiato e non collegato al ViewGroup specificato nel secondo parametro,
                                                                       //ma la visualizzazione gonfiata acquisisce LayoutParams del genitore che consente a tale visualizzazione di adattarsi correttamente al padre

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) { //momento in cui vengono recuperati i riferimenti agli elementi interni della RecyclerView da popolare con i nuovi dati
        final Country country = countryListFiltered.get(position);
        holder.name.setText(country.getName());
        Drawable myDrawable = context.getResources().getDrawable(R.drawable.country);
        holder.thumbnail.setImageDrawable(myDrawable);
    }

    @Override
    public int getItemCount() { //restituisce il numero di elementi nella countrylist
        return countryListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty() || charSequence.length() < 3) {
                    countryListFiltered = countryList;
                } else { //il filtro sulla ricerca inizia quando i caratteri inseriti sono almeno 3
                    List<Country> filteredList = new ArrayList<>();
                    for (Country row : countryList) {

                        //se la row contiene la sequenza di caratteri inserita, allora aggiunge la row alla filteredlist
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    countryListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = countryListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                countryListFiltered = (ArrayList<Country>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public interface CountryAdapterListener {
        void onCountrySelected(Country country);
    }
}
