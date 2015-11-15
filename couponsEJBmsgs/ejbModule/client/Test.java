package client;

import java.util.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.logger.Income;
import com.logger.IncomeType;
import com.msg.BusinessDelegate;


public class Test {
	
	public static void main(String[] args){
		
		
		BusinessDelegate bd = new BusinessDelegate();		

		Income i3 = new Income("Moishe", new Date(System.currentTimeMillis()), IncomeType.COMOPANY_UPDATE_COUPON, 100.5);
		bd.storeIncome(i3);
		
		Collection<Income> incomes = bd.viewIncomeByName("Dave");	
		for (Income i : incomes) {
			System.out.println(i.getId() + "\t" + i.getName() + "\t" + i.getDate() + "\t" + i.getAmount() + "\t" + i.getType().getDescription());
		}		
		
		
		

	}
	public static InitialContext getInitialContext(){
		Hashtable<String,String> h=new Hashtable<String,String>();
		h.put("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
		h.put("java.naming.provider.url","localhost");
		try {
			return new InitialContext(h);
		} catch (NamingException e) {
			System.out.println("Cannot generate InitialContext");
			e.printStackTrace();
		}
		return null;
	}

}
