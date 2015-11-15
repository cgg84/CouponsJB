package com.Data;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import Exceptions.CouponsException;

import com.Data.Coupon.CouponType;
import com.Interfaces.CouponDAO;


/**
 * Class Manages the coupon's objects connection to the database.
 * @author Chen Grinberg
 *
 */

public class CouponDBDAO implements CouponDAO {
	
	private ConnectionPool cp;
	
	
	/**
	 *  Gets singleton Instance of the connection pool
	 * @throws CouponsException
	 */
	public CouponDBDAO() throws CouponsException
	{
		cp = ConnectionPool.getInstance();
	}	
	
	/**
	 * Creates entry of a coupon in the company DB table by getting details from company java object
	 */
	@Override
	public void createCoupon(Coupon c) throws CouponsException {
		Connection conn = cp.getConnection();
		PreparedStatement ps;

		try {
			ps = conn.prepareStatement("INSERT INTO APP.COUPON "
					+ "(TITLE, START_DATE, END_DATE,"
					+ "AMOUNT, TYPE, MESSAGE, PRICE, IMAGE) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
					PreparedStatement.RETURN_GENERATED_KEYS);
			
			// Id is set Automatically.
			ps.setString(1, c.getTitle()); // Coupon Title
			ps.setDate(2, new java.sql.Date(c.getStartDate().getTime())); // StartDate
			ps.setDate(3, new java.sql.Date(c.getEndDate().getTime())); // EndDate
			ps.setInt(4, c.getAmount()); // Amount of coupons
			ps.setString(5, c.getType().name()); // Category - type
			ps.setString(6, c.getMessage()); // Description
			ps.setDouble(7, c.getPrice()); // Price
			ps.setString(8, c.getImage()); // Picture
			//new java.sql.Date(new java.util.Date().getTime()));
			int affectedRows = ps.executeUpdate();

			if (affectedRows == 0)
				throw new SQLException(
						"Creating coupon failed, no rows affected.");

			ResultSet generatedKey = ps.getGeneratedKeys();
			
			//returns the generated coupon key
			if (generatedKey.next()) {
				c.setId(generatedKey.getLong(1));
			} else {
				throw new SQLException(
						"Creating coupon failed, no ID obtained.");
			}

		} catch (SQLException e) {
			throw new CouponsException("Coupon Creation encoutered a general error. Contact your administrator." ,e);
		} finally {
			cp.returnConnection(conn);
		}

	}	
	/**
	 * Remove entry of coupon from the company DB table by getting details from company java object.
	 * The functions remove the instances of the coupon in the join Customer_Coupon table and Company_Coupon table
	 */
	@Override
	public void removeCoupon(Coupon c) throws CouponsException {

		Connection conn = cp.getConnection();
		PreparedStatement ps;

		try {			
			ps = conn.prepareStatement("DELETE FROM CUSTOMER_COUPON WHERE COUPON_ID = ?");
			ps.setLong(1, c.getId());
			int affectedRows = ps.executeUpdate();
			
			ps = conn.prepareStatement("DELETE FROM COMPANY_COUPON WHERE COUPON_ID = ?");
			ps.setLong(1, c.getId());
			affectedRows = ps.executeUpdate();
			
			//remove coupon from coupons table
			ps = conn.prepareStatement("DELETE FROM APP.COUPON WHERE id = ?");
			ps.setLong(1, c.getId());

			affectedRows = ps.executeUpdate();
			
			if (affectedRows == 0)
				throw new SQLException(	"Deleting coupon failed, no rows affected.");

		} catch (SQLException e) {
			throw new CouponsException("Deleting coupon encountered a general error. Conact your administer." ,e);
		} finally {
			cp.returnConnection(conn);
		}
	}
	
	/**
	 * Updates entry of coupon in the company DB table by getting details from company java object
	 */
	@Override
	public void updateCoupon(Coupon c) throws CouponsException {
		
		Connection conn = cp.getConnection();
		PreparedStatement ps;

		try {
			ps = conn.prepareStatement("UPDATE APP.COUPON SET TITLE = ?,"
					+ " START_DATE = ?, END_DATE = ?,"
					+ " AMOUNT = ?, TYPE = ?, MESSAGE = ?, "
					+ "PRICE = ?,IMAGE = ? WHERE (ID = ?)");
			
			ps.setString(1, c.getTitle()); //Coupon Title
			ps.setDate(2, new java.sql.Date(c.getStartDate().getTime())); // StartDate
			ps.setDate(3, new java.sql.Date(c.getEndDate().getTime())); // EndDate
			ps.setInt(4, c.getAmount()); //Amount of coupons
			ps.setString(5, c.getType().name()); //Category - type
			ps.setString(6, c.getMessage()); //Description
			ps.setDouble(7, c.getPrice()); //Price
			ps.setString(8, c.getImage());	//Picture
			ps.setLong(9, c.getId());

			ps.executeUpdate();
			
		} catch (SQLException e) {
			throw new CouponsException("Updating coupon uncountered a general Error. Contact your administrator." ,e);
		} finally {
			cp.returnConnection(conn);
		}		
	}
	
	/**
	 * Reads coupon in the DB table by given Id number.
	 * @return coupun's object.
	 * 	 */	
	@Override
	public Coupon getCoupon(long id) throws CouponsException {
		
		Connection conn = cp.getConnection();
		Statement stmt;
		ResultSet rs;
		Coupon c = new Coupon();		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM app.Coupon where (Id="+id+")");
			while (rs.next()) 
			{
				c.setId(rs.getLong(1));
				c.setTitle(rs.getString(2));
				c.setStartDate(rs.getDate(3));
				c.setEndDate(rs.getDate(4));
				c.setAmount(rs.getInt(5));
				c.setType(CouponType.valueOf(rs.getString(6)));
				c.setMessage(rs.getString(7));
				c.setPrice(rs.getDouble(8));
				c.setImage(rs.getString(9));				
			}
		} catch (SQLException e) {
			throw new CouponsException("Coupons loading encoutered a general error. Contact your administrator." ,e);
		} finally {
			cp.returnConnection(conn);
		}
		return c;
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
	 * Reads all the coupons in the DB table by Coupon's Type.
	 * @return Set of coupons.
	 */	

	@Override
	public Set<Coupon> getCouponsByType(CouponType ct) throws CouponsException {		
		
		Connection conn = cp.getConnection();
		Statement stmt;
		ResultSet rs;		
		Set<Coupon> cl = new HashSet<Coupon>();		
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM app.Coupon where (Type='"+ct.name()+"')");
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
	
	public Set<Coupon> getCouponsByDate(Date today) throws CouponsException {		
		
		Connection conn = cp.getConnection();
		Statement stmt;
		ResultSet rs;		
		Set<Coupon> cl = new HashSet<Coupon>();			
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM app.Coupon where (END_Date<='"+ today +"')");
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
			throw new CouponsException("Coupons was not found." ,e);
		} finally {
			cp.returnConnection(conn);
		}
		return cl;
	}	

}
