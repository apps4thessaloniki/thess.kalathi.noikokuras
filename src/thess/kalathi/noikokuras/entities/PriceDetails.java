package thess.kalathi.noikokuras.entities;

import java.io.Serializable;


public class PriceDetails implements Serializable {
    private Shop shop;
    private String minPrice;
    private String entryDate;

    public PriceDetails() {
        //
    }

    public PriceDetails(Shop shop, String minPrice, String entryDate) {
        this.shop = shop;
        this.minPrice = minPrice;
        this.entryDate = entryDate;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }
}
