package com.bazaarvoice.commons.data.model.json.schema.validation;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ValidationResults implements Serializable, Iterable<ValidationResult> {
    private List<ValidationResult> _results = Lists.newArrayList();

    public boolean hasResults() {
        return !_results.isEmpty();
    }

    public List<ValidationResult> getResults() {
        return _results;
    }

    @Override
    public Iterator<ValidationResult> iterator() {
        return _results.iterator();
    }

    public boolean hasWarningsOrErrors() {
        return !getWarningsAndErrors().isEmpty();
    }

    public Collection<ValidationResult> getWarningsAndErrors() {
        return Collections2.filter(_results, new Predicate<ValidationResult>() {
            @Override
            public boolean apply(ValidationResult result) {
                return result.getSeverity() == ResultSeverity.WARNING || result.getSeverity() == ResultSeverity.ERROR;
            }
        });
    }

    public boolean hasErrors() {
        return !getErrors().isEmpty();
    }

    public Collection<ValidationResult> getErrors() {
        return Collections2.filter(_results, new Predicate<ValidationResult>() {
            @Override
            public boolean apply(ValidationResult result) {
                return result.getSeverity() == ResultSeverity.ERROR;
            }
        });
    }

    public ValidationResults addResult(ValidationResult result) {
        _results.add(result);
        return this;
    }

    public ValidationResults addResults(Iterable<? extends ValidationResult> results) {
        for (final ValidationResult result : results) {
            addResult(result);
        }
        return this;
    }

    public ValidationResults addResults(ValidationResult... results) {
        return addResults(Arrays.asList(results));
    }

    public ValidationResults clear() {
        _results.clear();
        return this;
    }

    @Override
    public String toString() {
        return super.toString() + _results;
    }

    public static ValidationResult newNullErrorResult(String path) {
        return new ValidationResult().type(ResultType.NULL_VALUE).path(path).message("Cannot be null");
    }

    public static ValidationResult newRequiredErrorResult(String path) {
        return new ValidationResult().type(ResultType.REQUIRED_VALUE).path(path).message("Required");
    }

    public static ValidationResult newTypeErrorResult(String path, Object obj, String type) {
        return new ValidationResult().type(ResultType.INCOMPATIBLE_TYPE).path(path).message("Cannot be of type " + type + ": " + obj);
    }
}
