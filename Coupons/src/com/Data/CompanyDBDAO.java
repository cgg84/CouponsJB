package com.Data;

import java.sql.*;
import java.util.*;

import Exceptions.CouponsException;

import com.Interfaces.CompanyDAO;


/**
 * Class Manages the Company's objects connection to the database.
 * @author Chen Grinberg
 *
 */
public class CompanyDBDAO implements CompanyDAO {

	private ConnectionPool cp;	
	
	
	/**
	 *  Gets singleton Instance of the connection pool
	 * @throws CouponsException
	 */

	public CompanyDBDAO() throws CouponsException {
		cp = ConnectionPool.getInstance();
	}
	
	/**
	 * Creates entry of company in the company DB table by getting details from company java object.
	 */
	@Override
	public void createCompany(Company c) throws CouponsException {
		
		Connection conn = cp.getConnection();
		PreparedStatement ps;

		try {
			ps = conn.prepareStatement("INSERT INTO APP.COMPANY (COMP_NAME, PASSWORD, EMAIL) "
										+ "VALUES (?, ?, ?)",PreparedStatement.RETURN_GENERATED_KEYS);
					
			// Id is set Automatically.
			ps.setString(1, c.getCompName()); // Coupon Title
			ps.setString(2, c.getPassword()); // StartDate
			ps.setString(3, c.getEmail()); // EndDate			

			int affectedRows = ps.executeUpdate();
			
			if (affectedRows == 0)
				throw new CouponsException("Creating Company failed, no rows affected.");

			//returns the generated company key
			ResultSet generatedKey = ps.getGeneratedKeys();

			if (generatedKey.next()) {
				c.setId(generatedKey.getLong(1));
			} else {
				throw new CouponsException("Creating Company failed, no ID obtained.");
			}

		} catch (SQLException e) {
			throw new CouponsException("Company Creation encoutered a general error. Contact your administrator." ,e);
		} finally {
			cp.returnConnection(conn);
		}
	}
	
	/**
	 * Remove entry of company from the company DB table by getting details from company java object.
	 * The functions remove the instances of the coupon in the join Customer_Coupon table and Company_Coupon table
	 */
	@Override
	public void removeCompany(Company c) throws CouponsException {

		Connection conn = cp.getConnection();
		PreparedStatement ps;
		
		Set<Coupon> coupons = c.getCoupons();

		try {
			//Remove company's coupons From Customers Coupons Table
			if (coupons != null)
			{
				for (Coupon cpn : coupons)
				{
					long id = cpn.getId();
					
					ps = conn.prepareStatement("DELETE FROM APP.CUSTOMER_COUPON WHERE COUPON_ID = ?" );
					ps.setLong(1, id);

					ps.executeUpdate();						
				}
			}			
			//Remove company's coupons From Company's Coupons Table			
			ps = conn.prepareStatement("DELETE FROM APP.COMPANY_COUPON WHERE COMP_ID = ?");
			ps.setLong(1, c.getId());

			ps.executeUpdate();			

			//Remove company's coupons From Company
			ps = conn.prepareStatement("DELETE FROM APP.COMPANY WHERE id = ?");
			ps.setLong(1, c.getId());

			ps.executeUpdate();

		} catch (SQLException e) {
			throw new CouponsException("Deleting company encountered a general error. Conact your administer." ,e);
		} finally {
			cp.returnConnection(conn);
		}
	}
	
	/**
	 * Updates entry of company in the company DB table by getting details from company java object
	 */	
	@Override
	public void updateCompany(Company c) throws CouponsException {
		
		Connection conn = cp.getConnection();
		
		try {		
			PreparedStatement ps;		
			ps = conn.prepareStatement("UPDATE APP.COMPANY SET COMP_NAME = ?, PASSWORD = ?,"
					+ " EMAIL = ? WHERE (ID = ?)");			

			ps.setString(1,c.getCompName());
			ps.setString(2,c.getPassword()); 
			ps.setString(3,c.getEmail());
			ps.setLong(4,c.getId());

			ps.executeUpdate();


		} catch (SQLException e) {
			throw new CouponsException("Updating Company uncountered a general Error. Contact your administrator",e);
		} finally {
			cp.returnConnection(conn);
		}	

	}
	
	/**
	 * Reads company in the DB table by given Id number.
	 * Gets the coupons of the company as well.
	 * @return Company object as a whole [include coupons].
	 */	
	@Override
	public Company getCompany(long id) throws CouponsException {
		
		Connection conn = cp.getConnection();
		Statement stmt;
		ResultSet rs;
		
		Company c = new Company();
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM app.Company where (Id="+id+")");
			while (rs.next()) 
			{
				c.setId(rs.getLong(1));
				c.setCompName(rs.getString(2));
				c.setPassword(rs.getString(3));
				c.setEmail(rs.getString(4));
			}						
		} catch (SQLException e) {
			throw new CouponsException("Company was not found." ,e);
		} finally {
			cp.returnConnection(conn);
		}
		return c;
	}
	
	/**
	 * Reads company in the DB table by given Id number.
	 * Gets the coupons of the company as well.
	 * @return Company object as a whole [include coupons].
	 */		
	@Override
	public Company getCompanyByName(String name) throws CouponsException {
		Connection conn = cp.getConnection();
		Statement stmt;
		ResultSet rs;
		
		Company c = new Company();
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM app.Company where (COMP_NAME='"+ name +"')");
			while (rs.next()) 
			{
				c.setId(rs.getLong(1));
				c.setCompName(rs.getString(2));
				c.setPassword(rs.getString(3));
				c.setEmail(rs.getString(4));
			}						
		} catch (SQLException e) {
			throw new CouponsException("Company was not found." ,e);
		} finally {
			cp.returnConnection(conn);
		}
		return c;
	}
	
	

	
	/**
	 * Reads all companies in the DB table.
	 * Gets the coupons of the company as well.
	 * @return Set of each company object as a whole [include coupons].
	 */	
	@Override
	public Set<Company> getAllCompanies() throws CouponsException {

		Connection conn = cp.getConnection();
		Statement stmt;
		ResultSet rs;		
		Set<Company> cl = new HashSet<Company>();		
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM app.Company");
			while (rs.next()) 
			{
				Company c = new Company(); //Creates new Coupon instance
				
				c.setId(rs.getLong(1));
				c.setCompName(rs.getString(2));
				c.setPassword(rs.getString(3));
				c.setEmail(rs.getString(4));
				cl.add(c);
			}			
		} catch (SQLException e) {
			throw new CouponsException("Companies loading encoutered a general error. Contact your administrator." ,e);
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
	public Set<Coupon> getCoupons(Company c) throws CouponsException { 
		
		Connection conn = cp.getConnection();
		Statement stmt;
		ResultSet rs;
		CouponDBDAO cpnDAO = new CouponDBDAO(); 
		
		List<Long> cpnsIds = new ArrayList<Long>(); //Id's List from join table
		Set<Coupon> cl = new HashSet<Coupon>(); //Coupons list.
		
		try { 
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT Coupon_ID FROM app.Company_Coupon where (Comp_Id="+c.getId()+")");
			
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
			throw new CouponsException("Companies loading encoutered a general error. Contact your administrator.", e);
		} finally {
			cp.returnConnection(conn);
		}
		return cl;
	}
	
	/**
	 * Updates the join table Company_Coupon - first delete an old entries, 
	 * last update the correct list of coupons.
	 * @param c : Company to update.
	 * @throws CouponsException
	 */
	
	public void updateCouponsList(Company c) throws CouponsException {

		Connection conn = cp.getConnection();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement("DELETE FROM APP.COMPANY_COUPON WHERE COMP_ID = ?");
			ps.setLong(1, c.getId());

			ps.executeUpdate();

			for (Coupon cpn : c.getCoupons()) {

				ps = conn.prepareStatement("insert into APP.COMPANY_COUPON (COMP_ID, COUPON_ID) values(?, ?)");
				ps.setLong(1, c.getId());
				ps.setLong(2, cpn.getId());

				ps.executeUpdate();
			}

		} catch (SQLException e) {
			throw new CouponsException("Companies coupons loading encoutered a general error. Contact your administrator.", e);
		} finally {
			cp.returnConnection(conn);
		}
	}
	
	/**
	 * returns true if login had succeed.
	 */

	@Override
	public boolean login(String compName, String passowrd) throws CouponsException {
		Connection conn = cp.getConnection();
		Statement stmt;
		ResultSet rs;		
		
		try {
			stmt = conn.createStatement();
			//gets the company name and password and perform check with the given compName and password, if found returns true - else false.
			rs = stmt.executeQuery("Select COMP_NAME,PASSWORD from APP.COMPANY Where COMP_NAME='" + compName + "'");
			while (rs.next()) 
			{
				String dbName = rs.getString(1);
				String dbPass = rs.getString(2);
				
				if (dbName.equals(compName) && dbPass.equals(passowrd))
					return true;
			}
		
		} catch (SQLException e) {
			throw new CouponsException("Customers loading encoutered a general error. Contact your administrator." ,e);
		} finally {
			cp.returnConnection(conn);
		}
		return false;
	}
}
