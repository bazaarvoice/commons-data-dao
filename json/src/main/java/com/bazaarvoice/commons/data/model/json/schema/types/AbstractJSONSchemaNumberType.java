package com.bazaarvoice.commons.data.model.json.schema.types;

import com.bazaarvoice.commons.data.model.json.schema.JSONSchema;
import com.bazaarvoice.commons.data.model.json.schema.validation.ResultType;
import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResult;
import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResults;

import javax.annotation.Nullable;
import java.util.Comparator;

public abstract class AbstractJSONSchemaNumberType<V extends Number, S extends AbstractJSONSchemaNumberType<V,S>> extends AbstractJSONSchemaSimpleType<V,S> {
    private V _minimum;
    private Boolean _minimumExclusive;
    private V _maximum;
    private Boolean _maximumExclusive;
    private V _divisibleBy;

    public V getMinimum() {
        return _minimum;
    }

    public void setMinimum(V minimum) {
        _minimum = minimum;
    }

    public S minimum(V minimum) {
        setMinimum(minimum);

        //noinspection unchecked
        return (S) this;
    }

    public boolean isMinimumExclusive() {
        return _minimumExclusive == Boolean.TRUE;
    }

    public Boolean getMinimumExclusive() {
        return _minimumExclusive;
    }

    public void setMinimumExclusive(Boolean minimumExclusive) {
        _minimumExclusive = minimumExclusive;
    }

    public S minimumExclusive() {
        return minimumExclusive(true);
    }

    public S minimumInclusive() {
        return minimumExclusive(false);
    }

    public S minimumExclusive(Boolean minimumExclusive) {
        setMinimumExclusive(minimumExclusive);

        //noinspection unchecked
        return (S) this;
    }

    public V getMaximum() {
        return _maximum;
    }

    public void setMaximum(V maximum) {
        _maximum = maximum;
    }

    public S maximum(V maximum) {
        setMaximum(maximum);

        //noinspection unchecked
        return (S) this;
    }

    public boolean isMaximumExclusive() {
        return _maximumExclusive == Boolean.TRUE;
    }

    public Boolean getMaximumExclusive() {
        return _maximumExclusive;
    }

    public void setMaximumExclusive(Boolean maximumExclusive) {
        _maximumExclusive = maximumExclusive;
    }

    public S maximumExclusive() {
        return maximumExclusive(true);
    }

    public S maximumInclusive() {
        return maximumExclusive(false);
    }

    public S maximumExclusive(Boolean maximumExclusive) {
        setMaximumExclusive(maximumExclusive);

        //noinspection unchecked
        return (S) this;
    }

    public V getDivisibleBy() {
        return _divisibleBy;
    }

    public void setDivisibleBy(V divisibleBy) {
        if (divisibleBy != null && divisibleBy.doubleValue() == 0) {
            throw new IllegalArgumentException("Divisible-By value cannot be 0");
        }

        _divisibleBy = divisibleBy;
    }

    public S divisibleBy(V divisibleBy) {
        setDivisibleBy(divisibleBy);

        //noinspection unchecked
        return (S) this;
    }

    @Override
    public S merge(@Nullable S parentType) {
        if (parentType == null) {
            //noinspection unchecked
            return (S) this;
        }

        super.merge(parentType);

        if (_minimum == null) {
            _minimum = parentType.getMinimum();
        }

        if (_minimumExclusive == null) {
            _minimumExclusive = parentType.getMinimumExclusive();
        }

        if (_maximum == null) {
            _maximum = parentType.getMaximum();
        }

        if (_maximumExclusive == null) {
            _maximumExclusive = parentType.getMaximumExclusive();
        }

        if (_divisibleBy == null) {
            _divisibleBy = parentType.getDivisibleBy();
        }

        //noinspection unchecked
        return (S) this;
    }

    @Override
    public void validate(JSONSchema schema, Object obj, String path, ValidationResults results) {
        super.validate(schema, obj, path, results);

        Number number = (Number) obj;
        if (_minimum != null) {
            int result = NumberComparator.INSTANCE.compare(number, _minimum);
            if (isMinimumExclusive()) {
                if (!(result > 0)) {
                    results.addResult(new ValidationResult().type(ResultType.CONSTRAINT_VIOLATION).path(path).message("Value must be > " + _minimum + ": " + number));
                }
            } else {
                if (!(result >= 0)) {
                    results.addResult(new ValidationResult().type(ResultType.CONSTRAINT_VIOLATION).path(path).message("Value must be >= " + _minimum + ": " + number));
                }
            }
        }

        if (_maximum != null) {
            int result = NumberComparator.INSTANCE.compare(number, _maximum);
            if (isMaximumExclusive()) {
                if (!(result < 0)) {
                    results.addResult(new ValidationResult().type(ResultType.CONSTRAINT_VIOLATION).path(path).message("Value must be < " + _maximum + ": " + number));
                }
            } else {
                if (!(result <= 0)) {
                    results.addResult(new ValidationResult().type(ResultType.CONSTRAINT_VIOLATION).path(path).message("Value must be <= " + _maximum + ": " + number));
                }
            }
        }

        if (_divisibleBy != null) {
            if (_divisibleBy instanceof Integer && number instanceof Integer) {
                int divInt = _divisibleBy.intValue();
                int valueInt = number.intValue();
                if (valueInt % divInt != 0) {
                    results.addResult(new ValidationResult().type(ResultType.CONSTRAINT_VIOLATION).path(path).message("Value must be divisible by " + _divisibleBy + ": " + number));
                }
            } else {
                double divDouble = _divisibleBy.doubleValue();
                double valueDouble = number.doubleValue();
                double result = valueDouble / divDouble;
                if (Math.floor(result) != result) {
                    results.addResult(new ValidationResult().type(ResultType.CONSTRAINT_VIOLATION).path(path).message("Value must be divisible by " + _divisibleBy + ": " + number));
                }
            }
        }
    }

    @Override
    public String toString() {
        return super.toString() +
                "[minimum=" + _minimum + "]" +
                "[isMinimumExclusive=" + _minimumExclusive + "]" +
                "[maximum=" + _maximum + "]" +
                "[isMaximumExclusive=" + _maximumExclusive + "]" +
                "[divisibleBy=" + _divisibleBy + "]";
    }

    private static class NumberComparator implements Comparator<Number> {
        public static final NumberComparator INSTANCE = new NumberComparator();

        @Override
        public int compare(Number o1, Number o2) {
            if (o1 instanceof Integer && o2 instanceof Integer) {
                int i1 = o1.intValue();
                int i2 = o2.intValue();
                if (i1 < i2) {
                    return -1;
                } else if (i1 > i2) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                double i1 = o1.doubleValue();
                double i2 = o2.doubleValue();
                if (i1 < i2) {
                    return -1;
                } else if (i1 > i2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }
}
