package com.bazaarvoice.commons.data.model.json.schema.validation.formats;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegExFormatValidator extends AbstractTextFormatValidator {
    public RegExFormatValidator() {
        super("Reg-Ex");
    }

    @Override
    protected boolean validate(String str) {
        try {
            Pattern.compile(str);
            return true;
        } catch (PatternSyntaxException e) {
            return false;
        }
    }
}
