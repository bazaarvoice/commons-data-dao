package com.bazaarvoice.commons.data.model.json.schema.validation.formats;

import java.util.regex.Pattern;

public class PhoneNumberFormatValitor extends AbstractPatternBasedTextFormatValidator {
    /**
     * Based loosely on http://en.wikipedia.org/wiki/E.123
     * <p/>
     * Verify the number only includes an optional "+" in the beginning, spaces, a optional single set of parentheses, hyphens and/or slashes, and an optional extension.
     */
    private static final Pattern _sPhoneNumberPattern = Pattern.compile("^(\\+\\d+[\\s\\-/])?(\\d+[\\s\\-/])*((\\d+\\s)?\\(\\d+\\)\\s)?(\\d+[\\s\\-/])*\\d+(\\s?x\\s?(\\d+[\\s\\-/])*\\d+)?$");

    public PhoneNumberFormatValitor() {
        super("Phone Number", _sPhoneNumberPattern);
    }
}
