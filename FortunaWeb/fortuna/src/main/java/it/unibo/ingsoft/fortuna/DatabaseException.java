package it.unibo.ingsoft.fortuna;

import org.hibernate.boot.model.relational.Database;

public class DatabaseException extends Exception {
    public DatabaseException(String msg) {
        super(msg);
    }

    public DatabaseException(String msg, Throwable e) {
        super(msg, e);
    }
}
