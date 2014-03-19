package thess.kalathi.noikokuras.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {
    private String productId;
    private String productName;
    private List<PriceDetails> priceDetailsList;

    public Product(String productId, String productName) {
        this.productId = productId;
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<PriceDetails> getPriceDetailsList() {
        return priceDetailsList;
    }

    public void setPriceDetailsList(List<PriceDetails> priceDetailsList) {
        this.priceDetailsList = priceDetailsList;
    }

    public void addPriceDetailsToList(PriceDetails priceDetails) {
        if (priceDetailsList == null) {
            priceDetailsList = new ArrayList<PriceDetails>();
        }
        priceDetailsList.add(priceDetails);
    }
}