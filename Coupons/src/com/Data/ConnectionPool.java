package com.Data;

import java.sql.*;
import java.util.*;

import Exceptions.CouponsException;

/**
 * The Class manages the connections to the database.
 * @author Grinberg
 *
 */
public class ConnectionPool {
	
	private static ConnectionPool ConnectionPool; //Singleton
	private HashSet <Connection> availableCons;
	private HashSet <Connection> usedConnections; //apply close all connections.
	private static int MAX_CONNECTIONS = 5; //Defines maximum connections available

	/**
	 * create a singleton list object of connections allow MaxConnections(=5) different clients to connect the database simultaneously
	 * 
	 * @throws CouponsException
	 */
	
	private ConnectionPool() throws CouponsException
	{
		availableCons = new HashSet<Connection>();
		usedConnections = new HashSet<Connection>();
		
		String driver = "org.apache.derby.jdbc.ClientDriver";
		String connectionURL = "jdbc:derby://localhost:1527/CouponsDB";		
		
		for (int i=0;i<MAX_CONNECTIONS;i++)
		{
			try {
				Class.forName(driver);
			} catch (java.lang.ClassNotFoundException e) {
				throw new CouponsException("There has been a general problem connecting to DB. Contact your administrator", e);
			}
			try {
				availableCons.add(DriverManager.getConnection(connectionURL)); 
			} catch (SQLException e) {
				throw new CouponsException("There has been a general problem connecting to DB, Contact your administrator", e);
			}
		}		
	}
	
	/**
	 * 
	 * @return an instance of connection Pool. if doesn't exists, creating one (and only one...).
	 * @throws CouponsException
	 */
	
	public static ConnectionPool getInstance() throws CouponsException
	{		
		if (ConnectionPool == null)
			try {
				ConnectionPool = new ConnectionPool();
			} catch (Exception e) {
				throw new CouponsException("There has been a general problem connecting to DB. Contact your administrator", e);
			}
		return ConnectionPool;
	}
	
	/**
	 * 
	 * @return connection if available, if availableCons is empty, send the thread to wait until notified.
	 * adding the used connection as a reference to the connection.
	 * @throws CouponsException
	 */
	
	public synchronized Connection getConnection() throws CouponsException
	{
		if (availableCons.isEmpty()) //all connections are being used.
		{
			try {
				wait();
			} catch (InterruptedException e) {
				throw new CouponsException("There has been a general problem connecting to DB. Contact your administrator", e);
			}
		}		
		Connection con = availableCons.iterator().next();
		usedConnections.add(con);
		availableCons.remove(con);		
		return con;			
	}
	
	/**
	 * return connection to the pool and remove from usedConnections. 
	 * then notify the waiting threads (if there are any).
	 * @param con
	 */
	
	public synchronized void returnConnection(Connection con) 
	{
		availableCons.add(con);	
		usedConnections.remove(con);
		notify();
	}
	
	/**
	 * Closes all of the connections (Connections that are currently in use, and available ones).
	 * @throws CouponsException
	 */
	
	public void closeAllConnections() throws CouponsException
	{
		try {
			for (Connection c:availableCons)			
				c.close();
			for (Connection c:usedConnections)
				c.close();
		}catch (SQLException e) {
				throw new CouponsException("There has been a general problem closingthe DB. Contact your administrator", e);			
		}
	}

}
