package thess.kalathi.noikokuras.activities;

import java.util.ArrayList;
import java.util.List;

import thess.kalathi.noikokuras.R;
import thess.kalathi.noikokuras.asynctasks.WriteBasketToMemory;
import thess.kalathi.noikokuras.entities.Basket;
import thess.kalathi.noikokuras.entities.PriceDetails;
import thess.kalathi.noikokuras.entities.Shop;
import thess.kalathi.noikokuras.utility.CheckResources;
import thess.kalathi.noikokuras.utility.GridViewAdapter;
import thess.kalathi.noikokuras.utility.RetrieveXMLData;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class ViewProductsActivity extends Activity {
    private List<PriceDetails> priceDetailsList;
    private boolean readonly;
    private boolean noData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_products);

		Basket basket = ((MyApplication)getApplicationContext()).getBasket();
		if (basket == null) {
            startActivity(new Intent(this, MainActivity.class));
            return;
		}

	    Intent intent = getIntent();
	    readonly = intent.getBooleanExtra("readonly", false);
	    GridView gridView = (GridView) findViewById(R.id.gridView1);

	    if (basket.getProductsList() != null) {
	    	String[] productsList = new String[basket.getProductsList().size()];
	    	for (int i=0; i<basket.getProductsList().size(); i++) {
	    		productsList[i] = basket.getProductsList().get(i).getProductName();
	    	}
	    	gridView.setAdapter(new GridViewAdapter(this, productsList));
	    }

	    gridView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	if (readonly) {
	        		Intent intent = new Intent(getApplicationContext(), GetMinimumPricesOfProductActivity.class);
                    intent.putExtra("index", position);
                    intent.putExtra("readonly", readonly);
                    startActivity(intent);
                    finish();
	        	} else {
		            if (CheckResources.checkResources(ViewProductsActivity.this)) {
		                final ProgressDialog progressDialog = new ProgressDialog(ViewProductsActivity.this);
	                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	                    progressDialog.setMessage("Φορτώνονται οι τιμές για το προϊόν που επιλέξατε, παρακαλώ περιμένετε");
	                    progressDialog.show();

	                    final int index = position;
	                    priceDetailsList = ((MyApplication)getApplicationContext()).getBasket().getProductsList().get(position).getPriceDetailsList();
	                    if (priceDetailsList != null && priceDetailsList.size() > 0) {
	                    	progressDialog.cancel();
	                        Intent intent = new Intent(getApplicationContext(), GetMinimumPricesOfProductActivity.class);
	                        intent.putExtra("readonly", readonly);
	                        startActivity(intent);
	                        finish();
	                    } else {
	                        new Thread() {
	                            public void run() {
	                                try {
	                                    String productId = ((MyApplication)getApplicationContext()).getBasket().getProductsList().get(index).getProductId();
	                                    String areaId = ((MyApplication)getApplicationContext()).getBasket().getArea().getAreaId();

	                                    priceDetailsList = RetrieveXMLData.retrieveMinimumPricesForProductAtArea(productId, areaId);
	                                    if (priceDetailsList != null && priceDetailsList.size() > 0) {
	                                    	// first check whether the global variable holding all shops of an area has a value
	                                    	List<Shop> shopsOfArea = ((MyApplication)getApplicationContext()).getShopsOfArea();
	                                    	if (shopsOfArea != null && shopsOfArea.size() > 0) {
	                                    		// move on
	                                    	} else {
	                                    		shopsOfArea = RetrieveXMLData.retrieveShopsOfArea(areaId);
	                                    		if (shopsOfArea != null && shopsOfArea.size() > 0) {
	                                    			// update the global variable
	                                    			((MyApplication)getApplicationContext()).setShopsOfArea(shopsOfArea);
	                                    		}
	                                    	}
	                                    	List<PriceDetails> tempPriceDetailsList = new ArrayList<PriceDetails>();
	                                    	for (PriceDetails priceDetails: priceDetailsList) {
	                                    		for (Shop shop: shopsOfArea) {
	                                    			if (priceDetails.getShop().getShopId().equals(shop.getShopId())) {
	                                    				priceDetails.getShop().setShopAddress(shop.getShopAddress());
	                                    				priceDetails.getShop().setShopName(shop.getShopName());
	                                    				tempPriceDetailsList.add(priceDetails);
	                                    				break;
	                                    			}
	                                    		}
	                                    	}
	                                    	// finally update the basket global variable with the complete prices details
	                                    	((MyApplication)getApplicationContext()).getBasket().updatePriceDetailsListOfProduct(productId, tempPriceDetailsList);
	                                    } else {
	                                        progressDialog.cancel();
	                                        noData = true;
//	                                    	if (!readonly) {
//	                                    		/*
//	                                    		 * inserted the following due to error: can't create handler inside thread that has not called looper.prepare()
//	                                    		 */
//	                                    		runOnUiThread(new Runnable() {
//	                                    			public void run() {
//	                                    				Toast.makeText(ViewProductsActivity.this, "Προς το παρόν δεν υπάρχουν τιμές για το παρόν προϊόν στον server, παρακαλώ προσπαθήστε ξανά αργότερα", Toast.LENGTH_LONG).show();
//	                                    			}
//	                                    		});
//	                                    	}
	                                    }
	                                    Intent intent = new Intent(getApplicationContext(), GetMinimumPricesOfProductActivity.class);
	                                    intent.putExtra("index", index);
	                                    intent.putExtra("readonly", readonly);
	                                    intent.putExtra("noData", noData);
	                                    startActivity(intent);
	                                    finish();
	                                } catch (Exception ex) {
	                                    //Toast.makeText(ViewProductsActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
	                                }
	                            }
	                        }.start();
	                   }
	               }
		        }
	        }
	    });

	    Button saveBtn = (Button) findViewById(R.id.saveAllBtn);
	    saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MyApplication)getApplicationContext()).getBasket().getProductsList() == null ||
                        ((MyApplication)getApplicationContext()).getBasket().getProductsList().isEmpty()) {
                    Toast.makeText(ViewProductsActivity.this, "Δεν υπάρχουν προϊόντα στο καλάθι σας!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        WriteBasketToMemory writeBasketToMemoryTask = new WriteBasketToMemory(ViewProductsActivity.this);
                        writeBasketToMemoryTask.execute(((MyApplication)getApplicationContext()).getBasket());
                        Toast.makeText(ViewProductsActivity.this, "Το καλάθι με τα προϊόντα σας αποθηκεύτηκε επιτυχώς", Toast.LENGTH_SHORT).show();
                    } catch (Exception ex) {
                        Toast.makeText(ViewProductsActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

	    Button homeBtn = (Button) findViewById(R.id.homeBtn);
	    homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    WriteBasketToMemory writeBasketToMemoryTask = new WriteBasketToMemory(ViewProductsActivity.this);
                    writeBasketToMemoryTask.execute(((MyApplication)getApplicationContext()).getBasket());
                    Toast.makeText(ViewProductsActivity.this, "Το καλάθι με τα προϊόντα σας αποθηκεύτηκε αυτόματα", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ViewProductsActivity.this, MainActivity.class);
                    startActivity(intent);
                } catch (Exception ex) {
                    Toast.makeText(ViewProductsActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
	}

    /*
     * When the Delete button is pressed
     */
    public void deleteAllProducts(View view) {
        if (((MyApplication)getApplicationContext()).getBasket().getProductsList() == null ||
                ((MyApplication)getApplicationContext()).getBasket().getProductsList().isEmpty()) {
            Toast.makeText(ViewProductsActivity.this, "Δεν υπάρχουν προϊόντα στο καλάθι σας!", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewProductsActivity.this);
            alertDialogBuilder
                .setMessage("Είστε σίγουροι ότι θέλετε να διαγράψετε όλα τα προϊόντα από το καλάθι σας;")
                .setCancelable(false)
                .setPositiveButton("Ναι",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        ((MyApplication)getApplicationContext()).getBasket().setProductsList(null);
                        WriteBasketToMemory writeBasketToMemoryTask = new WriteBasketToMemory(ViewProductsActivity.this);
                        writeBasketToMemoryTask.execute(((MyApplication)getApplicationContext()).getBasket());
                        dialog.cancel();
                        Intent intent = new Intent(getApplicationContext(), ViewProductsActivity.class);
                        intent.putExtra("readonly", readonly);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Όχι",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
            AlertDialog alertDialog = alertDialogBuilder.create();  // create alert dialog
            alertDialog.show();  // show it
        }
    }

//    @Override
//    public void onBackPressed() {
//
//    }
}
