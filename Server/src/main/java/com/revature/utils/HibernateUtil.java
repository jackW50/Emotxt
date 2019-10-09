package com.revature.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.revature.models.Account;
import com.revature.models.Channel;
import com.revature.models.Message;
import com.revature.models.MessageBoard;
import com.revature.models.Status;
import com.revature.models.User;

public class HibernateUtil {

	private static final Logger log = LogManager.getLogger(HibernateUtil.class);
	private static SessionFactory sessionFactory;

	// Create singleton for sessionFactory
	private static SessionFactory buildSessionFactory() {

		/*
		 * Use hibernate.cf.xml to create a new config and then return the Session
		 * Factory that's created using the config.
		 */

		try {
			log.info("Configuring hibernate");
			Configuration config = new Configuration();
			config.configure("hibernate.cfg.xml");
			config.addAnnotatedClass(Account.class);
			config.addAnnotatedClass(Channel.class);
			config.addAnnotatedClass(Message.class);
			config.addAnnotatedClass(MessageBoard.class);
			config.addAnnotatedClass(Status.class);
			config.addAnnotatedClass(User.class);
			return config.buildSessionFactory();
		} catch (Exception e) {
			log.error("Exceoption raised while configuring hibernate.", e);
			throw new ExceptionInInitializerError();
		}
	}

	// This is a singleton. There should only be one instance per application.
	public static SessionFactory getSessionFactory() {
		return (sessionFactory == null) ? sessionFactory = buildSessionFactory() : sessionFactory;
	}
}
