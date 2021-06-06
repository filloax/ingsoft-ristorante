package it.unibo.ingsoft.fortuna.log;

import java.util.Arrays;

public enum LogManagerServices {
    LOG(0),
    LIST_FILES(1);

    private final int value;
    public int getValue() { return value; }
    
    public static LogManagerServices get(int value) {
        return Arrays.stream(LogManagerServices.values()).filter(el -> el.getValue() == value).findFirst().get();
    }

    private LogManagerServices(int value) {
        this.value = value;
    }
}
