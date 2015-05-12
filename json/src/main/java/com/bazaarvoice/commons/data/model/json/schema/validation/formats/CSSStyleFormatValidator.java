package com.bazaarvoice.commons.data.model.json.schema.validation.formats;

import java.util.regex.Pattern;

public class CSSStyleFormatValidator extends AbstractPatternBasedTextFormatValidator {
    /**
     * Example: <code>"color: red; background-color:#FFF"</code>
     */
    private static final Pattern _sCSSStylePattern = Pattern.compile("^(\\s*[a-zA-Z_\\-0-9]+\\s*:(\\s*[^\\s;]+)+;)*(\\s*[a-zA-Z_\\-0-9]+\\s*:(\\s*[^\\s;]+)+;?)$");

    public CSSStyleFormatValidator() {
        super("CSS Style", _sCSSStylePattern);
    }
}
