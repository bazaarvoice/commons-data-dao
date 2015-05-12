package com.bazaarvoice.commons.data.model.json.schema.validation;

import java.io.Serializable;

public class ValidationResult implements Serializable {
    private ResultType _type;
    private String _path;
    private String _message;

    public ResultSeverity getSeverity() {
        return _type != null ? _type.getSeverity() : null;
    }

    public ResultType getType() {
        return _type;
    }

    public void setType(ResultType type) {
        _type = type;
    }

    public ValidationResult type(ResultType type) {
        setType(type);
        return this;
    }

    public String getPath() {
        return _path;
    }

    public void setPath(String path) {
        _path = path;
    }

    public ValidationResult path(String path) {
        setPath(path);
        return this;
    }

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        _message = message;
    }

    public ValidationResult message(String message) {
        setMessage(message);
        return this;
    }

    @Override
    public String toString() {
        return super.toString() +
                "[type=" + _type + "]" +
                "[path=" + _path + "]" +
                "[message='" + _message + "']";
    }
}
