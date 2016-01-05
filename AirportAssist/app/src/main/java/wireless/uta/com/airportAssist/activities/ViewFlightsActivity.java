package wireless.uta.com.airportAssist.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import wireless.uta.com.airportAssist.dataobjects.Flight;
import wireless.uta.com.airportAssist.R;
import wireless.uta.com.airportAssist.dataobjects.Status;

import static android.widget.AdapterView.OnItemClickListener;

public class ViewFlightsActivity extends ActionBarActivity {

    private String TAG = this.getClass().getSimpleName();
    private String flightCarrierNumber,deptGate,deptTerminal;
    private Status status;
    private ArrayList<Flight> flightArrayList = new ArrayList<Flight>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_flights);

        ListView listview= (ListView)findViewById(R.id.flightList);
        this.flightArrayList = (ArrayList<Flight>)getIntent().getSerializableExtra("flightsList");
        final String[] flightNumbers= new String[flightArrayList.size()];
        final Status[] flightStatuses = new Status[flightArrayList.size()];
        final String[] deptTerminals = new String[flightArrayList.size()];
        final String[] deptGates = new String[flightArrayList.size()];

        for(int i=0;i<flightArrayList.size();i++) {
            Flight flight = flightArrayList.get(i);
            String flightNo = flight.getFlightNo();
            String carrierCode = flight.getCarrierCode();
            String statusCode =flight.getStatus();

            // Flight details to view
            flightCarrierNumber = carrierCode+flightNo;
            status = Status.get(statusCode);
            deptGate = flight.getDepartureGate();
            deptTerminal = flight.getDepartureTerminal();

            flightNumbers[i]= flightCarrierNumber;
            flightStatuses[i] = status;
            deptTerminals[i] = deptTerminal;
            deptGates[i] = deptGate;
            Log.i("ViewFlightsActivity","Flight : "+carrierCode+flightNo+"\n Status: "+statusCode+"\n Departure gate: "+deptGate+"\n Departure terminal: "+deptTerminal);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, flightNumbers);
        listview.setAdapter(adapter);

        // Item Click Listener for the listview
        OnItemClickListener itemClickListener = new  OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewFlightsActivity.this);
                alertDialogBuilder.setTitle("Latest Flight statuses");

                if(flightArrayList.isEmpty()) {
                    final TextView noFlightsView= new TextView(ViewFlightsActivity.this);
                    noFlightsView.setText("No flight data available currently");
                    alertDialogBuilder.setView(noFlightsView);
                } else {
                    final LinearLayout input = new LinearLayout (ViewFlightsActivity.this);
                    input.setOrientation(LinearLayout.VERTICAL);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    params.weight = 1.0f;
                    params.setMargins(14,4,0,0);

                    final TextView carrierNumber_txt= new TextView(ViewFlightsActivity.this);
                    carrierNumber_txt.setLayoutParams(params);

                    final TextView status_txt = new TextView(ViewFlightsActivity.this);
                    status_txt.setLayoutParams(params);

                    final TextView deptGate_txt = new TextView(ViewFlightsActivity.this);
                    deptGate_txt.setLayoutParams(params);

                    final TextView deptTerminal_txt = new TextView(ViewFlightsActivity.this);
                    deptTerminal_txt.setLayoutParams(params);

                    carrierNumber_txt.setText("Flight Number:"+flightNumbers[position]);
                    carrierNumber_txt.setTextSize(20.0f);

                    status_txt.setText("Status:"+flightStatuses[position]);
                    status_txt.setTextSize(20.0f);

                    deptGate_txt.setText("Departure Gate:"+deptGates[position]);
                    deptGate_txt.setTextSize(20.0f);

                    deptTerminal_txt.setText("Departure Terminal:"+deptTerminals[position]);
                    deptTerminal_txt.setTextSize(20.0f);

                    input.addView(carrierNumber_txt);
                    input.addView(status_txt);

                    if(deptTerminals[position]!=null){
                        input.addView(deptTerminal_txt);
                    }
                    if(deptGates[position]!=null) {
                        input.addView(deptGate_txt);
                    }

                    alertDialogBuilder.setView(input);
                }

                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                                // show it
                                alertDialog.show();
                            }
            };

        listview.setOnItemClickListener(itemClickListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_flights, menu);
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
