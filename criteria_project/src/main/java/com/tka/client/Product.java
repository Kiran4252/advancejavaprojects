package com.tka.client;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "product_table", schema= "advjava433batch_db")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int p_id;
	
	@Column(name = "p_name")
	private String p_name;
	private int price;
	@Column(name = "category")
	private String cat;
	@Column(name = "quantity")
	private int qty;
	
	public Product() {
		
	}
	
	public Product(int p_id, String p_name, int price, String cat, int qty) {
		super();
		this.p_id = p_id;
		this.p_name = p_name;
		this.price = price;
		this.cat = cat;
		this.qty = qty;
	}
	
	public Product(String p_name, int price, String cat, int qty) {
		super();
		this.p_name = p_name;
		this.price = price;
		this.cat = cat;
		this.qty = qty;
	}

	public int getP_id() {
		return p_id;
	}

	public void setP_id(int p_id) {
		this.p_id = p_id;
	}

	public String getP_name() {
		return p_name;
	}

	public void setP_name(String p_name) {
		this.p_name = p_name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getCat() {
		return cat;
	}

	public void setCat(String cat) {
		this.cat = cat;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	@Override
	public String toString() {
		return "Product [p_id=" + p_id + ", p_name=" + p_name + ", price=" + price + "]";
	}
	
	

}
