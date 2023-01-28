package org.unicrm.auth.exceptions;

import java.util.List;
import java.util.stream.Collectors;

public class ValidationException extends RuntimeException {
    private List<String> errorFieldsMessages;

    public ValidationException(List<String> errorFieldsMessages) {
        super(errorFieldsMessages.stream().collect(Collectors.joining(", ")));
        this.errorFieldsMessages = errorFieldsMessages;
    }

    public List<String> getErrorFieldsMessages() {
        return errorFieldsMessages;
    }

    public void setErrorFieldsMessages(List<String> errorFieldsMessages) {
        this.errorFieldsMessages = errorFieldsMessages;
    }
}
