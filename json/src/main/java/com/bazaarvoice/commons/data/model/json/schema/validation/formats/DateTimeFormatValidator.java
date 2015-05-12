package com.bazaarvoice.commons.data.model.json.schema.validation.formats;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateTimeFormatValidator extends AbstractTextFormatValidator {
    private final String _dateFormat;

    public DateTimeFormatValidator(String formatName, String dateFormat) {
        super(formatName);
        _dateFormat = dateFormat;
    }

    @Override
    public boolean validate(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(_dateFormat);
        simpleDateFormat.setLenient(false);

        try {
            simpleDateFormat.parse(str);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
