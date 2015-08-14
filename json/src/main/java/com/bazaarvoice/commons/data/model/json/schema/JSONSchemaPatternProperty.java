package com.bazaarvoice.commons.data.model.json.schema;

import java.util.regex.Pattern;

public class JSONSchemaPatternProperty extends AbstractJSONSchemaProperty<JSONSchemaPatternProperty> {
    private Pattern _regex;

    public Pattern getRegex() {
        return _regex;
    }

    public void setRegex(Pattern regex) {
        _regex = regex;
    }

    public JSONSchemaPatternProperty regex(Pattern regex) {
        setRegex(regex);
        return this;
    }

    public String getPattern() {
        return _regex.pattern();
    }

    public void setPattern(String pattern) {
        _regex = Pattern.compile(pattern);
    }

    public JSONSchemaPatternProperty pattern(String pattern) {
        setPattern(pattern);
        return this;
    }

    @Override
    public String toString() {
        return super.toString() +
                "[regex='" + _regex + "']";
    }
}
