package wireless.uta.com.airportAssist.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;

import wireless.uta.com.airportAssist.R;
import wireless.uta.com.airportAssist.dataobjects.Flight;
import wireless.uta.com.airportAssist.helpers.AsyncHttpTaskGet;
import wireless.uta.com.airportAssist.helpers.AsyncHttpTaskPost;


public class MainActivity extends Activity {

    private static final long SCAN_PERIOD = 100000;

    private final String sensorId_1 = getString(R.string.sensor_1);
    private final String sensorId_2 = getString(R.string.sensor_2);
    private final String sensorId_3 = getString(R.string.sensor_3);
    private final String location_indicator_str = getString(R.string.location_indicator);

    private String TAG = this.getClass().getSimpleName();
    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    private boolean deviceFound = false;
    private ArrayList<BluetoothDevice> mFoundDevices;
    private String deviceUID;
    private AlertDialog alertDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler();
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }

        final Button btn_start= (Button)findViewById(R.id.start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start scanning for ble sensor tag
                scanLeDevice(true);
                btn_start.setEnabled(false);
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
    protected void onResume() {
        super.onResume();
        // Initializes found devices list
        mFoundDevices = new ArrayList<BluetoothDevice>();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFoundDevices.clear();
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

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String deviceAddress = device.getAddress();
                            if(deviceAddress.equals(sensorId_1)
                                    || deviceAddress.equals(sensorId_2)
                                    || deviceAddress.equals(sensorId_3)) {
                                if(!mFoundDevices.contains(device)) {
                                    mFoundDevices.add(device);
                                    notifyDataSetChanged();
                                }
                            }
                        }
                    });
                }
            };

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    /*
    This method is called each time there is a new sensor tag discovered.
    Based on the sensor tag uid it builds and displays the corresponding alert dialog box.
     */
    public void notifyDataSetChanged() {
        for(BluetoothDevice b: mFoundDevices) {
            ImageView image;
            AlertDialog.Builder alertDialogBuilder;
            deviceUID = b.getAddress();
            Log.i(TAG,"Device found: "+deviceUID);

            if(alertDialog!=null) {
                Log.i(TAG,"Cancelling alert");
                alertDialog.cancel();
            }

            switch (deviceUID) {
                case "78:A5:04:8C:2D:AD":
                    Log.i(TAG,"Device found: "+deviceUID);

                    image= new ImageView(this);
                    image.setImageResource(R.drawable.airportmap1);

                    alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    // set title
                    alertDialogBuilder.setTitle(location_indicator_str);
                    // set dialog message
                    alertDialogBuilder.setView(image);

                    alertDialogBuilder.setCancelable(false)
                            .setPositiveButton("View Restaurants",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, close
                                    dialog.cancel();
                                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

                                    //define parameters
                                    postParameters.add(new BasicNameValuePair("sid",deviceUID));

                                    AsyncHttpTaskPost httpWrapper = new AsyncHttpTaskPost();
                                    httpWrapper.setPostParameters(postParameters);
                                    httpWrapper.setFlightActivity(MainActivity.this);
                                    String url = "http://omega.uta.edu/~sxa1001/getRestaurants.php";
                                    HttpPost httpPost = new HttpPost(url);
                                    try{
                                        URI uri = new URI(url);
                                        httpWrapper.execute(uri);
                                    } catch (Exception e) {
                                        Log.e(TAG,"Error in http connection " + e.toString());
                                    }
                                }
                            })
                            .setNegativeButton("View Flights",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                    Calendar c = Calendar.getInstance();
                                    int date = c.get(Calendar.DATE);
                                    int month= c.get(Calendar.MONTH)+1;
                                    int year= c.get(Calendar.YEAR);
                                    int hour = c.get(Calendar.HOUR);

                                    AsyncHttpTaskGet httpGetWrapper = new AsyncHttpTaskGet();
                                    httpGetWrapper.setFlightActivity(MainActivity.this);
                                    try {
                                        URI uri = new URI("https://api.flightstats.com/flex/flightstatus/rest/v2/json/homescreen/status/DAL/dep/"+year+"/"+month+"/"+date+"/"+hour+"?appId=dbe625fa&appKey=ea8936ca56fd7b6c9812ed2c88fa9ec4&utc=false&numHours=1&maxFlights=5");
                                        httpGetWrapper.execute(uri);
                                    } catch (URISyntaxException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                    // create alert dialog
                    alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                    break;

                case "B4:99:4C:64:B9:46":
                    Log.i(TAG,"Device found: "+deviceUID);
                    image= new ImageView(this);
                    image.setImageResource(R.drawable.airportmap2);

                    alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    // set title
                    alertDialogBuilder.setTitle(location_indicator_str);
                    // set dialog message
                    alertDialogBuilder.setView(image);

                    alertDialogBuilder.setCancelable(false)
                            .setPositiveButton("View Restaurants",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, close
                                    dialog.cancel();
                                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

                                    //define parameters
                                    postParameters.add(new BasicNameValuePair("sid",deviceUID));

                                    AsyncHttpTaskPost httpWrapper = new AsyncHttpTaskPost();
                                    httpWrapper.setPostParameters(postParameters);
                                    httpWrapper.setFlightActivity(MainActivity.this);
                                    String url = "http://omega.uta.edu/~sxa1001/getRestaurants.php";
                                    HttpPost httpPost = new HttpPost(url);
                                    try{
                                        URI uri = new URI(url);
                                        httpWrapper.execute(uri);
                                    } catch (Exception e) {
                                        Log.e(TAG,"Error in http connection " + e.toString());
                                    }

                                }
                            })
                            .setNegativeButton("View Flights",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                    Calendar c = Calendar.getInstance();
                                    int date = c.get(Calendar.DATE);
                                    int month= c.get(Calendar.MONTH)+1;
                                    int year= c.get(Calendar.YEAR);
                                    int hour = c.get(Calendar.HOUR);

                                    AsyncHttpTaskGet httpGetWrapper = new AsyncHttpTaskGet();
                                    httpGetWrapper.setFlightActivity(MainActivity.this);

                                    try{
                                        URI uri = new URI("https://api.flightstats.com/flex/flightstatus/rest/v2/json/homescreen/status/DAL/dep/"+year+"/"+month+"/"+date+"/"+hour+"?appId=dbe625fa&appKey=ea8936ca56fd7b6c9812ed2c88fa9ec4&utc=false&numHours=1&maxFlights=5");
                                        httpGetWrapper.execute(uri);
                                    } catch (Exception e) {
                                        Log.e(TAG,"Error in http connection " + e.toString());
                                    }

                                }
                            });
                    // create alert dialog
                    alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                    break;

                case "B4:99:4C:64:33:DC":
                    Log.i(TAG,"Device found: "+deviceUID);
                    image= new ImageView(this);
                    image.setImageResource(R.drawable.airportmap3);

                    alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    // set title
                    alertDialogBuilder.setTitle(location_indicator_str);
                    // set dialog message
                    alertDialogBuilder.setView(image);
                    // .setMessage("Delete this profile?")

                    alertDialogBuilder.setCancelable(false)
                            .setPositiveButton("View Restaurants",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, close
                                    dialog.cancel();
                                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

                                    //define parameters
                                    postParameters.add(new BasicNameValuePair("sid",deviceUID));

                                    AsyncHttpTaskPost httpWrapper = new AsyncHttpTaskPost();
                                    httpWrapper.setPostParameters(postParameters);
                                    httpWrapper.setFlightActivity(MainActivity.this);
                                    String url = "http://omega.uta.edu/~sxa1001/getRestaurants.php";
                                    HttpPost httpPost = new HttpPost(url);
                                    try{
                                        URI uri = new URI(url);
                                        httpWrapper.execute(uri);
                                    } catch (Exception e) {
                                        Log.e(TAG,"Error in http connection " + e.toString());
                                    }
                                }
                            })
                            .setNegativeButton("View Flights",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                    Calendar c = Calendar.getInstance();
                                    int date = c.get(Calendar.DATE);
                                    int month= c.get(Calendar.MONTH)+1;
                                    int year= c.get(Calendar.YEAR);
                                    int hour = c.get(Calendar.HOUR);

                                    AsyncHttpTaskGet httpGetWrapper = new AsyncHttpTaskGet();
                                    httpGetWrapper.setFlightActivity(MainActivity.this);
                                    try{
                                        URI uri = new URI("https://api.flightstats.com/flex/flightstatus/rest/v2/json/homescreen/status/DAL/dep/"+year+"/"+month+"/"+date+"/"+hour+"?appId=dbe625fa&appKey=ea8936ca56fd7b6c9812ed2c88fa9ec4&utc=false&numHours=1&maxFlights=5");
                                        httpGetWrapper.execute(uri);
                                    } catch (Exception e) {
                                        Log.e(TAG,"Error in http connection " + e.toString());
                                    }
                                }
                            });
                    // create alert dialog
                    alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                    break;

                default: break;
            }
        }
    }


    public void getRestaurantsDataHandler(String result){
        String[] restaurants = result.split(",");
        Intent intent=new Intent(getApplicationContext(),ViewRestaurantsActivity.class);
        intent.putExtra("restaurants",restaurants);
        intent.putExtra("sid",deviceUID);
        startActivity(intent);
    }

    /*
     This method takes the JSON response from the Flightstats API, parses the JSON data for flight
     details and build the flightList Arraylist and loads the ViewFlightsActivity page.
     */
    public void getFlightsDataHandler(JSONObject result) {
        ArrayList<Flight> flightsList = new ArrayList<Flight>();

        try {
            JSONObject object = result;
            JSONArray flightArray= object.getJSONArray("flightStatuses");
            for(int i=0;i<flightArray.length();i++){
                Flight flight;
                JSONObject flightObject = flightArray.getJSONObject(i);
                String flightNo = flightObject.getString("flightNumber");
                String carrierCode= flightObject.getString("carrierFsCode");
                String status = flightObject.getString("status");
                String departureTerminal = null;
                String departureGate = null;

                if(flightObject.has("airportResources")){
                    JSONObject airportResources = flightObject.getJSONObject("airportResources");
                    if(airportResources.has("departureTerminal")) {
                        departureTerminal = airportResources.getString("departureTerminal");
                    }
                    if(airportResources.has("departureGate")) {
                        departureGate = airportResources.getString("departureGate");
                    }
                    flight = new Flight(flightNo,carrierCode,status,departureTerminal,departureGate);
                }else {
                    flight = new Flight(flightNo,carrierCode,status);
                }

                Log.i(TAG,"Creating flight");
                flightsList.add(flight);
            }
            Intent intent= new Intent(getApplicationContext(),ViewFlightsActivity.class);
            intent.putExtra("flightsList",flightsList);
            startActivity(intent);

        } catch(Exception e){
            Log.i(TAG, e.getMessage());
        }
    }
}
