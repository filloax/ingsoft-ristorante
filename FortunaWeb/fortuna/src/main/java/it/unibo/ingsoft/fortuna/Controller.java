package it.unibo.ingsoft.fortuna;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;

public abstract class Controller {

	private Connection connessioneDB;

	// "jdbc:db2://diva.disi.unibo.it:50000/sample" esempio String database
	private void apriConnessione(String database) throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		String password, user;
		Scanner sc = new Scanner(System.in);
		
		Class.forName("com.ibm.db2.jcc.DB2Driver").getDeclaredConstructor().newInstance();
		
		System.out.println("Inserire user: ");
		user = sc.nextLine();
		System.out.println("Inserire password: ");
		password = sc.nextLine();
		sc.close();

		connessioneDB = DriverManager.getConnection(database, user, password);
	}

	protected Connection getConnection() {
		return connessioneDB;
	}

	protected void scriviMessaggio(String msg) {
		// aprire file e scrivere msg, non ricordo se andava bene "File" o meno
	}

	protected void scriviOperazione(String ip, String operazione) {
		// same rispetto a sopra
	}
}
