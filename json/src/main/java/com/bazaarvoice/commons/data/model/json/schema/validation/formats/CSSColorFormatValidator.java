package com.bazaarvoice.commons.data.model.json.schema.validation.formats;

import com.google.common.collect.Sets;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * Accepted values:
 * <pre>
 *     // Hex
 *     #aB3
 *     #aaBB33
 *
 *     // RGB
 *     rgb(255,0,0)
 *     rgb(100%,0,0)
 *
 *     // Standard CSS2 colors
 *     black
 *     white
 *     orange
 *     transparent
 * </pre>
 */
public class CSSColorFormatValidator extends AbstractPatternBasedTextFormatValidator {
    /**
     * Accepted Formats:
     * <pre>
     *     // Hex
     *     #aB3
     *     #aaBB33
     *
     *     // RGB
     *     rgb(255,0,0)
     *     rgb(100%,0,0)
     * </pre>
     */
    private static final Pattern _sCSSColorPattern = Pattern.compile("^#([0-9a-fA-F]{3}|([0-9a-fA-F]{6}))|rgb\\(\\d{1,3}%?,\\d{1,3}%?,\\d{1,3}%?\\)$");

    /**
     * Known color keywords
     */
    private static final Set<String> _sColorKeywords = Sets.newHashSet(
            "aqua", "black", "blue", "fuchsia", "gray", "green", "lime",
            "maroon", "navy", "olive", "orange", "purple", "red", "silver",
            "teal", "white", "yellow",
            "transparent");

    public CSSColorFormatValidator() {
        super("CSS Color", _sCSSColorPattern);
    }

    @Override
    protected boolean validate(String str) {
        return _sColorKeywords.contains(str) || super.validate(str);
    }
}
