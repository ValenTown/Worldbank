package com.progettoMP2018.clashers.worldbank.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

import com.progettoMP2018.clashers.worldbank.R;
import com.progettoMP2018.clashers.worldbank.dao.DBHelper;
import com.progettoMP2018.clashers.worldbank.utility.CacheHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView loadTopicActivity = (CardView) findViewById(R.id.btn_load_topics);
        CardView loadCountryActivity = (CardView) findViewById(R.id.btn_load_countries);
        CardView loadSavedImages = (CardView) findViewById(R.id.btn_saved_charts);
        CardView loadOfflineFiles = (CardView) findViewById(R.id.btn_research_history);
        CardView deleteCache = (CardView) findViewById(R.id.btn_delete_cache);

        loadTopicActivity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) { //al click sul pulsante "topic" si carica l'activity corrispondente "Activity Topic"
                Intent intent = new Intent(MainActivity.this, ActivityTopic.class);
                startActivity(intent);
            }
        });


        loadCountryActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //al click sul pulsante "country" si carica l'activity corrispondente "Activity Country"
                Intent intent = new Intent(MainActivity.this, ActivityCountry.class);
                startActivity(intent);
            }
        });

        loadSavedImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //al click sul pulsante "show saved images" si carica l'activity corrispondente "SavedChartsActivity"
                Intent intent = new Intent(MainActivity.this, SavedChartsActivity.class);
                startActivity(intent);
            }
        });

        loadOfflineFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //al click sul pulsante "load offline files" si carica l'activity corrispondente "ActivityOfflineData"
                Intent intent = new Intent(MainActivity.this, ActivityOfflineData.class);
                startActivity(intent);
            }
        });

        deleteCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //al click sul pulsante "delete cache" si fa partire la cancellazione della cache dell'app
                CacheHandler.deleteCache(MainActivity.this);
                DBHelper helper = new DBHelper(MainActivity.this);
                helper.open(); //apre il db
                helper.deleteAllJson(); //cancella tutti i json memorizzati
                helper.deleteSavedRequests(); //cancella tutte le "requests" salvate per esser usate nelle varie activity
                helper.close(); //chiude il db
            }
        });
    }
}
