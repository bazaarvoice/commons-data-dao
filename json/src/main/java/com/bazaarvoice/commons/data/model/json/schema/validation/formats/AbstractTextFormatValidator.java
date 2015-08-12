package com.bazaarvoice.commons.data.model.json.schema.validation.formats;

import com.bazaarvoice.commons.data.model.json.schema.validation.ResultType;
import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResult;
import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResults;

public abstract class AbstractTextFormatValidator implements TextFormatValidator {

    private final String _formatName;

    protected AbstractTextFormatValidator(String formatName) {
        _formatName = formatName;
    }

    protected String getFormatName() {
        return _formatName;
    }

    @Override
    public void validate(String path, String str, ValidationResults results) {
        if (!validate(str)) {
            addFormatMismatchResult(path, str, results);
        }
    }

    protected abstract boolean validate(String str);

    protected void addFormatMismatchResult(String path, String str, ValidationResults results) {
        results.addResult(new ValidationResult().type(ResultType.FORMAT_MISMATCH).path(path).message("Text not in " + _formatName + " format: " + str));
    }

}
