package com.example.thy.localdata;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import model.Tour;
import utils.UIHelper;

// This is a test of changes in VCS what
public class MainActivity extends ActionBarActivity {

    public static final String LOGTAG = "LD";
    public static final String USERNAME = "pref_username";
    public static final String  VIEWIMAGE = "example_checkbox";
    SharedPreferences settings;
    SharedPreferences.OnSharedPreferenceChangeListener listener;

    private File file;
    private static final String FILENAME = "jsondata";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //settings = getPreferences(MODE_PRIVATE);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                MainActivity.this.showPreferences(null);
            }
        };
        settings.registerOnSharedPreferenceChangeListener(listener);

        //File f = getFilesDir();
        //String path = f.getAbsolutePath();
        //UIHelper.displayText(this, R.id.textView, path);

        File extDir = getExternalFilesDir(null);
        String path = extDir.getAbsolutePath();
        UIHelper.displayText(this, R.id.textView, path);

        file = new File(extDir, FILENAME);

//        ToursPullParser parser = new ToursPullParser();
//        List<Tour> tours = parser.parseXML(this);
//
//        ArrayAdapter<Tour> adapter = new ArrayAdapter<Tour>(this,android.R.layout.simple_list_item_2,tours);
//        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setPreferences(View v)    {
        Log.i(LOGTAG, "Set was Clicked!");
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(USERNAME, UIHelper.getText(this,R.id.etUsername));
        editor.commit();
        UIHelper.displayText(this, R.id.textView,"Preferences saved!");
    }

    public void showPreferences(View v){
        Log.i(LOGTAG, "Show was Clicked!");
        String prefValue = settings.getString(USERNAME,"Not found");
        UIHelper.displayText(this,R.id.textView,prefValue);
        UIHelper.setCBChecked(this,R.id.cbViewImage,settings.getBoolean(VIEWIMAGE, false));

        /*Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);*/
    }

    public void createFile(View v) throws IOException {
        String text = UIHelper.getText(this,R.id.etUsername);
        FileOutputStream fos = openFileOutput("txtfile1.txt", MODE_PRIVATE);
        fos.write(text.getBytes());
        fos.close();

        UIHelper.displayText(this,R.id.textView,"File written to disk!");
    }

    public void readFile(View v) throws IOException {
        FileInputStream fis = openFileInput("txtfile1.txt");
        BufferedInputStream bis = new BufferedInputStream(fis);
        StringBuffer sb = new StringBuffer();
        while (bis.available()!=0){
            char c = (char) fis.read();
            sb.append(c);
        }
        bis.close();
        fis.close();

        UIHelper.displayText(this, R.id.textView, sb.toString());
    }

    public void createJSON(View v) throws IOException, JSONException {
        /*JSONArray data = new JSONArray();
        JSONObject tour;

        tour = new JSONObject();
        tour.put("tour", "Phnom Penh");
        tour.put("price", 500);
        data.put(tour);

        tour = new JSONObject();
        tour.put("tour", "Siem Reap");
        tour.put("price", 700);
        data.put(tour);

        tour = new JSONObject();
        tour.put("tour", "Rattanakiri");
        tour.put("price", 1500);
        data.put(tour);*/
//        if (!checkExternalStorage()){
//            return;
//        }

        JSONArray data = initJSONData();
        String text = data.toString();
        FileOutputStream fos = openFileOutput("tours", MODE_PRIVATE);
        fos.write(text.getBytes());
        fos.close();

        UIHelper.displayText(this,R.id.textView,"File written to disk!\n" + data.toString());
    }

    public void readJSON(View v) throws IOException , JSONException {
        FileInputStream fis = openFileInput("tours");
        BufferedInputStream bis = new BufferedInputStream(fis);
        StringBuffer sb = new StringBuffer();
        while (bis.available()!=0){
            char c = (char) fis.read();
            sb.append(c);
        }
        bis.close();
        fis.close();

        JSONArray data = new JSONArray(sb.toString());
        StringBuffer toursBuffer = new StringBuffer();
        for (int i=0 ; i < data.length();i++){
//            String tour = data.getJSONObject(i).getString("tour");
//            toursBuffer.append(tour + " : " + data.getJSONObject(i).getInt("price") + "\n");
            toursBuffer.append(
                    data.getJSONObject(i).getString("tour")
                    + " : " +
                    data.getJSONObject(i).getInt("price")
                    + "\n");
        }

        UIHelper.displayText(this, R.id.textView, toursBuffer.toString());
    }

    private JSONArray initJSONData() throws JSONException {
        JSONArray data = new JSONArray();
        JSONObject tour;

        tour = new JSONObject();
        tour.put("tour", "Phnom Penh");
        tour.put("price", 500);
        data.put(tour);

        tour = new JSONObject();
        tour.put("tour", "Siem Reap");
        tour.put("price", 700);
        data.put(tour);

        tour = new JSONObject();
        tour.put("tour", "Rattanakiri");
        tour.put("price", 1500);
        data.put(tour);

        return data;
    }

    public void createFileExt(View v) throws IOException, JSONException {

        if(!checkExternalStorage()) return;

        JSONArray data = initJSONData();
        String text = data.toString();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(text.getBytes());
        fos.close();

        UIHelper.displayText(this,R.id.textView,"File written to disk!\n" + data.toString());
    }

    public void readFileExt(View v) throws IOException , JSONException {
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        StringBuffer sb = new StringBuffer();
        while (bis.available()!=0){
            char c = (char) fis.read();
            sb.append(c);
        }
        bis.close();
        fis.close();

        JSONArray data = new JSONArray(sb.toString());
        StringBuffer toursBuffer = new StringBuffer();
        for (int i=0 ; i < data.length();i++){
//            String tour = data.getJSONObject(i).getString("tour");
//            toursBuffer.append(tour + " : " + data.getJSONObject(i).getInt("price") + "\n");
            toursBuffer.append(
                    data.getJSONObject(i).getString("tour")
                            + " : " +
                            data.getJSONObject(i).getInt("price")
                            + "\n");
        }

        UIHelper.displayText(this, R.id.textView, toursBuffer.toString());
    }

    public boolean checkExternalStorage(){
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        } else if(state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)){
            UIHelper.displayText(this, R.id.textView,"External storage is read-only");
        } else {
            UIHelper.displayText(this, R.id.textView,"External storage is unavailable");
        }
        return false;
    }

    public void showListData(View v){
        Intent intent = new Intent(this, ListDataActivity.class);
        intent.putExtra("ShowMyTours", false);
        startActivity(intent);
    }

    public void showListMyTours(View v){
        Intent intent = new Intent(this, ListDataActivity.class);
        intent.putExtra("ShowMyTours", true);
        startActivity(intent);
    }
}
