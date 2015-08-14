package com.bazaarvoice.commons.data.model.json.schema.validation.formats;

import com.bazaarvoice.commons.data.model.json.schema.types.JSONSchemaTextFormat;
import com.bazaarvoice.commons.data.model.json.schema.validation.ValidationResults;
import com.google.common.collect.Maps;

import java.util.Map;

public class TextFormatValidators {
    private Map<JSONSchemaTextFormat, TextFormatValidator> _validators = Maps.newEnumMap(JSONSchemaTextFormat.class);

    public TextFormatValidators() {
        _validators.put(JSONSchemaTextFormat.DATE_TIME, new DateTimeFormatValidator("Date/Time", "yyyy-MM-dd'T'HH:mm:ssZ"));
        _validators.put(JSONSchemaTextFormat.DATE, new DateTimeFormatValidator("Date", "yyyy-MM-dd"));
        _validators.put(JSONSchemaTextFormat.TIME, new DateTimeFormatValidator("Time", "HH:mm:ss"));
        _validators.put(JSONSchemaTextFormat.UTC_MILLISECONDS, new NumberFormatValidator("UTC milliseconds"));

        _validators.put(JSONSchemaTextFormat.REGEX, new RegExFormatValidator());

        _validators.put(JSONSchemaTextFormat.CSS_COLOR, new CSSColorFormatValidator());
        _validators.put(JSONSchemaTextFormat.CSS_STYLE, new CSSStyleFormatValidator());

        _validators.put(JSONSchemaTextFormat.PHONE_NUMBER, new PhoneNumberFormatValitor());
        _validators.put(JSONSchemaTextFormat.URI, new URIFormatValidator());
        _validators.put(JSONSchemaTextFormat.EMAIL_ADDRESS, new EmailAddressFormatValidator());
        _validators.put(JSONSchemaTextFormat.IPV4_ADDRESS, new IP4AddressFormatValidator());
        _validators.put(JSONSchemaTextFormat.IPV6_ADDRESS, new IP6AddressFormatValidator());
        _validators.put(JSONSchemaTextFormat.HOSTNAME, new HostnameFormatValidator());
    }

    public TextFormatValidator registerValidator(JSONSchemaTextFormat format, TextFormatValidator validator) {
        if (format == null) {
            throw new IllegalArgumentException("Format cannot be null");
        }
        if (validator == null) {
            throw new IllegalArgumentException("Validator cannot be null");
        }
        return _validators.put(format, validator);
    }

    public void validate(JSONSchemaTextFormat format, String path, String str, ValidationResults results) {
        getFormatValidator(format).validate(path, str, results);
    }

    private TextFormatValidator getFormatValidator(JSONSchemaTextFormat format) {
        TextFormatValidator formatValidator = _validators.get(format);
        if (formatValidator == null) {
            throw new IllegalArgumentException("Unknown format: " + format);
        }
        return formatValidator;
    }
}
