package org.unicrm.auth.exceptions;

public class NotActiveAccountException extends RuntimeException{
    public NotActiveAccountException(String message) {
        super(message);
    }
}
