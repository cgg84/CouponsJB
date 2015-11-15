package com.Service;

import java.util.Collection;

import javax.ejb.Local;


import com.logger.Income;

@Local
public interface IncomeService {
	
	public void storeIncome(Income i);
	public Collection<Income> ViewAllIncome();
	public Collection<Income> viewAllIncomeByName(String name);
}
