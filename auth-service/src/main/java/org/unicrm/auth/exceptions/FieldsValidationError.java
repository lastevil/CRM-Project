package org.unicrm.auth.exceptions;

import java.util.List;

public class FieldsValidationError {
    private List<String> message;

    public FieldsValidationError() {
    }

    public FieldsValidationError(List<String> message) {
        this.message = message;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }
}
