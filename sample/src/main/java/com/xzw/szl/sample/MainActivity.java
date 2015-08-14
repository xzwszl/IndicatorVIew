package com.xzw.szl.sample;

import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xzw.szl.IndicatorView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    IndicatorView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {

        String a = Environment.getExternalStorageDirectory().toString();

        iv = (IndicatorView) findViewById(R.id.iv_test);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_container);
        viewPager.setAdapter(new PagerAdapter() {

            private int[] imgs = new int[]{R.drawable.lyf,R.drawable.lyf1,R.drawable.lyf2,R.drawable.lyf3,R.drawable.lyf4};

            @Override
            public int getCount() {
                return imgs.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == (View) object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.MATCH_PARENT));

                imageView.setImageResource(imgs[position]);

                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                container.addView(imageView);

                return imageView;
            }
        });

        iv.setViewPager(viewPager);
        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    Method method = ViewPager.class.getDeclaredMethod("setCurrentItemInternal", int.class, boolean.class, boolean.class, int.class);
                    method.setAccessible(true);
                    method.invoke(viewPager, (viewPager.getCurrentItem() + 1) % viewPager.getAdapter().getCount(), true, true, 1);

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                viewPager.postDelayed(this,5000);
            }
        },5000);
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
       switch (id) {
           case R.id.action_Left:
               iv.setGravity(IndicatorView.LEFT);
               return true;
           case R.id.action_center:
               iv.setGravity(IndicatorView.CENTER);
               return true;
           case R.id.action_right:
               iv.setGravity(IndicatorView.RIGHT);
               return true;

           case R.id.action_oval:
               iv.setType(IndicatorView.INDICATOR_OVAL);
               return true;
           case R.id.action_rect:
               iv.setType(IndicatorView.INDICATOR_RECTANGLE);
               return true;
           default:break;
       }
        return super.onOptionsItemSelected(item);
    }
}
