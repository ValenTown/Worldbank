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
import android.text.method.ScrollingMovementMethod;
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
import com.progettoMP2018.clashers.worldbank.adapter.TopicsAdapter;
import com.progettoMP2018.clashers.worldbank.dao.DBHelper;
import com.progettoMP2018.clashers.worldbank.entity.FullQuery;
import com.progettoMP2018.clashers.worldbank.entity.Topic;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ActivityTopic extends AppCompatActivity implements TopicsAdapter.TopicsAdapterListener {
    private static final String TAG = ActivityTopic.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Topic> topicList;
    private TopicsAdapter mAdapter;
    private SearchView searchView;
    private Dialog myDialog;
    private Button btnSelectTopic;

    //url del json da "fetchare"
    private static final String URL = "http://api.worldbank.org/v2/topics/?format=json";
    FullQuery fullQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        //Se la selezione parte da Country, si estrapola l'oggetto FullQuery precedentemente istanziato
        //nella CountryActivity che avrà già il valore di country impostato, altrimenti fullQuery viene impostato a null
        fullQuery = (FullQuery) getIntent().getSerializableExtra("country_selected");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //Setta la toolbar per funzionare come se fosse la ActionBar per questa activity

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //aggiunge "<" a sinistra della scritta nella toolbar
        getSupportActionBar().setTitle(R.string.toolbar_topics_title);//setta il titolo della toolbar
        recyclerView = findViewById(R.id.recycler_view);
        topicList = new ArrayList<>();
        mAdapter = new TopicsAdapter(this, topicList, this);

        // barra di notifica col background bianco
        whiteNotificationBar(recyclerView);
        //si crea il layout tramite recyclerview
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);

        fetchTopics(); //si chiama la funzione di fetch dei topics da json
    }


    private void fetchTopics() { //funzione che fetcha il json riguardante i topics

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
                                Toast.makeText(getApplicationContext(), R.string.fetch_topics, Toast.LENGTH_LONG).show();
                                return;
                            }
                            parseJsonData(response.toString());
                            helper.addURL(URL, response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // errore nell'uso del file json
                    Log.e(TAG, R.string.error + error.getMessage());
                    Toast.makeText(getApplicationContext(), R.string.error + error.getMessage(), Toast.LENGTH_SHORT).show();
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

            JSONArray topicArray = (new JSONArray(jsonString)).getJSONArray(1); //si prende il primo array nel file json visto che è un array di array e il primo array non serve a nulla
            Gson gson = new Gson(); //tramite il tool gson si trasformano i contenuti del json in oggetti riutilizzabili
            Type listType = new TypeToken<List<Topic>>() {
            }.getType();
            List<Topic> items = gson.fromJson(String.valueOf(topicArray), listType); //si crea una lista di "Topic" tramite gson prendendo il valore degli elementi in topicArray


            topicList.clear();
            topicList.addAll(items);

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
                setResult(RESULT_OK); //da ActivityTopic torno indietro ad ActivityCountry e si deve reimpostare fullQuery a null
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
    public void onTopicSelected(Topic topic) {
        showDialog(topic); //se viene selezionato un topic si mostra il suo contenuto su un nuovo dialog tramite la funzione showDialog
    }

    public void showDialog(final Topic topic) { //mostra un nuovo dialog contenente informazioni riguardo il topic in questione
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogLayout = inflater.inflate(R.layout.topic_details, null);
        myDialog = new Dialog(this);
        myDialog.setContentView(dialogLayout);
        TextView dialog_name = (TextView) myDialog.findViewById(R.id.details_name);
        TextView dialog_sourceNote = (TextView) myDialog.findViewById(R.id.details_sourcenote);
        dialog_sourceNote.setMovementMethod(new ScrollingMovementMethod());
        btnSelectTopic = (Button) dialogLayout.findViewById(R.id.btn_select_topic);
        dialog_name.setText(topic.getValue());
        dialog_sourceNote.setText(topic.getSourceNote());
        myDialog.show();
        btnSelectTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //bottone per selezionare il topic scelto e andare avanti alla nuova activity
                if (fullQuery == null) { //se fullquery è null significa che sto nell'activity topic come prima activity
                    fullQuery = new FullQuery();
                }
                fullQuery.setTopic(topic);  //altrimenti se non è null significa che ho già selezionato topic e quindi vado alla successiva activity ovvero indicator
                Intent i = new Intent(ActivityTopic.this, ActivityIndicator.class);
                i.putExtra("topic_selected", fullQuery);
                myDialog.dismiss();
                startActivity(i);
            }
        });
    }


}