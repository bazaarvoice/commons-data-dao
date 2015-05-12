package com.bazaarvoice.commons.data.model.json.schema.validation.formats;

import java.net.URI;
import java.net.URISyntaxException;

public class URIFormatValidator extends AbstractTextFormatValidator {
    public URIFormatValidator() {
        super("URI");
    }

    @Override
    protected boolean validate(String str) {
        try {
            new URI(str);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
