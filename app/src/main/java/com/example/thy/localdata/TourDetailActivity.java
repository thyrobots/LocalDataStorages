package com.example.thy.localdata;

import java.text.NumberFormat;

import db.ToursDataSource;
import model.Tour;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static db.ToursDataSource.*;

public class TourDetailActivity extends Activity {

	Tour tour;
    ToursDataSource dataSource;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tour_detail);
		
//		tour = new Tour();
//		tour.setId(1);
//		tour.setTitle("Tour title");
//		tour.setDescription("Tour description");
//		tour.setPrice(999);
//		tour.setImage("map_winecountry");
        final Button add_tour_btn = (Button) findViewById(R.id.add_mytour_btn);
        final Button delete_tour_btn = (Button) findViewById(R.id.delete_mytour_btn);
        add_tour_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMyTour();
            }
        });
        delete_tour_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMyTour();
            }
        });

        if (getIntent().getBooleanExtra("isMyTours",true)){
            add_tour_btn.setVisibility(View.INVISIBLE);
        } else {
            delete_tour_btn.setVisibility(View.VISIBLE);
        }

        Bundle b = getIntent().getExtras();
        tour = b.getParcelable(".model.Tour");
		refreshDisplay();
        dataSource = new ToursDataSource(this);
	}

    private void deleteMyTour() {
        // Do delete
        if (dataSource.deleteMyTours(tour)){
            Toast.makeText(this, "Tour has been deleted!", Toast.LENGTH_LONG).show();
            Log.i(MainActivity.LOGTAG, "Tour has been deleted!");
            setResult(-1);
            finish();
        } else {

        }
    }

    private void refreshDisplay() {
		
		TextView tv = (TextView) findViewById(R.id.titleText);
		tv.setText(tour.getTitle());
		
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		tv = (TextView) findViewById(R.id.priceText);
		tv.setText(nf.format(tour.getPrice()));
		
		tv = (TextView) findViewById(R.id.descText);
		tv.setText(tour.getDescription());
		
		ImageView iv = (ImageView) findViewById(R.id.imageView1);
        int imageResource = getResources().getIdentifier(
        		tour.getImage(), "drawable", getPackageName());
        if (imageResource != 0) {
        	iv.setImageResource(imageResource);
        }
	}

    private void addMyTour(){
        if (dataSource.addToMyTours(tour)){
            Toast.makeText(this, "Tour has been added!", Toast.LENGTH_LONG).show();
            Log.i(MainActivity.LOGTAG, "Tour has been added!");
        } else {
            Toast.makeText(this, "Tour has not been added!", Toast.LENGTH_LONG).show();
            Log.i(MainActivity.LOGTAG, "Tour has not been added!");
        }
    }

    protected void onResume() {
        super.onResume();
        dataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }

}
