package thess.kalathi.noikokuras.asynctasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import thess.kalathi.noikokuras.entities.Basket;
import android.content.Context;
import android.os.AsyncTask;

public class WriteBasketToMemory extends AsyncTask<Basket, Void, Void> {
    private Context context;


    public WriteBasketToMemory(Context context) {
        this.context = context;
    }

    protected Void doInBackground(Basket... baskets) {
        File file = context.getFileStreamPath("mybasket");
        if (!file.exists()) {
            /*
             *  Create new file.
             *  When doing this, we need to pass the file path to the file directory.
             *  Otherwise we are trying to write the file to root, which is READ-ONLY!
             */
            String filePath = context.getFilesDir().getPath().toString() + "/mybasket";
            try {
                file = new File(filePath);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        // finally write to memory
        try {
            writeToMemory(baskets[0]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private void writeToMemory(Basket basket) throws Exception {
        FileOutputStream outputStream = null;
        ObjectOutputStream objectStream = null;

        try {
            outputStream = context.openFileOutput("mybasket", Context.MODE_PRIVATE);
            objectStream = new ObjectOutputStream(outputStream);
            objectStream.writeObject(basket);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (objectStream != null) objectStream.close();
                if (outputStream != null) outputStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
