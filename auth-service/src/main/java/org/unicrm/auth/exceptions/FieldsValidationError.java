package org.unicrm.auth.exceptions;

import java.util.List;

public class FieldsValidationError {
    private List<String> errorFieldsMessages;

    public FieldsValidationError() {
    }

    public FieldsValidationError(List<String> errorFieldsMessages) {
        this.errorFieldsMessages = errorFieldsMessages;
    }

    public List<String> getErrorFieldsMessages() {
        return errorFieldsMessages;
    }

    public void setErrorFieldsMessages(List<String> errorFieldsMessages) {
        this.errorFieldsMessages = errorFieldsMessages;
    }
}
