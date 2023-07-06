package org.unicrm.ticket.exception;

public class NoPermissionToDeleteException extends RuntimeException {

    public NoPermissionToDeleteException(String message) {
        super(message);
    }
}
