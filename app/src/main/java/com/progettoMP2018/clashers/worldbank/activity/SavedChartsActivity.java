package com.progettoMP2018.clashers.worldbank.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.progettoMP2018.clashers.worldbank.R;
import com.progettoMP2018.clashers.worldbank.adapter.CellAdapter;
import com.progettoMP2018.clashers.worldbank.entity.Cell;
import com.progettoMP2018.clashers.worldbank.utility.BitmapHandler;

import java.util.ArrayList;

public class SavedChartsActivity extends AppCompatActivity implements CellAdapter.CellAdapterListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_charts);
        //si usa la recyclerview per impostare il layout
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.gallery);
        recyclerView.setHasFixedSize(true); //tramite questa funzione la grandezza della recycler view non cambierà a seconda del contenuto che avrà al suo interno

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        //si carica l'immagine dallo storage interno dove viene salvata dall'activity chart
        ArrayList<String> images = BitmapHandler.loadImageFromStorage("/data/data/com.progettoMP2018.clashers.worldbank/app_imageDir");
        if (images.size() == 0) { //se l'array ha grandezza zero significa che non c'è nulla dentro e quindi l'immagine non esiste, restituisce un alert
            showDialog();
        }
        ArrayList<Cell> cells = prepareData(images);
        CellAdapter adapter = new CellAdapter(getApplicationContext(), cells, this);
        recyclerView.setAdapter(adapter); //qui si sistemano le celle ottenute nella prepareData nella recyclerview


    }

    private ArrayList<Cell> prepareData(ArrayList<String> images) { //funzione che sistema un'immagine ottenuta in celle e restituisce l'immagine
        ArrayList<Cell> theimage = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            Cell cell = new Cell();
            cell.setPath(images.get(i));
            theimage.add(cell);
        }
        return theimage;
    }

    public void showDialog() { //funzione che restituisce l'alert collegato al fatto che l'immagine non viene trovata
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(SavedChartsActivity.this);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle(R.string.missing_img);
        alertBuilder.setMessage(R.string.missing_img_description);
        alertBuilder.setPositiveButton(R.string.back,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onCellSelected(Cell cell) { //funzione che, una volta selezionata un'immagine, rimanda all'activity "FullImageActivity" portandosi dietro l'immagine selezionata
        Intent i = new Intent(SavedChartsActivity.this, FullImageActivity.class);
        i.putExtra("image_selected", cell.getPath());
        startActivity(i);
    }
}
