package com.bazaarvoice.commons.data.model.json.schema.validation;

public enum ResultType {
    /** Type of object not allowed */
    INCOMPATIBLE_TYPE(ResultSeverity.ERROR),

    /** Value is required */
    REQUIRED_VALUE(ResultSeverity.ERROR),

    /** Value cannot be null */
    NULL_VALUE(ResultSeverity.ERROR),

    /** Format doesn't match spec */
    FORMAT_MISMATCH(ResultSeverity.ERROR),

    /** Format cannot be checked */
    FORMAT_NOT_VALIDATED(ResultSeverity.WARNING),

    /** Value isn't bound by the constraints */
    CONSTRAINT_VIOLATION(ResultSeverity.ERROR),

    /** $schema is missing from the object when necessary to disambiguate a union schema */
    MISSING_SCHEMA(ResultSeverity.WARNING),

    /** $schema isn't a string */
    INVALID_SCHEMA(ResultSeverity.ERROR),
    ;

    private final ResultSeverity _severity;

    private ResultType(ResultSeverity severity) {
        _severity = severity;
    }

    public ResultSeverity getSeverity() {
        return _severity;
    }
}
