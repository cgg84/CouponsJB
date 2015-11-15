package com.Interfaces;


import Exceptions.CouponsException;

import com.Program.CouponSystem.ClientType;

public interface CouponClientFacade {
	
	public CouponClientFacade login(String name,String psrd, ClientType ct ) throws CouponsException;

}
