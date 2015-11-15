package com.Data;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import Exceptions.CouponsException;

/**
 * The class 'Coupon' Manages the Coupon's object
 * @author Chen Grinberg
 *	 */
@XmlRootElement
public class Coupon {	
	
	private long id;
	private String title;
	private Date startDate;
	private Date endDate;
	private int amount;
	private CouponType type;
	private String message;
	private double price;
	private String image;
	
	/**
	 * @category CouponType holds the available Coupon Categories.
	 * @author Grinberg
	 *
	 */	
	public enum CouponType {
		
		RESTAURANTS, ELECTRICITY, FOOD, HEALTH, SPORTS, CAMPING, TRAVELING, SPA  
	}
	
	/**Allow the coupon to be handled in HashSets.
	 *  generated with length of coupon's title (won't allow two coupon's with same title to be added).
	 */
	@Override
	public int hashCode() {
		return 100+this.title.length();
	}
	
	/**
	 * Construct the coupon object given no Data. 
	 * for Database parse usage only.
	 */
	//Constructors
	public Coupon() {} //DB update usage only
	
	/**
	 * Construct the coupon object given no Id data. 
	 * for Database parse usage only.
	 * @throws CouponsException  Amount, Price and Data Exceptions.
	 */	
	
	public Coupon(String title, Date startDate, Date endDate, int amount,
			CouponType type, String message, double price, String image) throws CouponsException {
		try {
			setTitle(title);
			setStartDate(startDate);
			setEndDate(endDate);
			setAmount(amount);
			setType(type);
			setMessage(message);
			setPrice(price);
			setImage(image);
		} catch (CouponsException e) {
			throw new CouponsException(e.getMessage());
		}
	}
	
	/**
	 * Construct the coupon object given full data.	  
	 * @throws CouponsException 
	 */	
	public Coupon(long id, String title, Date startDate, Date endDate,
			int amount, CouponType type,String message, double price, String image) throws CouponsException {		
		this(title, startDate, endDate, amount ,type, message, price, image);
		setId(id);		
	}
	
	//Getters And Setter
	/**
	 * 
	 * @return coupon's id.
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * 
	 * @param id holds the id of the coupon.
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return the title of the coupon.
	 */
	public String getTitle() {
		return title;
	}	
	/**
	 * 
	 * @param title holds the title of the coupon
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * 
	 * @return start Date of the Coupon
	 */
	public Date getStartDate() {
		return startDate;
	}
	
	/**
	 * 
	 * @param startDate holds the start date of the coupon.
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	/**
	 * 
	 * @return the expiration date of the coupon
	 */
	
	public Date getEndDate() {
		return endDate;
	}
	
	/**
	 * 
	 * @param set the expiration date of the coupon
	 * @throws CouponsException if end date is before start date.
	 */	
	public void setEndDate(Date endDate) throws CouponsException {
		if (endDate.before(startDate))
			throw new CouponsException("Ending date has to be later than starting date.");
		this.endDate = endDate;
	}
	
	/**
	 * 
	 * @return the amount of the coupons available to purchase.
	 */	
	
	public int getAmount() {		
		return amount;
	}
	
	/**
	 * 
	 * @param amount holds the amount of the coupons available to purchase.
	 * @throws CouponsException if inserted amount is smaller than 0.
	 */
	public void setAmount(int amount) throws CouponsException {
		if (amount < 1)
			throw new CouponsException("Amount has to be an larger than 0.");
		this.amount = amount;
	}
	
	/**
	 * 
	 * @return the type of the coupon.
	 */
	public CouponType getType() {
		return type;
	}
	
	/**
	 * 
	 * @param type holds the type of the coupon.
	 */
	public void setType(CouponType type) {
		this.type = type;
	}
	
	/**
	 * 
	 * @return further details about the coupon.
	 */
	
	public String getMessage() {
		return message;
	}
	
	/**
	 * 
	 * @param message holds further details about the coupon.
	 */

	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * 
	 * @return the price of the coupons.
	 */
	public double getPrice() {
		return price;
	}
	
	/**
	 * 
	 * @param price 
	 * @throws CouponsException 
	 */
	public void setPrice(double price) throws CouponsException {
		if (price < 0)
			throw new CouponsException("Coupon's price has to be larger than 0.");		
		this.price = price;
	}
	
	/**
	 * 
	 * @return the image source file string
	 */	
	public String getImage() {
		return image;
	}
	
	/**
	 * 
	 * @param image holds the source of the file string.
	 */
	public void setImage(String image) {
		this.image = image;
	}	
	//End Getters and Setter.
	
	/**
	 * Returns false if title or id are different
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Coupon)
		{
			Coupon otherCoupon = (Coupon)o;		
			if (otherCoupon.getId() == this.getId() || otherCoupon.getTitle().toLowerCase().equals(this.getTitle().toLowerCase()))
				return true;
		}
		return false;
	}
	
	/**
	 * toString Method: returns the coupon's details to print.
	 */

	@Override
	public String toString() {
		String str = "Coupon's Id:" + getId() + " Title: " + getTitle() + " Start Date: " 
				+ getStartDate().toString() + " End Date: " + getEndDate().toString() + 
				" Amount: " + getAmount() + " Type: " + getType().name() 
				+ " Message: " + getMessage()
				+ " Price:" + getPrice() + " image src: " + getImage()+"\n";
		return str;		
	}
}
