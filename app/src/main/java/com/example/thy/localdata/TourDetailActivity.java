package com.example.thy.localdata;

import java.text.NumberFormat;

import model.Tour;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class TourDetailActivity extends Activity {

	Tour tour;
	
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

        Bundle b = getIntent().getExtras();
        tour = b.getParcelable(".model.Tour");
		refreshDisplay();
		
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

}
