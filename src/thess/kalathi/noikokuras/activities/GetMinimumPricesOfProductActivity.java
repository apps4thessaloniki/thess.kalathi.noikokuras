package thess.kalathi.noikokuras.activities;

import java.util.ArrayList;
import java.util.List;

import thess.kalathi.noikokuras.R;
import thess.kalathi.noikokuras.asynctasks.WriteBasketToMemory;
import thess.kalathi.noikokuras.entities.Basket;
import thess.kalathi.noikokuras.entities.PriceDetails;
import thess.kalathi.noikokuras.entities.Shop;
import thess.kalathi.noikokuras.utility.CheckResources;
import thess.kalathi.noikokuras.utility.GridView2Adapter;
import thess.kalathi.noikokuras.utility.RetrieveXMLData;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class GetMinimumPricesOfProductActivity extends Activity {
    private TextView areaName;
    private TextView productName;
    private int index;
    private boolean readonly;
    private boolean noData;
    List<Shop> shopsOfArea;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_minimum_prices_of_product);

        Basket basket = ((MyApplication)getApplicationContext()).getBasket();
        if (basket == null) {
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        Intent intent = getIntent();
        index = intent.getIntExtra("index", 0);
        readonly = intent.getBooleanExtra("readonly", false);
        noData = intent.getBooleanExtra("noData", false);

        areaName = (TextView) findViewById(R.id.areaName);
        productName = (TextView) findViewById(R.id.productName);
        GridView gridView = (GridView) findViewById(R.id.gridView1);

        areaName.setText(basket.getArea().getAreaName());
        productName.setText(basket.getProductsList().get(index).getProductName());

        if (noData) {
            String note = "Προς το παρόν το Υπουργείο Ανάπτυξης δεν παρέχει τιμές για το παρόν προϊόν για τη συγκεκριμένη περιοχή. Παρακαλώ προσπαθήστε ξανά στο μέλλον. Να γνωρίζετε ότι οι τιμές των προϊόντων ενημερώνονται κάθε 15 μέρες, στις 5 και 20 κάθε μήνα.";
            productName.setTextColor(Color.RED);
            productName.append("\n\n" + note);
        } else {
            List<PriceDetails> priceDetailsList = basket.getProductsList().get(index).getPriceDetailsList();
            if (priceDetailsList != null && priceDetailsList.size() > 0) {
                String[] pricesList = new String[priceDetailsList.size()];
                for (int i=0; i<priceDetailsList.size(); i++) {
                    pricesList[i] = priceDetailsList.get(i).getMinPrice() + ";" + priceDetailsList.get(i).getEntryDate() + ";" +
                    priceDetailsList.get(i).getShop().getShopAddress();
                }
                gridView.setAdapter(new GridView2Adapter(this, pricesList));

                productName.setTextColor(Color.BLACK);
                productName.setText(basket.getProductsList().get(index).getProductName());
            }
        }

        Button downloadBtn = (Button) findViewById(R.id.downloadBtn);
        if (readonly) {
        	Toast.makeText(GetMinimumPricesOfProductActivity.this, "Για να δείτε τις πιο πρόσφατες τιμές, παρακαλώ πατήστε πάνω στο κουμπί με το βελάκι", Toast.LENGTH_SHORT).show();

        	downloadBtn.setVisibility(View.VISIBLE);
        	downloadBtn.setOnClickListener(new View.OnClickListener() {
        		@Override
        		public void onClick(View v) {
        		    if (CheckResources.checkResources(GetMinimumPricesOfProductActivity.this)) {
        		    	progressDialog = new ProgressDialog(GetMinimumPricesOfProductActivity.this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setMessage("Γίνεται φόρτωση των τιμών για το προϊόν, παρακαλώ περιμένετε");
                        progressDialog.show();

                        new Thread() {
                            public void run() {
                                try {
                                	String productId = ((MyApplication)getApplicationContext()).getBasket().getProductsList().get(index).getProductId();
                                    String areaId = ((MyApplication)getApplicationContext()).getBasket().getArea().getAreaId();

                                	List<PriceDetails> priceDetailsList = RetrieveXMLData.retrieveMinimumPricesForProductAtArea(productId, areaId);
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
                                		/*
                                		 * inserted the following due to error: can't create handler inside thread that has not called looper.prepare()
                                		 */
//                                		runOnUiThread(new Runnable() {
//                                			public void run() {
//                                				Toast.makeText(GetMinimumPricesOfProductActivity.this, "Προς το παρόν δεν υπάρχουν τιμές για το παρόν προϊόν στον server, παρακαλώ προσπαθήστε ξανά αργότερα", Toast.LENGTH_LONG).show();
//                                			}
//                                		});
                                	}
                                	Intent intent = new Intent(getApplicationContext(), GetMinimumPricesOfProductActivity.class);
                                    intent.putExtra("index", index);
                                    intent.putExtra("readonly", readonly);
                                    intent.putExtra("noData", noData);
                                    startActivity(intent);
                                    finish();
                                } catch (Exception ex) {
                                    //Toast.makeText(GetMinimumPricesOfProductActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }.start();
        		    }
        		}
        	});
        } else {
        	downloadBtn.setVisibility(View.INVISIBLE);
        }

        Button saveBtn = (Button) findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    WriteBasketToMemory writeBasketToMemoryTask = new WriteBasketToMemory(GetMinimumPricesOfProductActivity.this);
                    writeBasketToMemoryTask.execute(((MyApplication)getApplicationContext()).getBasket());
                    Toast.makeText(GetMinimumPricesOfProductActivity.this, "Το προϊόν αποθηκεύτηκε επιτυχώς", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    Toast.makeText(GetMinimumPricesOfProductActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        Button homeBtn = (Button) findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    WriteBasketToMemory writeBasketToMemoryTask = new WriteBasketToMemory(GetMinimumPricesOfProductActivity.this);
                    writeBasketToMemoryTask.execute(((MyApplication)getApplicationContext()).getBasket());
                    Toast.makeText(GetMinimumPricesOfProductActivity.this, "Το καλάθι με τα προϊόντα σας αποθηκεύτηκε αυτόματα", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } catch (Exception ex) {
                    Toast.makeText(GetMinimumPricesOfProductActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /*
     * When the Delete button is pressed
     */
    public void removeProductFromBasket(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GetMinimumPricesOfProductActivity.this);
        alertDialogBuilder
            .setMessage("Είστε σίγουροι ότι θέλετε να διαγράψετε το παρόν προϊόν από το καλάθι σας;")
            .setCancelable(false)
            .setPositiveButton("Ναι",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    ((MyApplication)getApplicationContext()).getBasket().getProductsList().remove(index);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ViewProductsActivity.class);
        intent.putExtra("readonly", readonly);
        startActivity(intent);
        finish();
    }
}
