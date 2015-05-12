package com.bazaarvoice.commons.data.model.json.schema.validation.formats;

import java.util.regex.Pattern;

public class HostnameFormatValidator extends AbstractPatternBasedTextFormatValidator {
    private static final Pattern _sHostnamePattern = Pattern.compile("^(([0-9a-zA-Z_\\-]+)\\.)*[0-9a-zA-Z_\\-]+$");

    public HostnameFormatValidator() {
        super("Hostname", _sHostnamePattern);
    }
}
