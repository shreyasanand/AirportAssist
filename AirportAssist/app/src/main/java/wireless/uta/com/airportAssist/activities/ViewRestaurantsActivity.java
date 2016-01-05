package wireless.uta.com.airportAssist.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import wireless.uta.com.airportAssist.R;


public class ViewRestaurantsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_restaurants);


        String[] restaurants = getIntent().getStringArrayExtra("restaurants");
        String deviceUID = getIntent().getStringExtra("sid");

        ListView listview= (ListView)findViewById(R.id.viewRestaurants);
        //db entries//
        ImageView image=(ImageView)findViewById(R.id.restaurantZoom);

        if(deviceUID.equals(R.string.sensor_1)){
        image.setImageResource(R.drawable.airportmap1zoom);}
        if(deviceUID.equals(R.string.sensor_2)){
        image.setImageResource(R.drawable.airportmap2zoom);}
        if(deviceUID.equals(R.string.sensor_3)){
        image.setImageResource(R.drawable.airportmap3zoom);}
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, restaurants);
        listview.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_restaurants, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return true;
    }
}