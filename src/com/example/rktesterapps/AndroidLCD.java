package com.example.rktesterapps;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class AndroidLCD extends Activity {
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lcd);
		
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		
		PagerAdapter adapter = new CustomAdapter(AndroidLCD.this);
		viewPager.setAdapter(adapter);

	}

}
