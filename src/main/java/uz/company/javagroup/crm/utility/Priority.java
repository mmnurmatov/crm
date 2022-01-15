package uz.isd.javagroup.grandcrm.utility;

import java.io.Serializable;

public enum Priority implements Serializable {

    LOW, MEDIUM, HIGH;

    public String getString() {
        return this.name();
    }
}
