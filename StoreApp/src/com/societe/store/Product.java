package com.societe.store;

public class Product {
	
	private int id;
	private Brand brand;
	private Category category;
	public Brand getBrand() {
		return brand;
	}
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	private double price; 
	private int finalDiscount;
	private double discountPrice;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getFinalDiscount() {
		return finalDiscount;
	}
	public void setFinalDiscount(int finalDiscount) {
		this.finalDiscount = finalDiscount;
	}
	public double getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(double discountPrice) {
		this.discountPrice = discountPrice;
	}
	
}
