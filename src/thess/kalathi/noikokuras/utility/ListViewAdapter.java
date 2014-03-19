package thess.kalathi.noikokuras.utility;

import thess.kalathi.noikokuras.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {
    private final Context context;
    private final String[] values;

    public ListViewAdapter(Context context, String[] values) {
        this.context = context;
        this.values = values;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View relativeLayoutView;
        TextView textView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            // get layout from listview_items.xml
            relativeLayoutView = inflater.inflate(R.layout.listview_items, null);
        } else {
            relativeLayoutView = (View) convertView;
        }
        // set value into textview after the if-then-else statement
        textView = (TextView) relativeLayoutView.findViewById(R.id.text);
        textView.setText(values[position]);

        return relativeLayoutView;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}