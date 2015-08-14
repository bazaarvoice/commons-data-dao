package com.bazaarvoice.commons.data.model.json.schema.validation.formats;

import java.util.regex.Pattern;

public class EmailAddressFormatValidator extends AbstractPatternBasedTextFormatValidator {
    /**
     * Matches typical e-mail addresses:
     * <pre>
     *     aperson@example.com
     *     another.person@an-example.com
     *     a.person+special@mail.example.com
     * </pre>
     */
    private static final Pattern _sEmailAddressPattern = Pattern.compile("^[0-9a-zA-Z_\\-!#\\$%\\&'\\*\\+/=\\?\\^`{|}~\\.]+@(([0-9a-zA-Z_\\-]+)\\.)*[0-9a-zA-Z_\\-]+\\.[a-zA-Z]+$");

    public EmailAddressFormatValidator() {
        super("E-mail Address", _sEmailAddressPattern);
    }
}
