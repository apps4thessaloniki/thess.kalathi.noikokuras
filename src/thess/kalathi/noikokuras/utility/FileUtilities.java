package thess.kalathi.noikokuras.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import thess.kalathi.noikokuras.entities.PriceDetails;
import thess.kalathi.noikokuras.entities.Shop;
import android.content.res.Resources;


public class FileUtilities {

	/**
	 * Map<Key, Value>
	 */
    public static Map<String, String> readTextFileByLine(Resources resources, int resourceId) throws Exception {
    	Map<String, String> map = new HashMap<String, String>();
    	BufferedReader bufferReader = null;

    	try {
    		InputStream inputStream = resources.openRawResource(resourceId);
    		bufferReader = new BufferedReader(new InputStreamReader(inputStream));
    		String line = "";
    		String[] words = null;
            while ((line = bufferReader.readLine()) != null) {
            	words = line.split("-");
            	map.put(words[1], words[0]);
            }
        } catch (IOException e) {
            throw new Exception(e);
        } finally {
        	try {
        		bufferReader.close();
        	} catch (IOException ex) {
        		throw new Exception(ex);
        	}
        }
        return map;
    }

    public static Map<String, String> readCategoriesStringByLine(String data) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String key = "";
        String value = "";

        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new InputSource(new StringReader(data)));
            // normalize text representation
            doc.getDocumentElement().normalize();

           // category list
            NodeList categoryList = doc.getDocumentElement().getChildNodes();
            for (int i=0; i<categoryList.getLength(); i++) {
                Node categoryNode = categoryList.item(i);
                if (categoryNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element categoryElement = (Element)categoryNode;

                    NodeList detailsList = categoryElement.getChildNodes();
                    for (int j=0; j<detailsList.getLength(); j++) {
                        Node detailNode = detailsList.item(j);
                        if (detailNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element detailElement = (Element)detailNode;
                            if (detailElement != null) {
                                if (detailElement.getFirstChild() != null) {
                                    if (j == 0) {
                                        key = detailElement.getFirstChild().getNodeValue(); // category id
                                    } else if (j == 1) {
                                        value = detailElement.getFirstChild().getNodeValue();   // category name
                                    }
                                }
                            }
                        }
                    }
                    map.put(key, value);
                }
            }
        } catch (Exception ex) {
            throw new Exception(ex);
        }

        return map;
    }

    public static Map<String, String> readProductsStringByLine(String data) throws Exception {
    	Map<String, String> map = new HashMap<String, String>();
    	String key = "";
    	String value = "";

    	try {
          DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
          DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
          Document doc = docBuilder.parse(new InputSource(new StringReader(data)));
          // normalize text representation
          doc.getDocumentElement().normalize();

         // category list
          NodeList categoryList = doc.getDocumentElement().getChildNodes();
          for (int i=0; i<categoryList.getLength(); i++) {
              Node categoryNode = categoryList.item(i);
              if (categoryNode.getNodeType() == Node.ELEMENT_NODE) {
                  Element categoryElement = (Element)categoryNode;

                  NodeList detailsList = categoryElement.getChildNodes();
                  for (int j=0; j<detailsList.getLength(); j++) {
                      Node detailNode = detailsList.item(j);
                      if (detailNode.getNodeType() == Node.ELEMENT_NODE) {
                          Element detailElement = (Element)detailNode;
                          if (detailElement != null) {
                              if (detailElement.getFirstChild() != null) {
                                  if (j == 0) {
                                      key = detailElement.getFirstChild().getNodeValue();	// product id
                                  } else if (j == 1) {
                                      value = detailElement.getFirstChild().getNodeValue();	// product name
                                  }
                              }
                          }
                      }
                  }
                  map.put(key, value);
              }
          }
    	} catch (Exception ex) {
    		throw new Exception(ex);
    	}

    	return map;
    }

    public static List<PriceDetails> readMinimumPricesStringByLine(String data) throws Exception {
        List<PriceDetails> priceDetailsList = new ArrayList<PriceDetails>();

        try {
          DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
          DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
          Document doc = docBuilder.parse(new InputSource(new StringReader(data)));
          // normalize text representation
          doc.getDocumentElement().normalize();

         // category list
          NodeList categoryList = doc.getDocumentElement().getChildNodes();
          for (int i=0; i<categoryList.getLength(); i++) {
              Node categoryNode = categoryList.item(i);
              if (categoryNode.getNodeType() == Node.ELEMENT_NODE) {
                  Element categoryElement = (Element)categoryNode;

                  PriceDetails priceDetails = new PriceDetails();
                  NodeList detailsList = categoryElement.getChildNodes();
                  for (int j=0; j<detailsList.getLength(); j++) {
                      Node detailNode = detailsList.item(j);
                      if (detailNode.getNodeType() == Node.ELEMENT_NODE) {
                          Element detailElement = (Element)detailNode;
                          if (detailElement != null) {
                              if (detailElement.getFirstChild() != null) {
                                  switch (j) {
                                      case 0: Shop shop = new Shop(detailElement.getFirstChild().getNodeValue(), null, null);   // shop
                                              priceDetails.setShop(shop);
                                              break;
                                      case 2: priceDetails.setMinPrice(detailElement.getFirstChild().getNodeValue()); break;    // minPrice
                                      case 3: priceDetails.setEntryDate(detailElement.getFirstChild().getNodeValue()); break;   // entryDate
                                  }
                              }
                          }
                      }
                  }
                  priceDetailsList.add(priceDetails);
              }
          }
        } catch (Exception ex) {
    		throw new Exception(ex);
    	}

        return priceDetailsList;
    }

    public static List<Shop> readShopsOfAreaStringByLine(String data) throws Exception {
        List<Shop> shopsList = new ArrayList<Shop>();

        try {
          DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
          DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
          Document doc = docBuilder.parse(new InputSource(new StringReader(data)));
          // normalize text representation
          doc.getDocumentElement().normalize();

         // category list
          NodeList categoryList = doc.getDocumentElement().getChildNodes();
          for (int i=0; i<categoryList.getLength(); i++) {
              Node categoryNode = categoryList.item(i);
              if (categoryNode.getNodeType() == Node.ELEMENT_NODE) {
                  Element categoryElement = (Element)categoryNode;

                  Shop shop = new Shop();
                  NodeList detailsList = categoryElement.getChildNodes();
                  for (int j=0; j<detailsList.getLength(); j++) {
                      Node detailNode = detailsList.item(j);
                      if (detailNode.getNodeType() == Node.ELEMENT_NODE) {
                          Element detailElement = (Element)detailNode;
                          if (detailElement != null) {
                              if (detailElement.getFirstChild() != null) {
                                  switch (j) {
                                      case 0: shop.setShopId(detailElement.getFirstChild().getNodeValue()); break;
                                      case 1: shop.setShopAddress(detailElement.getFirstChild().getNodeValue()); break;    // minPrice
                                      case 2: shop.setShopName(detailElement.getFirstChild().getNodeValue()); break;   // entryDate
                                  }
                              }
                          }
                      }
                  }
                  shopsList.add(shop);
              }
          }
        } catch (Exception ex) {
    		throw new Exception(ex);
    	}

        return shopsList;
    }

    public static String downloadXMLData(URL url, boolean useConvertTextToGreek) throws Exception {
    	String data = "";
    	BufferedReader reader = null;
    	InputStream inputStream = null;
    	HttpURLConnection conn = null;

		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-type", "text/xml");

			inputStream = conn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				if (useConvertTextToGreek) data += convertTextToGreek(line);
				else data += line;
			}
		} catch (MalformedURLException ex) {
		    throw new Exception(ex);
		} catch (IOException ex) {
		    throw new Exception(ex);
        } finally {
			try {
				if (inputStream != null) inputStream.close();
				if (reader != null) reader.close();
				if (conn != null) conn.disconnect();
			} catch (IOException ex) {
				throw new Exception(ex);
			}
		}

		return data;
    }

    public static String convertTextToGreek(String line) {
		// taken from http://www.codetable.net/Group/greek-and-coptic
        // capital letters
        if (line.contains("&#x391;")) line = line.replace("&#x391;", "�");
        if (line.contains("&#x392;")) line = line.replace("&#x392;", "�");
        if (line.contains("&#x393;")) line = line.replace("&#x393;", "�");
        if (line.contains("&#x394;")) line = line.replace("&#x394;", "�");
        if (line.contains("&#x395;")) line = line.replace("&#x395;", "�");
        if (line.contains("&#x396;")) line = line.replace("&#x396;", "�");
        if (line.contains("&#x397;")) line = line.replace("&#x397;", "�");
        if (line.contains("&#x398;")) line = line.replace("&#x398;", "�");
        if (line.contains("&#x399;")) line = line.replace("&#x399;", "�");
        if (line.contains("&#x39A;")) line = line.replace("&#x39A;", "�");
        if (line.contains("&#x39B;")) line = line.replace("&#x39B;", "�");
        if (line.contains("&#x39C;")) line = line.replace("&#x39C;", "�");
        if (line.contains("&#x39D;")) line = line.replace("&#x39D;", "�");
        if (line.contains("&#x39E;")) line = line.replace("&#x39E;", "�");
        if (line.contains("&#x39F;")) line = line.replace("&#x39F;", "�");
        if (line.contains("&#x3A0;")) line = line.replace("&#x3A0;", "�");
        if (line.contains("&#x3A1;")) line = line.replace("&#x3A1;", "�");
        if (line.contains("&#x3A3;")) line = line.replace("&#x3A3;", "�");
        if (line.contains("&#x3A4;")) line = line.replace("&#x3A4;", "�");
        if (line.contains("&#x3A5;")) line = line.replace("&#x3A5;", "�");
        if (line.contains("&#x3A6;")) line = line.replace("&#x3A6;", "�");
        if (line.contains("&#x3A7;")) line = line.replace("&#x3A7;", "�");
        if (line.contains("&#x3A8;")) line = line.replace("&#x3A8;", "�");
        if (line.contains("&#x3A9;")) line = line.replace("&#x3A9;", "�");
        // capital letters with tonos
        if (line.contains("&#x386;")) line = line.replace("&#x386;", "�");
        if (line.contains("&#x388;")) line = line.replace("&#x388;", "�");
        if (line.contains("&#x389;")) line = line.replace("&#x389;", "�");
        if (line.contains("&#x38A;")) line = line.replace("&#x38A;", "�");
        if (line.contains("&#x38C;")) line = line.replace("&#x38C;", "�");
        if (line.contains("&#x38E;")) line = line.replace("&#x38E;", "�");
        if (line.contains("&#x38F;")) line = line.replace("&#x38F;", "�");
        if (line.contains("&#x3AA;")) line = line.replace("&#x3AA;", "�");
        // small letters
        if (line.contains("&#x3B1;")) line = line.replace("&#x3B1;", "�");
        if (line.contains("&#x3B2;")) line = line.replace("&#x3B2;", "�");
        if (line.contains("&#x3B3;")) line = line.replace("&#x3B3;", "�");
        if (line.contains("&#x3B4;")) line = line.replace("&#x3B4;", "�");
        if (line.contains("&#x3B5;")) line = line.replace("&#x3B5;", "�");
        if (line.contains("&#x3B6;")) line = line.replace("&#x3B6;", "�");
        if (line.contains("&#x3B7;")) line = line.replace("&#x3B7;", "�");
        if (line.contains("&#x3B8;")) line = line.replace("&#x3B8;", "�");
        if (line.contains("&#x3B9;")) line = line.replace("&#x3B9;", "�");
        if (line.contains("&#x3BA;")) line = line.replace("&#x3BA;", "�");
        if (line.contains("&#x3BB;")) line = line.replace("&#x3BB;", "�");
        if (line.contains("&#x3BC;")) line = line.replace("&#x3BC;", "�");
        if (line.contains("&#x3BD;")) line = line.replace("&#x3BD;", "�");
        if (line.contains("&#x3BE;")) line = line.replace("&#x3BE;", "�");
        if (line.contains("&#x3BF;")) line = line.replace("&#x3BF;", "�");
        if (line.contains("&#x3C0;")) line = line.replace("&#x3C0;", "�");
        if (line.contains("&#x3C1;")) line = line.replace("&#x3C1;", "�");
        if (line.contains("&#x3C2;")) line = line.replace("&#x3C2;", "�");
        if (line.contains("&#x3C3;")) line = line.replace("&#x3C3;", "�");
        if (line.contains("&#x3C4;")) line = line.replace("&#x3C4;", "�");
        if (line.contains("&#x3C5;")) line = line.replace("&#x3C5;", "�");
        if (line.contains("&#x3C6;")) line = line.replace("&#x3C6;", "�");
        if (line.contains("&#x3C7;")) line = line.replace("&#x3C7;", "�");
        if (line.contains("&#x3C8;")) line = line.replace("&#x3C8;", "�");
        if (line.contains("&#x3C9;")) line = line.replace("&#x3C9;", "�");
        // small letters with tonos and/or dialytika
        if (line.contains("&#x3AC;")) line = line.replace("&#x3AC;", "�");
        if (line.contains("&#x3AD;")) line = line.replace("&#x3AD;", "�");
        if (line.contains("&#x3AE;")) line = line.replace("&#x3AE;", "�");
        if (line.contains("&#x3CC;")) line = line.replace("&#x3CC;", "�");
        if (line.contains("&#x3CE;")) line = line.replace("&#x3CE;", "�");
        if (line.contains("&#x3AF;")) line = line.replace("&#x3AF;", "�");
        if (line.contains("&#x3CA;")) line = line.replace("&#x3CA;", "�");
        if (line.contains("&#x390;")) line = line.replace("&#x390;", "�");
        if (line.contains("&#x3CD;")) line = line.replace("&#x3CD;", "�");
        if (line.contains("&#x3CB;")) line = line.replace("&#x3CB;", "�");
        if (line.contains("&#x3B0;")) line = line.replace("&#x3B0;", "�");
        return line;
    }
}