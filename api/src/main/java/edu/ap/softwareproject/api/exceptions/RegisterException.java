package edu.ap.softwareproject.api.exceptions;

import edu.ap.softwareproject.api.enums.RegisterExceptionReason;

public class RegisterException extends Exception {
    public final RegisterExceptionReason reason;

    public RegisterException(RegisterExceptionReason reason) {
        this.reason = reason;
    }
}
