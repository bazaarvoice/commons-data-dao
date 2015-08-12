package com.bazaarvoice.commons.data.model.json.schema.validation.formats;

import java.util.regex.Pattern;

public abstract class AbstractPatternBasedTextFormatValidator extends AbstractTextFormatValidator {
    private final Pattern _pattern;

    protected AbstractPatternBasedTextFormatValidator(String formatName, Pattern pattern) {
        super(formatName);

        _pattern = pattern;
    }

    @Override
    protected boolean validate(String str) {
        return _pattern.matcher(str).matches();
    }
}
