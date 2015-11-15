package com.Data;

import java.sql.*;
import java.util.*;

import Exceptions.CouponsException;

import com.Data.Coupon.CouponType;
import com.Interfaces.CustomerDAO;

/**
 * Class Manages the Customer's objects connection to the database.
 * @author Chen Grinberg
 *
 */

public class CustomerDBDAO implements CustomerDAO {
	
	private ConnectionPool cp;
	
	
	/**
	 *  Gets singleton Instance of the connection pool
	 * @throws CouponsException
	 */
	
	public CustomerDBDAO() throws CouponsException
	{
		cp = ConnectionPool.getInstance();
	}
	
	/**
	 * Creates entry of customer in the company DB table by getting details from company java object.
	 */	

	@Override
	public void createCustomer(Customer c) throws CouponsException {
		Connection conn = cp.getConnection();
		PreparedStatement ps;

		try {
			ps = conn.prepareStatement("INSERT INTO APP.CUSTOMER (CUST_NAME, PASSWORD, EMAIL) "
										+ "VALUES (?, ?, ?)",PreparedStatement.RETURN_GENERATED_KEYS);
					
			// Id is set Automatically.
			ps.setString(1, c.getCustName()); // Customer Title
			ps.setString(2, c.getPassword()); // Password
			ps.setString(3, c.getEmail()); // Mail			

			int affectedRows = ps.executeUpdate();

			if (affectedRows == 0)
				throw new SQLException(
						"Creating Customer failed, no rows affected.");

			ResultSet generatedKey = ps.getGeneratedKeys();
			
			
			//returns the generated customer key
			if (generatedKey.next()) {
				c.setId(generatedKey.getLong(1));
			} else {
				throw new SQLException(
						"Creating Customer failed, no ID obtained.");
			}

		} catch (SQLException e) {
			throw new CouponsException("Customer Creation encoutered a general error. Contact your administrator.", e);
		} finally {
			cp.returnConnection(conn);
		}
	}
	
	/**
	 * Remove entry of customer from the company DB table by getting details from company java object.
	 * The functions remove the instances of the coupon in the join Customer_Coupon table.
	 */

	@Override
	public void removeCustomer(Customer c) throws CouponsException {
		
		Connection conn = cp.getConnection();
		PreparedStatement ps;

		try {
			//delete from join table
			ps = conn.prepareStatement("DELETE FROM APP.CUSTOMER_COUPON WHERE CUST_ID = ?");
			ps.setLong(1, c.getId());

			ps.executeUpdate();
			
			//delete from customers table
			ps = conn.prepareStatement("DELETE FROM APP.CUSTOMER WHERE id = ?");
			ps.setLong(1, c.getId());

			int affectedRows = ps.executeUpdate();

			if (affectedRows == 0)
				throw new SQLException(	"Deleting a customer failed, no rows affected.");

		} catch (SQLException e) {
			throw new CouponsException("Deleting a customer encountered a general error. Conact your administer.",e);
		} finally {
			cp.returnConnection(conn);
		}
	}
	
	
	
	/**
	 * Updates entry of company in the company DB table by getting details from company java object
	 */	
	@Override
	public void updateCustomer(Customer c) throws CouponsException {
		Connection conn = cp.getConnection();
		PreparedStatement ps;

		try {
			ps = conn.prepareStatement("UPDATE APP.CUSTOMER SET CUST_NAME = ?, PASSWORD = ?,"
					+ "EMAIL = ? WHERE (ID = ?)");			
			
			ps.setString(1,c.getCustName()); 
			ps.setString(2,c.getPassword()); 
			ps.setString(3, c.getEmail());
			ps.setLong(4, c.getId());

			ps.executeUpdate();
		
			
		} catch (SQLException e) {
			throw new CouponsException("Updating a customer uncountered a general Error. Contact your administrator.",e);
		} finally {
			cp.returnConnection(conn);
		}
	}
	
	/**
	 * Reads customer from the DB table by given Id number.
	 * Gets the coupons of the customer as well.
	 * @return a customer object.
	 */	

	@Override
	public Customer getCustomer(long id) throws CouponsException {
		
		Connection conn = cp.getConnection();
		Statement stmt;
		ResultSet rs;
		Customer c = new Customer();		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM app.CUSTOMER where (Id="+id+")");
			while (rs.next()) 
			{				
				c.setId(rs.getLong(1));
				c.setCustName(rs.getString(2));
				c.setPassword(rs.getString(3));
				c.setEmail(rs.getString(4));				
			}		
			
		} catch (SQLException e) {
			throw new CouponsException("Customer was not found.");
		} finally {
			cp.returnConnection(conn);
		}
		return c;
	}
	
	/**
	 * Reads customer from the DB table by given Customer Name.
	 * Gets the coupons of the customer as well.
	 * @return a customer object.
	 */	
	
	public Customer getCustomerByName(String name) throws CouponsException{
		
		Connection conn = cp.getConnection();
		Statement stmt;
		ResultSet rs;
		Customer c = new Customer();		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM app.CUSTOMER where (CUST_NAME='"+name+"')");
			while (rs.next()) 
			{				
				c.setId(rs.getLong(1));
				c.setCustName(rs.getString(2));
				c.setPassword(rs.getString(3));
				c.setEmail(rs.getString(4));				
			}		
			
		} catch (SQLException e) {
			throw new CouponsException("Customer was not found.",e);
		} finally {
			cp.returnConnection(conn);
		}
		return c;

	}
	
	/**
	 * Reads all customers in the DB table.
	 * Gets the coupons of the customers as well.
	 * @return Set of each customer object.
	 */	

	@Override
	public Set<Customer> getAllCustomers() throws CouponsException {
		
		Connection conn = cp.getConnection();
		Statement stmt;
		ResultSet rs;		
		Set<Customer> cl = new HashSet<Customer>();
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM app.Customer");
			while (rs.next()) 
			{
				Customer c = new Customer(); //Creates new Customer instance
				
				c.setId(rs.getLong(1));
				c.setCustName(rs.getString(2));
				c.setPassword(rs.getString(3));
				c.setEmail(rs.getString(4));
				cl.add(c);
			}			
		} catch (SQLException e) {
			throw new CouponsException("Customers loading encoutered a general error. Contact your administrator.",e);
		} finally {
			cp.returnConnection(conn);
		}
		return cl;
	}
	
	/**
	 * Reads all the coupons in the DB table.
	 * @return Set of coupons.
	 */	
	@Override
	public Set<Coupon> getAllCoupons() throws CouponsException {
		
		Connection conn = cp.getConnection();
		Statement stmt;
		ResultSet rs;		
		Set<Coupon> cl = new HashSet<Coupon>();		
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM app.Coupon");
			while (rs.next()) 
			{
				Coupon c = new Coupon(); //Creates new Coupon instance
				c.setId(rs.getLong(1));
				c.setTitle(rs.getString(2));
				c.setStartDate(rs.getDate(3));
				c.setEndDate(rs.getDate(4));
				c.setAmount(rs.getInt(5));
				c.setType(CouponType.valueOf(rs.getString(6)));
				c.setMessage(rs.getString(7));
				c.setPrice(rs.getDouble(8));
				c.setImage(rs.getString(9));
				cl.add(c);
			}
		} catch (SQLException e) {
			throw new CouponsException("Coupon was not found." ,e);
		} finally {
			cp.returnConnection(conn);
		}
		return cl;
	}
	
	/**
	 * Reads all coupons in the DB join table.
	 * @return Set of coupons that belongs to company c.
	 */
	@Override
	public Set<Coupon> getCoupons(Customer c) throws CouponsException {
		
		Connection conn = cp.getConnection();
		Statement stmt;
		ResultSet rs;
		CouponDBDAO cpnDAO = new CouponDBDAO(); 
		
		List<Long> cpnsIds = new ArrayList<Long>(); //List of ids from join table
		Set<Coupon> cl = new HashSet<Coupon>();	//Client's Coupons to be returned
		
		
		try { 
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT Coupon_ID FROM app.Customer_Coupon where (Cust_Id="+c.getId()+")");
			
			while (rs.next()) //Retrieve the id's of customers coupon
			{
				cpnsIds.add(rs.getLong(1));
			}
			
			for (Long cpnid : cpnsIds) //Retrieve the coupon details for each id in the list.
			{
				Coupon cpn = cpnDAO.getCoupon(cpnid);
				cl.add(cpn);
			}			
			
		} catch (SQLException e) {
			throw new CouponsException("Customers loading encoutered a general error. Contact your administrator.",e);
		} finally {
			cp.returnConnection(conn);
		}

		return cl;
	}
	
	/**
	 * Updates the join table Customer_Coupon - first delete an old entries, 
	 * last update the correct list of coupons.
	 * @param c : Customer to update.
	 * @throws CouponsException
	 */
	
	public void updateCouponsList(Customer c) throws CouponsException {

		Connection conn = cp.getConnection();
		PreparedStatement ps;
		try {
			//Delete the entry from the join table, and insert the updated coupons instead.
			ps = conn.prepareStatement("DELETE FROM APP.CUSTOMER_COUPON WHERE CUST_ID = ?");
			ps.setLong(1, c.getId());
			ps.executeUpdate();
			
			for (Coupon cpn : c.getCoupons()) {

				ps = conn.prepareStatement("insert into APP.CUSTOMER_COUPON (CUST_ID, COUPON_ID) values(?, ?)");
				ps.setLong(1, c.getId());
				ps.setLong(2, cpn.getId());

				ps.executeUpdate();
			}

		} catch (SQLException e) {
			throw new CouponsException("Customers coupons loading encoutered a general error. Contact your administrator.",e);
		} finally {
			cp.returnConnection(conn);
		}
	}
	
	
	/**
	 * returns true if login had succeed.
	 */
	@Override
	public boolean login(String custName, String passowrd) throws CouponsException {
		
		Connection conn = cp.getConnection();
		Statement stmt;
		ResultSet rs;		
		//gets the customer name and password and perform check with the given custName and password, if found returns true - else false.
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("Select CUST_NAME,PASSWORD from APP.CUSTOMER Where CUST_NAME='" + custName + "'");
			while (rs.next()) 
			{
				String dbName = rs.getString(1);
				String dbPass = rs.getString(2);
				
				if (dbName.equals(custName) && dbPass.equals(passowrd))
					return true;
			}
		
		} catch (SQLException e) {
			throw new CouponsException("Customers loading encoutered a general error. Contact your administrator.",e);
		} finally {
			cp.returnConnection(conn);
		}
		return false;
	}	
}
