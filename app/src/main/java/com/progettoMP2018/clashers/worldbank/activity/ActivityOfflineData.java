package com.progettoMP2018.clashers.worldbank.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.progettoMP2018.clashers.worldbank.utility.MyDividerItemDecoration;
import com.progettoMP2018.clashers.worldbank.R;
import com.progettoMP2018.clashers.worldbank.adapter.OfflineDataAdapter;
import com.progettoMP2018.clashers.worldbank.dao.DBHelper;
import com.progettoMP2018.clashers.worldbank.entity.SavedRequest;

import java.util.ArrayList;
import java.util.List;

public class ActivityOfflineData extends AppCompatActivity implements OfflineDataAdapter.OfflineDataAdapterListener {

    private static final String TAG = ActivityTopic.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<SavedRequest> elementList;
    private OfflineDataAdapter mAdapter;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_data);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //Setta la toolbar per funzionare come se fosse la ActionBar per questa activity


        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //aggiunge "<" a sinistra della scritta nella toolbar
        getSupportActionBar().setTitle(R.string.toolbar_offline_data_title); //setta il titolo della toolbar

        recyclerView = findViewById(R.id.recycler_view);
        elementList = new ArrayList<>();
        mAdapter = new OfflineDataAdapter(this, elementList, this);

        // barra di notifica col background bianco
        whiteNotificationBar(recyclerView);
        //si crea il layout tramite recyclerview
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);

        getElementsFromDB(); //qui si invoca la funzione per prendere gli elementi dal db


    }

    public void getElementsFromDB() { //funzione che serve a prendere gli elementi salvati nel db
        DBHelper helper = new DBHelper(this);
        helper.open(); //si apre il db
        Cursor c = helper.getSavedRequests();
        while (c.moveToNext()) { //finchè ci sono elementi si va avanti a cercare
            String json = c.getString(c.getColumnIndex("json"));
            String topic = c.getString(c.getColumnIndex("topic"));
            String indicator = c.getString(c.getColumnIndex("indicator"));
            String country = c.getString(c.getColumnIndex("country"));
            String indicatorId = c.getString(c.getColumnIndex("indicator_id"));
            String countryIso2code = c.getString(c.getColumnIndex("country_iso2code"));

            SavedRequest savedRequest = new SavedRequest(json, topic, indicator, country, indicatorId, countryIso2code); //oggetto contentente tutti i dati specificati nel ciclo while sottostante
            elementList.add(savedRequest); //si aggiunge l'oggetto appena creato dentro una lista

        }
        helper.close(); //si chiude il db
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //configurazione per ricercare nella activity tramite SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        //in ascolto per cercare nell'activity in base a modifiche sul testo della query del campo di ricerca
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filtra il contenuto dell'activity quando una query nel campo di ricerca viene mandata in ricerca
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //filtra il contenuto dell'activity quando il testo della query cambia
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * qui si gestiscono i click sull'action bar. L'action bar gestirà
         * automaticamente i click sul bottone, fin quando si specificherà una activity
         * genitore in AndroidManifest.xml
         */
        int id = item.getItemId();

        switch (id) {
            // qui si reagisce al bottone sull'action bar
            case R.id.action_search:
                return true;

            case android.R.id.home:
                setResult(RESULT_OK); //da ActivityOfflineData torno indietro ad ActivityMain e devo reimpostare fullQuery a null
                                      //todo: controllare commento
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //si chiude la search view quando si clicca il pulsante di back
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) { //setta la notificationbar di colore bianco
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onElementSelected(SavedRequest element) { //se viene selezionato un elemento si mostra il suo contenuto sul corrispondente chart
        Intent i = new Intent(ActivityOfflineData.this, ActivityChart.class);
        i.putExtra("saved_request", element); //ci si porta dietro la saved request impostata all'inizio
        startActivity(i);

    }

}
