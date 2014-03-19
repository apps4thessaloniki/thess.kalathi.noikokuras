package thess.kalathi.noikokuras.asynctasks;

import java.net.URL;

import thess.kalathi.noikokuras.utility.FileUtilities;
import android.os.AsyncTask;

public class CheckWSAvailability extends AsyncTask<Void, Void, String> {
    private boolean useConvertTextToGreek;


    public CheckWSAvailability(boolean useConvertTextToGreek) {
        this.useConvertTextToGreek = useConvertTextToGreek;
    }

    protected String doInBackground(Void...voids) {
        String result = "";
        try {
            URL url = new URL("http://services.e-prices.gr/checkWSAvailabilityWS?ui=kcs0pyoea752kvboabia");
            result = FileUtilities.downloadXMLData(url, useConvertTextToGreek);
        } catch (Exception ex) {
        	//Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return result;
    }
}
