package com.progettoMP2018.clashers.worldbank.activity;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.reflect.TypeToken;
import com.progettoMP2018.clashers.worldbank.MyApplication;
import com.progettoMP2018.clashers.worldbank.utility.MyDividerItemDecoration;
import com.progettoMP2018.clashers.worldbank.R;
import com.progettoMP2018.clashers.worldbank.adapter.CountryAdapter;
import com.progettoMP2018.clashers.worldbank.dao.DBHelper;
import com.progettoMP2018.clashers.worldbank.entity.Country;
import com.progettoMP2018.clashers.worldbank.entity.FullQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ActivityCountry extends AppCompatActivity implements CountryAdapter.CountryAdapterListener {
    private static final String TAG = ActivityCountry.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Country> countryList;
    private CountryAdapter mAdapter;
    private SearchView searchView;
    private Dialog myDialog;
    private Button btnSelectCountry;

    // url del json da "fetchare"
    private static final String URL = "http://api.worldbank.org/v2/countries/?per_page=304&format=json";
    FullQuery fullQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        //Se arriviamo da ActivityIndicator prendiamo l'oggetto FullQuery con l'indicator già impostato in base alla scelta dell'utente
        fullQuery = (FullQuery) getIntent().getSerializableExtra("indicator_selected");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //Setta la toolbar per funzionare come se fosse la ActionBar per questa activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //aggiunge "<" a sinistra della scritta nella toolbar
        getSupportActionBar().setTitle(R.string.toolbar_countries_title); //setta il titolo della toolbar

        recyclerView = findViewById(R.id.recycler_view);
        countryList = new ArrayList<>();
        mAdapter = new CountryAdapter(this, countryList, this);

        // barra di notifica col background bianco
        whiteNotificationBar(recyclerView);
        //si crea il layout tramite recyclerview
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);

        fetchCountry(); //si chiama la funzione di fetch delle country da json
    }

    private void fetchCountry() { //funzione che fetcha il json riguardante le countries

        final DBHelper helper = new DBHelper(this);
        helper.open(); //si apre il db
        Cursor c = helper.getURL(URL);
        if (c.getCount() == 0) { //se il getCount è == 0 significa che non ci sono url, quindi va fatta tutta la procedura da capo
            JsonArrayRequest request = new JsonArrayRequest(URL,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray
                                                       response) {
                            if (response == null) {
                                Toast.makeText(getApplicationContext(), R.string.countries_error_fetch, Toast.LENGTH_LONG).show();
                                return;
                            }
                            parseJsonData(response.toString());
                            helper.addURL(URL, response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // errore nell'uso del file json
                    Log.e(TAG, getString(R.string.error) + error.getMessage());
                    Toast.makeText(getApplicationContext(), getString(R.string.error) + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            MyApplication.getInstance().addToRequestQueue(request);
        } else { //altrimenti si prende il file json corrispondente all'url dal database
            c.moveToFirst();
            String json = c.getString(c.getColumnIndex("json"));
            parseJsonData(json);
        }


    }

    void parseJsonData(String jsonString) { //funzione che analizza il contenuto del file json
        try {

            JSONArray countryArray = (new JSONArray(jsonString)).getJSONArray(1); //si prende il primo array nel file json visto che è un array di array e il primo array non serve a nulla
            Gson gson = new Gson(); //tramite il tool gson si trasformano i contenuti del json in oggetti riutilizzabili
            //todo: controllare questo commento!
            Type listType = new TypeToken<List<Country>>() {
            }.getType();
            List<Country> items = gson.fromJson(String.valueOf(countryArray), listType); //si crea una lista di "Country" tramite gson prendendo il valore degli elementi in countryArray

            countryList.clear();
            countryList.addAll(items);

            //si fa il refresh della recycler view
            mAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }


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
                setResult(RESULT_OK); //da ActivityCountry torno indietro ad ActivityIndicator e devo reimpostare fullQuery a null
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
    public void onCountrySelected(Country country) {
        showDialog(country); //se viene selezionata una country mostro il suo contenuto su un nuovo dialog tramite la funzione showDialog
    }

    public void showDialog(final Country country) { //mostra un nuovo dialog contenente informazioni riguardo la country in questione
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogLayout = inflater.inflate(R.layout.country_details, null);
        myDialog = new Dialog(this);
        myDialog.setContentView(dialogLayout);
        TextView dialog_name = (TextView) myDialog.findViewById(R.id.details_name);
        TextView dialog_capital = (TextView) myDialog.findViewById(R.id.details_capital);
        TextView dialog_other_details = (TextView) myDialog.findViewById(R.id.other_details);
        btnSelectCountry = (Button) dialogLayout.findViewById(R.id.btn_select_country);
        dialog_name.setText(country.getName());
        if (!country.getCapitalCity().equals("")) { //se esiste la capitale della country selezionata allora la mostro, altrimenti no
            dialog_capital.setText(getString(R.string.capital_name, country.getCapitalCity()));
            dialog_other_details.setText(getString(R.string.country_other_details, country.getLongitude(), country.getLatitude()));
        }
        myDialog.show();
        btnSelectCountry.setOnClickListener(new View.OnClickListener() { //bottone per selezionare la country scelta e andare avanti alla nuova activity
            @Override
            public void onClick(View view) {
                if (fullQuery == null) { //se fullquery è null significa che provengo dall'activity country come prima activity e quindi apro l'activity successiva ovvero topic
                    fullQuery = new FullQuery();
                    fullQuery.setCountry(country);
                    Intent i = new Intent(ActivityCountry.this, ActivityTopic.class);
                    i.putExtra("country_selected", fullQuery);
                    myDialog.dismiss();
                    startActivity(i);
                } else { //altrimenti se non è null vado alla successiva activity ovvero country
                    fullQuery.setCountry(country);
                    Intent i = new Intent(ActivityCountry.this, ActivityChart.class);
                    i.putExtra("item_selected", fullQuery);
                    myDialog.dismiss();
                    startActivity(i);
                }

            }
        });
    }


}
