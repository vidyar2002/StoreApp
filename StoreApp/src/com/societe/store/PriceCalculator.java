package com.societe.store;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
/**
 * 
 * @author Vidya
 *
 */
public class PriceCalculator {
	
	private static Map<String,Brand> BrandMap = null;
	private static Map<String,Product> inventoryMap = null;
	private static Map<String,Category> categoryMap = null;
	private static Properties bundle = null;
	private static String cvsSplitBy = ",";
	
	
	public static void main(String[] args) {
		
		try {
			
			//Load properties
			InputStream input = new FileInputStream("inventory.properties");

			bundle = new Properties();
			bundle.load(input);
						
			//populate category, brands and inventory data
			categoryMap = populateCategory(bundle.getProperty("categoryFileLocation"));
			BrandMap = populateBrand(bundle.getProperty("brandFileLocation"));
			inventoryMap = populateInventory(bundle.getProperty("inventoryFileLocation"));
						
		    System.out.print("Enter Items: ");
		    InputStreamReader streamReader = new InputStreamReader(System.in);
		    BufferedReader bufferedReader = new BufferedReader(streamReader);
		    String itemStr = bufferedReader.readLine();
		    
		    costCalculation(itemStr);
		    
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}	
	
	/**
	 * Set up the categoy set up
	 * @return Map of category info with category Name as the key Map<String, Category>
	 */
	private static Map<String,Category> populateCategory(String csvFile) {
		
		System.out.println("Going to load the Category details");
		Map<String,Category> mp = new HashMap<String,Category>();
		String line = "";
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {            	
            	Category c = new Category();            	
                String[] str = line.split(cvsSplitBy);
                if(str != null && str.length > 1)
                {
                c.setCategoryName(str[0]);
                c.setDiscount(Integer.parseInt(str[1]));
                c.setParentCategory(str.length>2?mp.get(str[2]):null);
                mp.put(str[0], c); 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		System.out.println("Loaded the Category details");
		return mp;
	}	
	
	/**
	 * To read the brand info from CSV file
	 * @param csvFile
	 * @return
	 */
	private static Map<String,Brand> populateBrand(String csvFile) {
		System.out.println("Going to load the Brand details");
		Map<String,Brand> mp = new HashMap<String,Brand>();
		Brand b =null;
        String line = "";        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
            	b = new Brand();
                // use comma as separator
                String[] str = line.split(cvsSplitBy);
                b.setBrandName(str[0]);
                try{
                	b.setDiscount(Integer.parseInt(str[1]));
                }
                catch(NumberFormatException ex)
                {
                	b.setDiscount(0);
                }
                mp.put(str[0], b);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Loaded the Brand details");
		return mp;
	}
	
	/**
	 * Populate inventory data from csv file
	 * @param csvFile
	 * @return Inventory data Map<String, Product>
	 */
	private static Map<String,Product> populateInventory(String csvFile) {
		System.out.println("Going to load the Inventory details");
		Map<String,Product> mp = new HashMap<String,Product>();
		Product p =null;
        String line = "";
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
            	p = new Product();
                // use comma as separator
                String[] str = line.split(cvsSplitBy);                
                p.setId(Integer.parseInt(str[0]));
                p.setBrand(BrandMap.get(str[1].trim()));
                p.setCategory(categoryMap.get(str[2].trim()));
                p.setPrice(Double.parseDouble(str[3]));
                p.setDiscountPrice(calculateFinalPrice(p));
                System.out.println("product id= " + p.getId() + " , brand=" + p.getBrand().getBrandName() + ", "+p.getPrice());
                mp.put(str[0], p); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }		
        System.out.println("Loaded the inventory details");
		return mp;
	}	

	/**
	 * Price calculation for each product
	 * @param Product p
	 * @return double price
	 */
	private static double calculateFinalPrice(Product p) {
		int brandDiscount = p.getBrand().getDiscount();
		
		int discount = 0;
		int finalDiscount = 0;
		Category parentCategory = null;
		
		Category productCategory = p.getCategory();
		int categoryDiscount = productCategory.getDiscount();		
		parentCategory = productCategory.getParentCategory();
		
		while(parentCategory != null) {
			discount = parentCategory.getDiscount();
			if( categoryDiscount > discount ) discount = categoryDiscount;
			parentCategory = parentCategory.getParentCategory();
		} 
		
		if(brandDiscount > discount) finalDiscount = brandDiscount;
			else finalDiscount = discount;
		double price = p.getPrice() - p.getPrice() *finalDiscount/100;
		
		return price;
	}

	/**
	 * Cost calculation for the selected items
	 * @param itemStr
	 */
	private static void costCalculation(String itemStr)
	{
		String[] items = itemStr.split(",");
	    double totalPrice = 0;
	    for(String item : items){
	    	System.out.println("Entered Items price: "+inventoryMap.get(item).getDiscountPrice());
	    	double price = inventoryMap.get(item).getDiscountPrice();
	    	totalPrice = totalPrice + price;
	    }
	    System.out.print("Total Items price: "+totalPrice);

	}
}
