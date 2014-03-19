package thess.kalathi.noikokuras.utility;

import thess.kalathi.noikokuras.asynctasks.CheckWSAvailability;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class CheckResources {

    /*
     * Check for internet connection and then if the web services are on
     */
    public static boolean checkResources(Context context) {
        boolean everyhtingIsFine = false;

        if (CheckResources.existsNetworkConnection(context) &&
                CheckResources.areWebServicesON(context)) {
            everyhtingIsFine = true;
        }

        return everyhtingIsFine;
    }

    public static boolean existsNetworkConnection(Context context) {
        boolean existsNetworkConnection = false;

        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                existsNetworkConnection = true;
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Δεν υπάρχει σύνδεση στο internet");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("ΟΚ",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();  // create alert dialog
                alertDialog.show(); // show it
            }
        } catch (Exception ex) {
            Toast.makeText(context, "Πρόβλημα με το ίντερνετ", Toast.LENGTH_LONG).show();
        }

        return existsNetworkConnection;
    }

    public static boolean areWebServicesON(Context context) {
        boolean areWebServicesON = false;

        try {
            CheckWSAvailability wsAvailability = new CheckWSAvailability(false);
            String result = wsAvailability.execute().get();
            if (result.contains("ON")) {
                areWebServicesON = true;
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Η υπηρεσία δεν είναι διαθέσιμη προς το παρόν. Παρακαλώ προσπαθήστε ξανά αργότερα");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("ΟΚ",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();  // create alert dialog
                alertDialog.show(); // show it
            }
        } catch (Exception ex) {
            Toast.makeText(context, "Πρόβλημα με την υπηρεσία", Toast.LENGTH_LONG).show();
        }

        return areWebServicesON;
    }
}
