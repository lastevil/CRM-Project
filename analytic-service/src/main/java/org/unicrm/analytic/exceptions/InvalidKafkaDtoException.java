package org.unicrm.analytic.exceptions;

public class InvalidKafkaDtoException extends RuntimeException{
    public InvalidKafkaDtoException(String s){
        super(s);
    }
}
