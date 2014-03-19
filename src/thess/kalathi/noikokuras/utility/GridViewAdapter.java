package thess.kalathi.noikokuras.utility;

import thess.kalathi.noikokuras.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {
    private final Context context;
    private final String[] values;

    public GridViewAdapter(Context context, String[] values) {
        this.context = context;
        this.values = values;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View relativeLayoutView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            // get layout from menu_items.xml
            relativeLayoutView = inflater.inflate(R.layout.menu_items, null);

            // set value into textview
            TextView textView = (TextView) relativeLayoutView.findViewById(R.id.label);
            textView.setText(values[position]);
            //textView.setPadding(0, 0, 0, 2);

            // set image based on selected text
//            ImageView imageView = (ImageView) relativeLayoutView.findViewById(R.id.logo);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setImageResource(R.drawable.tick);
        } else {
            relativeLayoutView = (View) convertView;
        }

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