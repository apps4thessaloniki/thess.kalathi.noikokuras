package thess.kalathi.noikokuras.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Basket implements Serializable {
    private Area area;
    private List<Product> productsList;

    public Basket(Area area) {
        this.area = area;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public List<Product> getProductsList() {
        return productsList;
    }

    public void setProductsList(List<Product> productsList) {
        this.productsList = productsList;
    }

    public boolean productExists(Product product) {
        boolean productExists = false;

        if (productsList != null && productsList.size()>0) {
            for (Product p: productsList) {
                if (p.getProductId().equals(product.getProductId())) {
                    productExists = true;
                    break;
                }
            }
        }
        return productExists;
    }

    public void addProductToList(Product product) {
        if (productsList == null) {
            productsList = new ArrayList<Product>();
        }
        productsList.add(product);
    }

    public void updatePriceDetailsListOfProduct(String productId, List<PriceDetails> priceDetailsList) {
        for (Product product: productsList) {
            if (product.getProductId().equals(productId)) {
                product.setPriceDetailsList(priceDetailsList);
            }
        }
    }

    public void deletePriceDetailsOfAllProducts() {
    	for (Product product: productsList) {
    	    if (product.getPriceDetailsList() != null)
    	        product.setPriceDetailsList(null);
    	}
    }
}
