package com.revature.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	private static final Logger log = LogManager.getLogger(HibernateUtil.class);
	private static SessionFactory sessionFactory;

	// Create singleton for sessionFactory
	private static SessionFactory createSessionFactory() {

		/*
		 * Use hibernate.cf.xml to create a new config and then return the Session
		 * Factory that's created using the config.
		 */

		try {
			log.info("Configuring hibernate");
			Configuration config = new Configuration();
			config.configure("hibernate.cfg.xml");
			addAnnotatedClasses(config);
			return config.buildSessionFactory();
		} catch (Exception e) {
			log.error("Exceoption raised while configuring hibernate.", e);
			throw new ExceptionInInitializerError();
		}
	}

	// This is a singleton. There should only be one instance per application.
	public static SessionFactory getSessionFactory() {
		return (sessionFactory == null) ? sessionFactory = createSessionFactory() : sessionFactory;
	}

	/*
	 * Iterates through class files of specified directory and maps those which
	 * describe hibernate entities to the config file.
	 */

	private static void addAnnotatedClasses(Configuration config) throws Exception {
		log.info("Adding annotated classes.");
		@SuppressWarnings("rawtypes")
		List<Class> classPaths = new ArrayList<>();
		File[] files = new File("./src/main/java/com.revature/models").listFiles();

		for (File file : files) {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			log.info("Looking at " + file.getName());
			while (line != null) {
				if (line.contains("@Entity")) {
					String className = file.getName().substring(0, file.getName().length() - 5);
					classPaths.add(Class.forName("com.revature.models" + className));
					log.info("Added " + file.getName() + " to the list of valid annotated classes.");
					break;
				}
				line = reader.readLine();
			}
			reader.close();
		}
		log.info("Calling addAnnotatedClass on valid class files.");
		classPaths.forEach(entityClass -> config.addAnnotatedClass(entityClass));
		log.info("All annotated classes added.");
	}
}
