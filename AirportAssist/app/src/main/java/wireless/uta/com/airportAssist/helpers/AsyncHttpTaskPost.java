package wireless.uta.com.airportAssist.helpers;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;

import wireless.uta.com.airportAssist.activities.MainActivity;

/**
 * Class : AsyncHttpTaskPost
 * A type of AsyncHttpTask to handle Http post request and response.
 *
 * Author : Shreyas
 */
public class AsyncHttpTaskPost extends AsyncHttpTask {

    // Properties
    private String TAG = this.getClass().getSimpleName();
    private ArrayList<NameValuePair> postParameters = null;
    private MainActivity flightActivity;
    private HttpPost httpPost = null;

    // Operations
    @Override
    protected InputStream doInBackground(URI... uris) {
        InputStream is = null;
        try{
            URI uri = uris[0];
            this.httpPost = new HttpPost(uri);
            this.httpPost.setEntity(new UrlEncodedFormEntity(this.postParameters));
            Log.i(TAG, this.httpPost.getURI().toString());

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(this.httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        }
        catch(Exception e){
            Log.e(TAG, "Error in http connection " + e.toString());
        }
        this.inputStream = is;
        return is;
    }

    @Override
    protected void onPostExecute(InputStream is) {
        String result = responseToString(is);
        this.flightActivity.getRestaurantsDataHandler(result);
    }

    //Getters and Setters
    public void setFlightActivity(MainActivity flightActivity) {
        this.flightActivity = flightActivity;
    }

    public void setPostParameters(ArrayList<NameValuePair> postParameters) {
        this.postParameters = postParameters;
    }
}
