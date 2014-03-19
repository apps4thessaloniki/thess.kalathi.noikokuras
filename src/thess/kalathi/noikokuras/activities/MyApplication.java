package thess.kalathi.noikokuras.activities;

import java.util.List;
import java.util.Map;

import thess.kalathi.noikokuras.entities.Basket;
import thess.kalathi.noikokuras.entities.Shop;
import android.app.Application;

public class MyApplication extends Application {
    private Basket basket = null;
    private Map<String, String> allCategories = null;
    private Map<String, String> productsOfCategory = null;
    private List<Shop> shopsOfArea = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public Map<String, String> getAllCategories() {
        return allCategories;
    }

    public void setAllCategories(Map<String, String> allCategories) {
        this.allCategories = allCategories;
    }

    public Map<String, String> getProductsOfCategory() {
        return productsOfCategory;
    }

    public void setProductsOfCategory(Map<String, String> productsOfCategory) {
        this.productsOfCategory = productsOfCategory;
    }

    public List<Shop> getShopsOfArea() {
        return shopsOfArea;
    }

    public void setShopsOfArea(List<Shop> shopsOfArea) {
        this.shopsOfArea = shopsOfArea;
    }
}