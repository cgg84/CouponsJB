package com.Service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.logger.Income;

/**
 * Session Bean implementation class IncomeServiceBean
 */
@Stateless(name="IncomeService")
public class IncomeServiceBean implements IncomeService {
	
	//entity manager from Persistence Context
	@PersistenceContext(unitName="coupons") private EntityManager em;
	
    public IncomeServiceBean() { }

	public void storeIncome(Income i) {
		em.persist(i);		
	}
	
	public Collection<Income> ViewAllIncome() {
		Query query;
		query = em.createQuery("SELECT i FROM Income AS i");		
		
		@SuppressWarnings("unchecked")
		Collection<Income> incomes = (Collection<Income>)query.getResultList();
		return incomes;		
	}


	public Collection<Income> viewAllIncomeByName(String name) {		
		Query query;
		query = em.createQuery("SELECT i FROM Income AS i WHERE i.name = ?1");
		query.setParameter(1,name);
		
		@SuppressWarnings("unchecked")
		Collection<Income> incomes = (Collection<Income>)query.getResultList();
		return incomes;
	}
}
