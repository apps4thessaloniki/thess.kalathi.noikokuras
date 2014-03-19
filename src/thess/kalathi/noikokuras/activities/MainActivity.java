package thess.kalathi.noikokuras.activities;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import thess.kalathi.noikokuras.R;
import thess.kalathi.noikokuras.asynctasks.ReadBasketFromMemory;
import thess.kalathi.noikokuras.entities.Area;
import thess.kalathi.noikokuras.entities.Basket;
import thess.kalathi.noikokuras.utility.CheckResources;
import thess.kalathi.noikokuras.utility.RetrieveXMLData;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {
    private Basket basket = null;
    private Map<String, String> categoriesMap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		basket = ((MyApplication)getApplicationContext()).getBasket();
		if (basket == null) {
		    try {
		        ReadBasketFromMemory readBasketTask = new ReadBasketFromMemory(MainActivity.this);
		        readBasketTask.execute();
		        basket = readBasketTask.get();
		        // Add area: Dimo Thessalonikis
		        if (basket == null) {
		            Area area = new Area("428", "Δήμος Θεσσαλονίκης");
		            basket = new Basket(area);
		        }
		        ((MyApplication)getApplicationContext()).setBasket(basket);
		    } catch (Exception ex) {
		        Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
		    }
		}

		Button button1 = (Button)findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		        if (CheckResources.checkResources(MainActivity.this)) {
                    final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("Φορτώνονται όλες οι κατηγορίες προϊόντων, παρακαλώ περιμένετε");
                    progressDialog.show();

                    categoriesMap = ((MyApplication)getApplicationContext()).getAllCategories();
                    if (categoriesMap != null && categoriesMap.size() > 0) {
                        progressDialog.cancel();
                        Intent intent = new Intent(getApplicationContext(), AddCategoriesActivity.class);
                        startActivity(intent);
                    } else {
                        new Thread() {
                            public void run() {
                                try {
                                    categoriesMap = RetrieveXMLData.retrieveAllCategories();
                                    if (categoriesMap != null && categoriesMap.size() > 0) {
                                        // sort the values of map alphabetically
                                        Map<String, String> sortedCategoriesMap = sortCategoriesByComparator(categoriesMap);
                                        // cache the categories
                                        ((MyApplication)getApplicationContext()).setAllCategories(sortedCategoriesMap);
                                        Intent intent = new Intent(getApplicationContext(), AddCategoriesActivity.class);
                                        startActivity(intent);
                                    } else {
                                        progressDialog.cancel();
                                        /*
                                         * inserted the following due to error: can't create handler inside thread that has not called looper.prepare()
                                         */
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(MainActivity.this, "Κάποιο τεχνικό πρόβλημα προέκυψε, παρακαλώ προσπαθήστε ξανά αργότερα", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                } catch (Exception ex) {
                                    //Toast.makeText(AddAreaActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }.start();
                    }
                }
		    }
		});

		Button button2 = (Button)findViewById(R.id.button2);
		button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (basket == null) {
               		Toast.makeText(MainActivity.this, "Δεν έχετε προϊόντα στο καλάθι σας", Toast.LENGTH_SHORT).show();
                } else {
                	if (basket.getProductsList() == null) {
                		Toast.makeText(MainActivity.this, "Δεν έχετε προϊόντα στο καλάθι σας", Toast.LENGTH_SHORT).show();
                	} else {
                		if (basket.getProductsList().size() == 0) {
                			Toast.makeText(MainActivity.this, "Δεν έχετε προϊόντα στο καλάθι σας", Toast.LENGTH_SHORT).show();
                		} else {
                			Intent intent = new Intent(getApplicationContext(), ViewProductsActivity.class);
                			intent.putExtra("readonly", true);
                			startActivity(intent);
                		}
                	}
                }
            }
        });

		Button exitBtn = (Button)findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
	}

	private Map sortCategoriesByComparator(Map<String, String> unsortedMap) {
        List list = new LinkedList(unsortedMap.entrySet());

        // sort list based on comparator
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable)((Map.Entry)o1).getValue())
                            .compareTo(((Map.Entry)o2).getValue());
            }
        });

        // LinkedHashMap make sure order in which keys were inserted
        Map<String, String> sortedMap = new LinkedHashMap<String, String>();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put((String)entry.getKey(), (String)entry.getValue());
        }
        return sortedMap;
    }
}
