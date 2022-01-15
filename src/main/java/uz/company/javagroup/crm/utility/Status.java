package uz.isd.javagroup.grandcrm.utility;

import java.io.Serializable;

public enum Status implements Serializable {

    ACTIVE, PENDING, INACTIVE, REJECTED, DELETED, ANSWERED, CLOSED;

    public String getStatus() {
        return this.name();
    }

}
