package thess.kalathi.noikokuras.entities;

import java.io.Serializable;

public class Shop implements Serializable {
    private String shopId;
    private String shopName;
    private String shopAddress;

    public Shop() {
        //
    }

    public Shop(String shopId, String shopName, String shopAddress) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }


}
