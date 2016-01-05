package wireless.uta.com.airportAssist.helpers;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import wireless.uta.com.airportAssist.activities.MainActivity;

/**
 * Class: AsyncHttpTask
 * Abstract class that extends AsyncTask to handle Http connections.
 *
 * Author: Shreyas
 */
public abstract class AsyncHttpTask extends AsyncTask<URI,Void,InputStream> {

    //Properties
    private String TAG = this.getClass().getSimpleName();

    protected InputStream inputStream = null;
    protected MainActivity flightActivity;

    //Operations
    /**
     * Converts the passed InputStream parameter to String and returns it.
     * @param inputStream
     * @return responseString
     */
    protected String responseToString(InputStream inputStream) {
        //convert response to string
        try{
            BufferedReader bufferedReader = new BufferedReader(
                                            new InputStreamReader(inputStream,"iso-8859-1"),8);
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            String responseString = "";

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            inputStream.close();

            responseString = stringBuilder.toString();
            return responseString;

        } catch(Exception e) {
            Log.e(TAG, "Error converting result " + e.toString());
            return "Error converting result "+e.toString();
        }
    }
}