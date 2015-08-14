package com.bazaarvoice.commons.data.model.json.schema.validation.formats;

import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResults;

public interface TextFormatValidator {

    void validate(String path, String str, ValidationResults results);

}
