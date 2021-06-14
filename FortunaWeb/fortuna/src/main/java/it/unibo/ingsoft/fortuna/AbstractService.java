package it.unibo.ingsoft.fortuna;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;

import org.springframework.beans.factory.annotation.Autowired;

import it.unibo.ingsoft.fortuna.ConfigProps.DatabaseProps;
import it.unibo.ingsoft.fortuna.log.ILogManager;

public abstract class AbstractService {

    @Autowired
    private ConfigProps config;

	@Autowired
	private ILogManager log;

	private Connection connessioneDB;

	// "jdbc:db2://diva.disi.unibo.it:50000/sample" esempio String database
	private void apriConnessione() throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		String password, user;
		DatabaseProps dbConf = config.getDb();

		Class.forName(dbConf.getDriver()).getDeclaredConstructor().newInstance();

		user = dbConf.getUsername();
		password = dbConf.getPassword();

		String uri = String.format("jdbc:%s://%s:%d/%s", dbConf.getDbms(), dbConf.getHost(), dbConf.getPort(), dbConf.getName());

		connessioneDB = DriverManager.getConnection(uri, user, password);
	}

	protected Connection getConnection() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, SQLException {
		if (connessioneDB == null || connessioneDB.isClosed())
			apriConnessione();

		return connessioneDB;
	}

	protected void scriviMessaggio(String msg) {
		log.scriviMessaggio(msg);
	}

	protected void scriviOperazione(String ip, String operazione) {
		log.scriviOperazione(ip, operazione);
	}
}
