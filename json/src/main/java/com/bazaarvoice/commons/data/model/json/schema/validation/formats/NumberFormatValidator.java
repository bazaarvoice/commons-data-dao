package com.bazaarvoice.commons.data.model.json.schema.validation.formats;

import java.util.regex.Pattern;

public class NumberFormatValidator extends AbstractTextFormatValidator {
    private static final Pattern _sFloatIndicatorPattern = Pattern.compile(".*[\\.eE].*");

    public NumberFormatValidator(String formatName) {
        super(formatName);
    }

    @Override
    protected boolean validate(String str) {
        return _sFloatIndicatorPattern.matcher(str).matches() ? validateFloat(str) : validateInteger(str);
    }

    private boolean validateFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean validateInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
