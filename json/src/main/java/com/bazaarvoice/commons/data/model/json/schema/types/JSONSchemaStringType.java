package com.bazaarvoice.commons.data.model.json.schema.types;

import com.bazaarvoice.commons.data.model.json.schema.JSONSchema;
import com.bazaarvoice.commons.data.model.json.schema.validation.ResultType;
import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResult;
import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResults;
import com.bazaarvoice.commons.data.model.json.schema.validation.formats.TextFormatValidators;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.util.List;
import java.util.regex.Pattern;

public class JSONSchemaStringType extends AbstractJSONSchemaSimpleType<String, JSONSchemaStringType> {
    private static final TextFormatValidators _sFormatValidators = new TextFormatValidators();

    private Integer _minimumLength;
    private Integer _maximumLength;
    private Pattern _regex;
    private JSONSchemaTextFormat _format;
    private String _customFormat;

    public Integer getMinimumLength() {
        return _minimumLength;
    }

    public void setMinimumLength(Integer minimumLength) {
        _minimumLength = minimumLength;
    }

    public JSONSchemaStringType minimumLength(Integer minimumLength) {
        setMinimumLength(minimumLength);
        return this;
    }

    public Integer getMaximumLength() {
        return _maximumLength;
    }

    public void setMaximumLength(Integer maximumLength) {
        _maximumLength = maximumLength;
    }

    public JSONSchemaStringType maximumLength(Integer maximumLength) {
        setMaximumLength(maximumLength);
        return this;
    }

    public<E extends Enum> JSONSchemaStringType enumValues(E... enumValues) {

        final List<String> values = Lists.newArrayList();

        if (enumValues != null) {
            for (E enumValue: enumValues) {
                values.add(enumValue.name());
            }
        }

        return super.enumValues(values);
    }

    public Pattern getRegex() {
        return _regex;
    }

    public void setRegex(Pattern pattern) {
        _regex = pattern;
    }

    public JSONSchemaStringType regex(Pattern pattern) {
        setRegex(pattern);
        return this;
    }

    public String getPattern() {
        return _regex != null ? _regex.pattern() : null;
    }

    public void setPattern(String pattern) {
        setRegex(pattern != null ? Pattern.compile(pattern) : null);
    }

    public JSONSchemaStringType pattern(String pattern) {
        setPattern(pattern);
        return this;
    }

    public JSONSchemaTextFormat getFormat() {
        return _format;
    }

    public void setFormat(JSONSchemaTextFormat format) {
        _format = format;
    }

    public JSONSchemaStringType format(JSONSchemaTextFormat format) {
        setFormat(format);
        return this;
    }

    public String getCustomFormat() {
        return _customFormat;
    }

    public void setCustomFormat(String customFormat) {
        if (!Strings.isNullOrEmpty(customFormat)) {
            setFormat(JSONSchemaTextFormat.CUSTOM);
        } else {
            setFormat(null);
        }
        _customFormat = customFormat;
    }

    public JSONSchemaStringType customFormat(String customFormat) {
        setCustomFormat(customFormat);
        return this;
    }

    @Override
    public JSONSchemaStringType merge(@Nullable JSONSchemaStringType parentType) {
        if (parentType == null) {
            return this;
        }

        super.merge(parentType);

        if (_minimumLength == null) {
            _minimumLength = parentType._minimumLength;
        }

        if (_maximumLength == null) {
            _maximumLength = parentType._maximumLength;
        }

        if (_regex == null) {
            _regex = parentType._regex;
        }

        if (_format == null) {
            _format = parentType._format;
        }

        if (_customFormat == null) {
            _customFormat = parentType._customFormat;
        }

        return this;
    }

    @Override
    public void validate(JSONSchema schema, Object obj, String path, ValidationResults results) {
        super.validate(schema, obj, path, results);

        String str = (String) obj;

        if (_minimumLength != null && str.length() < _minimumLength) {
            results.addResult(new ValidationResult().type(ResultType.FORMAT_MISMATCH).path(path).message("Minimum length is " + _minimumLength + ": " + str));
        }

        if (_maximumLength != null && str.length() > _maximumLength) {
            results.addResult(new ValidationResult().type(ResultType.FORMAT_MISMATCH).path(path).message("Maximum length is " + _maximumLength + ": " + str));
        }

        if (_regex != null && !_regex.matcher(str).find()) {
            results.addResult(new ValidationResult().type(ResultType.FORMAT_MISMATCH).path(path).message("Text doesn't matter pattern " + getPattern() + ": " + str));
        }

        if (_format != null && _format != JSONSchemaTextFormat.CUSTOM) {
            _sFormatValidators.validate(_format, path, str, results);
        }

        if (_format == JSONSchemaTextFormat.CUSTOM) {
            results.addResult(new ValidationResult().type(ResultType.FORMAT_NOT_VALIDATED).path(path).message("Custom format " + _customFormat + " not validated: " + str));
        }
    }

    @Override
    public String toString() {
        return super.toString() +
                "[minimumLength=" + _minimumLength + "]" +
                "[maximumLength=" + _maximumLength + "]" +
                "[regex=" + _regex + "]" +
                "[format=" + _format + "]" +
                "[customFormat='" + _customFormat + "']";
    }
}
