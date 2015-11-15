package Exceptions;

@SuppressWarnings("serial")
public class CouponsException extends Exception {
	
	public CouponsException(String msg)	
	{
		super(msg);
	}
	
	public CouponsException(String msg, Exception e)	
	{
		super(msg,e);
	}
}
