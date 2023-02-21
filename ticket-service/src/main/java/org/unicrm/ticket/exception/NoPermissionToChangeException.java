package org.unicrm.ticket.exception;

public class NoPermissionToChangeException extends RuntimeException {

    public NoPermissionToChangeException(String message) {
        super(message);
    }
}
