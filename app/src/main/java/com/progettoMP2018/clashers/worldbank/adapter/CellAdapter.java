package com.progettoMP2018.clashers.worldbank.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.progettoMP2018.clashers.worldbank.R;
import com.progettoMP2018.clashers.worldbank.entity.Cell;
import java.util.ArrayList;

public class CellAdapter extends RecyclerView.Adapter<CellAdapter.ViewHolder> {
    private ArrayList<Cell> galleryList;
    private CellAdapterListener listener;
    private Context context;

    public CellAdapter(Context context, ArrayList<Cell> galleryList, CellAdapterListener listener) {
        this.galleryList = galleryList;
        this.context = context;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) { //momento in cui un elemento della RecyclerView viene creato
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.saved_chart_cell, viewGroup, false);//Se attachToRoot è impostato su false, il file di layout specificato nel primo parametro viene
                                                                                                                                //gonfiato e non collegato al ViewGroup specificato nel secondo parametro,
                                                                                                                                //ma la visualizzazione gonfiata acquisisce LayoutParams del genitore che consente a tale visualizzazione di adattarsi correttamente al padre
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CellAdapter.ViewHolder viewHolder, final int i) { //momento in cui vengono recuperati i riferimenti agli elementi interni della RecyclerView da popolare con i nuovi dati
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP); //setta il modo di visualizzazione dell'immagine
        viewHolder.img.setImageBitmap(BitmapFactory.decodeFile(galleryList.get(i).getPath())); //decodifica la bmp passata come parametro
        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCellSelected(galleryList.get(i)); //al click su un'immagine nella lista restituisce quella stessa immagine
            }
        });
    }

    @Override
    public int getItemCount() { //restituisce il numero di elementi nella gallerylist
        return galleryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder { //viewholder viene usato per ridurre le invocazioni al metodo findviewbyid, si riciclano il
                                                              //più possibile le view usate per visualizzare elementi, e il viewholder conserva i riferimenti
                                                              //ai widget interni ad ogni elemento
        private TextView title;
        private ImageView img;
        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            img = (ImageView) view.findViewById(R.id.img);
        }
    }

    public interface CellAdapterListener {
        void onCellSelected(Cell cell);
    }
}
