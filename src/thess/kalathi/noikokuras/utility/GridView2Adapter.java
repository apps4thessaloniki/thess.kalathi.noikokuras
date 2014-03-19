package thess.kalathi.noikokuras.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import thess.kalathi.noikokuras.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridView2Adapter extends BaseAdapter {
    private final Context context;
    private final String[] values;

    public GridView2Adapter(Context context, String[] values) {
        this.context = context;
        this.values = values;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View relativeLayoutView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            // get layout from menu_items.xml
            relativeLayoutView = inflater.inflate(R.layout.textview_items, null);

            // set value into textview
            TextView priceView = (TextView) relativeLayoutView.findViewById(R.id.price);
            TextView shopView = (TextView) relativeLayoutView.findViewById(R.id.shop);
            TextView dateView = (TextView) relativeLayoutView.findViewById(R.id.date);

            String[] priceDetails = values[position].split(";");
            priceView.setText("Τιμή: " + priceDetails[0] + " €");
            shopView.setText("Κατάστημα: " + priceDetails[2]);
            dateView.setText("Ημερομηνία δειγματοληψίας: " + convertDateToString(priceDetails[1]));
        } else {
            relativeLayoutView = (View) convertView;
        }

        return relativeLayoutView;
    }

    private String convertDateToString(String myDate) {
        String[] values = myDate.split("/");

        Calendar mydate = Calendar.getInstance();
        mydate.set(Calendar.DAY_OF_MONTH, Integer.valueOf(values[0]));
        mydate.set(Calendar.MONTH, Integer.valueOf(values[1])-1);
        mydate.set(Calendar.YEAR, Integer.valueOf(values[2]));

        DateFormat df = new SimpleDateFormat("d MMMM yyyy");
        return df.format(mydate.getTime());
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int position) {
        return values[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}