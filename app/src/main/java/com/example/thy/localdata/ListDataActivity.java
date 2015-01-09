package com.example.thy.localdata;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import db.ToursDataSource;
import model.Tour;


public class ListDataActivity extends ListActivity {

    private static final String LOGTAG = "LD";
    public static final String USERNAME = "pref_username";
    public static final String  VIEWIMAGE = "example_checkbox";
    private static final int TOUR_DETAIL_ACTIVITY = 1001;
//    SQLiteOpenHelper dbhelper;
//    SQLiteDatabase database;
    ToursDataSource dataSource;
    SharedPreferences settings;
    List<Tour> tours;
    Boolean isMyTours;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);
        settings = PreferenceManager.getDefaultSharedPreferences(this);

       // getActionBar().setDisplayShowHomeEnabled(true);
//        List<Tour> tours = getFromXMLParser();

//        dbhelper = new ToursDBOpenHelper(this);
//        database = dbhelper.getWritableDatabase();
        dataSource = new ToursDataSource(this);
        dataSource.open();

        Intent intent = getIntent();
        isMyTours = intent.getBooleanExtra("ShowMyTours", true);
        if (isMyTours){
            tours = dataSource.findMyTours();
        } else {

            tours = dataSource.findAll();
            if (tours.size() == 0) {
                createData();
                tours = dataSource.findAll();
            }
        }
        refreshDisplay();
    }

    private void refreshDisplay() {

        if (settings.getBoolean(VIEWIMAGE,false)){
            ArrayAdapter<Tour> adapter = new TourListAdapter(this,tours);
            setListAdapter(adapter);
        } else {
            ArrayAdapter<Tour> adapter = new ArrayAdapter<Tour>(this,android.R.layout.simple_list_item_1,tours);
            setListAdapter(adapter);
        }
    }


    private List<Tour> getFromXMLParser(){
        //ToursPullParser parser = new ToursPullParser();
        ToursJDOMParser parser = new ToursJDOMParser();
        List<Tour> tours = parser.parseXML(this);
        return  tours;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }

    public void createData(){
        ToursJDOMParser parser = new ToursJDOMParser();
        List<Tour> tours = parser.parseXML(this);

        for (Tour tour : tours){
            dataSource.create(tour);
        }

        /*Tour tour = new Tour();
        tour.setTitle("Phnom Penh");
        tour.setDescription("A tour to Phnom Penh");
        tour.setPrice(500);
        tour.setImage("phnompenh");
        tour = dataSource.create(tour);
        Log.i(LOGTAG, "Tour created with id:" + tour.getId());

        tour = new Tour();
        tour.setTitle("Siem Reap");
        tour.setDescription("A tour to Siem Reap");
        tour.setPrice(700);
        tour.setImage("siemreap");
        tour = dataSource.create(tour);
        Log.i(LOGTAG, "Tour created with id:" + tour.getId());

        tour = new Tour();
        tour.setTitle("Rattanakiri");
        tour.setDescription("A tour to Rattanakiri");
        tour.setPrice(1500);
        tour.setImage("rattanakiri");
        tour = dataSource.create(tour);
        Log.i(LOGTAG, "Tour created with id:" + tour.getId());*/

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Tour tour = tours.get(position);
        Intent intent = new Intent(this, TourDetailActivity.class);
        intent.putExtra(".model.Tour", tour);
        intent.putExtra("isMyTours", isMyTours);
        //startActivity(intent);
        startActivityForResult(intent, TOUR_DETAIL_ACTIVITY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TOUR_DETAIL_ACTIVITY && isMyTours && resultCode==-1){
            dataSource.open();
            tours = dataSource.findMyTours();
            refreshDisplay();
        }
    }
}


