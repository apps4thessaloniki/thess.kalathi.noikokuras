package thess.kalathi.noikokuras.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import thess.kalathi.noikokuras.R;
import thess.kalathi.noikokuras.utility.CheckResources;
import thess.kalathi.noikokuras.utility.RetrieveXMLData;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddCategoriesActivity extends Activity {
	private ArrayAdapter<String> arrayAdapter;
    private TextView filterText;
    private List<String> categoriesValues;
    private List<String> categoriesKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_categories);

        filterText = (TextView) findViewById(R.id.searchText);
        filterText.addTextChangedListener(filterTextWatcher);

        Map<String, String> categoriesMap = ((MyApplication)getApplicationContext()).getAllCategories();
        if (categoriesMap != null && categoriesMap.size() > 0) {
            // convert map of only values to list
            categoriesValues = new ArrayList<String>(categoriesMap.values());
            // convert map of only keys to list
            categoriesKeys = new ArrayList<String>(categoriesMap.keySet());

            ListView listView = (ListView) findViewById(R.id.listView1);
            arrayAdapter = new ArrayAdapter<String>(this, R.layout.listview_items, R.id.text, categoriesValues);
            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new OnItemClickListener() {
            	@Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (CheckResources.checkResources(AddCategoriesActivity.this)) {
                    	int index = categoriesValues.indexOf(parent.getItemAtPosition(position).toString());
                        final String categoryName = categoriesValues.get(index);
                        final String categoryId = categoriesKeys.get(index);

                        final ProgressDialog progressDialog = new ProgressDialog(AddCategoriesActivity.this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setMessage("Φορτώνονται όλα τα προϊόντα για την κατηγορία " + categoryName + ", παρακαλώ περιμένετε");
                        progressDialog.show();

                        new Thread() {
                            public void run() {
                                try {
                                    Map<String, String> productsMap = RetrieveXMLData.retrieveProductsOfCategory(categoryId);
                                    if (productsMap != null && productsMap.size() > 0) {
                                        ((MyApplication)getApplicationContext()).setProductsOfCategory(productsMap);
                                        Intent intent = new Intent(getApplicationContext(), AddProductsActivity.class);
                                        startActivity(intent);
                                    } else {
                                        progressDialog.cancel();
                                        /*
                                         * inserted the following due to error: can't create handler inside thread that has not called looper.prepare()
                                         */
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(AddCategoriesActivity.this, "Κάποιο τεχνικό πρόβλημα προέκυψε, παρακαλώ προσπαθήστε ξανά αργότερα", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                } catch (Exception ex) {
                                    //Toast.makeText(AddCategoriesActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }.start();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            AddCategoriesActivity.this.arrayAdapter.getFilter().filter(s);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        filterText.removeTextChangedListener(filterTextWatcher);
    }
}