package thess.kalathi.noikokuras.utility;

import java.net.URL;
import java.util.List;
import java.util.Map;

import thess.kalathi.noikokuras.entities.PriceDetails;
import thess.kalathi.noikokuras.entities.Shop;

public class RetrieveXMLData {

    public static Map<String, String> retrieveAllCategories() throws Exception {
        Map<String, String> categoriesMap = null;

        try {
            URL url = new URL("http://services.e-prices.gr/getCategoriesWS?ui=kcs0pyoea752kvboabia");
            String dataAsXML = FileUtilities.downloadXMLData(url, true);
            categoriesMap = FileUtilities.readCategoriesStringByLine(dataAsXML);
        } catch (Exception ex) {
            throw new Exception(ex);
        }

        return categoriesMap;
    }

    public static Map<String, String> retrieveProductsOfCategory(String categoryId) throws Exception {
        Map<String, String> productsMap = null;

        try {
            URL url = new URL("http://services.e-prices.gr/getProductsOfACategoryWS?ui=xrdmetsdkkx3uhw20jaw&categoryid=" + categoryId);
            String dataAsXML = FileUtilities.downloadXMLData(url, true);
            productsMap = FileUtilities.readProductsStringByLine(dataAsXML);
        } catch (Exception ex) {
            throw new Exception(ex);
        }

        return productsMap;
    }

    public static List<PriceDetails> retrieveMinimumPricesForProductAtArea(String productId, String areaId) throws Exception {
        List<PriceDetails> priceDetailsList = null;

        try {
            String site = "http://services.e-prices.gr/GetMinimumPriceOfAProductatPlaceWS?ui=kcs0pyoea752kvboabia&areaid=" + areaId + "&productid=" + productId;
            URL url = new URL(site);
            String dataAsXML = FileUtilities.downloadXMLData(url, true);
            priceDetailsList = FileUtilities.readMinimumPricesStringByLine(dataAsXML);
        } catch (Exception ex) {
            throw new Exception(ex);
        }

        return priceDetailsList;
    }

    public static List<Shop> retrieveShopsOfArea(String areaId) throws Exception {
        List<Shop> shopsList = null;

        try {
            String site = "http://services.e-prices.gr/getShopsOfAnAreaWS?ui=kcs0pyoea752kvboabia&areaid=" + areaId;
            URL url = new URL(site);
            String dataAsXML = FileUtilities.downloadXMLData(url, true);
            shopsList = FileUtilities.readShopsOfAreaStringByLine(dataAsXML);
        } catch (Exception ex) {
            throw new Exception(ex);
        }

        return shopsList;
    }

}
