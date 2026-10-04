package com.tka.client;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class SelectOperation {

	public static void main(String[] args) {
		
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		cfg.addAnnotatedClass(Product.class);
		
		SessionFactory factory = cfg.buildSessionFactory();
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		
		Criteria criteria = session.createCriteria(Product.class);
		
		List<Product> allproducts = criteria.list();
		for(Product p : allproducts) {
			System.out.println(p.getP_id() + " " + p.getP_name() + " " + p.getPrice() + " " + p.getCat() + " " + p.getQty());
		}

	}

}
