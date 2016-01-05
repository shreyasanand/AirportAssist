package wireless.uta.com.airportAssist.helpers;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URI;

import wireless.uta.com.airportAssist.activities.MainActivity;

/**
 * Class : AsyncHttpTaskGet
 * A type of AsyncHttpTask to handle Http get request and response.
 *
 * Author : Shreyas
 */
public class AsyncHttpTaskGet extends AsyncHttpTask {

    // Properties
    private String TAG = this.getClass().getSimpleName();
    private JSONObject jsonObject = null;
    private HttpGet httpGet = null;
    private MainActivity flightActivity;

    // Operations
    @Override
    protected InputStream doInBackground(URI... uris) {
        InputStream is = null;
        try{
            URI uri = uris[0];
            this.httpGet = new HttpGet(uri);
            HttpClient httpclient = new DefaultHttpClient();

            Log.i(this.TAG, this.httpGet.getURI().toString());
            HttpResponse response = httpclient.execute(this.httpGet);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        }
        catch(Exception e){
            Log.e(this.TAG, "Error in http connection " + e.toString());
        }
        inputStream = is;
        return is;
    }

    @Override
    protected void onPostExecute(InputStream is) {
        String result = responseToString(is);
        try{
            this.jsonObject = new JSONObject(result);
        }catch (Exception e){
            Log.i(TAG,"Error converting string to JSON inside doInBackground");
        }
        this.flightActivity.getFlightsDataHandler(this.jsonObject);
    }

    // Getters and Setters
    public void setFlightActivity(MainActivity flightActivity) {
        this.flightActivity = flightActivity;
    }

}