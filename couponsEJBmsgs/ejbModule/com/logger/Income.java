package com.logger;
import java.io.Serializable;


import java.util.Date;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table
@XmlRootElement
public class Income implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private Date date;
	private IncomeType type;
	private double amount;
	
	public Income() { }	
	
	public Income(String name, Date date, IncomeType type, double amount) {			
		this.setName(name);
		this.setDate(date);
		this.setType(type);
		this.setAmount(amount);
	}
	
	@Id
	@GeneratedValue
	@Column(nullable=false)
	public long getId() {return id;	}
	public void setId(long id) {this.id = id;}	

	
	@Column(nullable=false)
	public String getName() {return name;}
	public void setName(String name) {this.name = name;	}
	
	@Column(nullable=false)
	public Date getDate() {return date;}
	public void setDate(Date date) {this.date = date;	}
	
	@Column(nullable=false)
	public IncomeType getType() {return type;}
	public void setType(IncomeType type) {this.type = type;	}

	@Column(nullable=false)
	public double getAmount() {	return amount; 	} 
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
}

