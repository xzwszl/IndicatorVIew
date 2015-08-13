package com.xzw.szl.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.xzw.szl.IndicatorView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {

        final IndicatorView iv = (IndicatorView) findViewById(R.id.iv_test);
        iv.setCount(5);

        iv.post(new Runnable() {

            float coefficient = 0; // from 0 to 1
            int position = 0;

            AccelerateDecelerateInterpolator adi = new AccelerateDecelerateInterpolator();


            @Override
            public void run() {

                iv.setLocationWithCoefficient(position, adi.getInterpolation(coefficient));
                coefficient+=0.05;

                if (coefficient >= 1.0f) {
                    coefficient = 0.0f;
                    position = (position + 1) % 5;
                }

                iv.postDelayed(this,50);
            }
        });
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
