package thess.kalathi.noikokuras.asynctasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import thess.kalathi.noikokuras.entities.Basket;
import android.content.Context;
import android.os.AsyncTask;

public class ReadBasketFromMemory extends AsyncTask<Void, Void, Basket> {
    private Context context;


    public ReadBasketFromMemory(Context context) {
        this.context = context;
    }

    protected Basket doInBackground(Void... voids) {
        Basket basket = null;

        File file = context.getFileStreamPath("mybasket");
        if (file.exists()) {
            FileInputStream inputStream = null;
            ObjectInputStream objectStream = null;
            try {
                inputStream = context.openFileInput("mybasket");
                objectStream = new ObjectInputStream(inputStream);
                basket = (Basket)objectStream.readObject();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (objectStream != null) objectStream.close();
                    if (inputStream != null) inputStream.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return basket;
    }
}