package thess.kalathi.noikokuras.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import thess.kalathi.noikokuras.R;
import thess.kalathi.noikokuras.entities.Product;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AddProductsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        // continue only if there is data
        Map<String, String> productsMap = ((MyApplication)getApplicationContext()).getProductsOfCategory();
        if (productsMap != null && productsMap.size() > 0) {
            // convert map of only values to list
            final List<String> productsValues = new ArrayList<String>(productsMap.values());
            // convert map of only keys to list
            final List<String> productsKeys = new ArrayList<String>(productsMap.keySet());

            ListView listView = (ListView) findViewById(R.id.listView1);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.listview_items, R.id.text, productsValues);
            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String productName = productsValues.get(position);
                    String productId = productsKeys.get(position);
                    Product product = new Product(productId, productName);

                    if (((MyApplication)getApplicationContext()).getBasket().productExists(product)) {
                        Toast.makeText(AddProductsActivity.this, "Το προϊόν " + product.getProductName() + " είναι ήδη στο καλάθι σας", Toast.LENGTH_SHORT).show();
                    } else {
                       ((MyApplication)getApplicationContext()).getBasket().addProductToList(product);
                       Intent intent = new Intent(getApplicationContext(), ViewProductsActivity.class);
                       startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), AddCategoriesActivity.class);
        startActivity(intent);
        finish();
    }
}
