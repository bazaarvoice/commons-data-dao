package com.bazaarvoice.commons.data.model.json.schema.validation.formats;

public class IP6AddressFormatValidator extends AbstractTextFormatValidator {
    public IP6AddressFormatValidator() {
        super("IPv6");
    }

    @Override
    protected boolean validate(String str) {
        // IPv6 is in the format 0123:4567:89ab:cdef:0123:4567:89ab:cdef or 0123::cdef:0123:4567:89ab:cdef
        String[] parts = str.split(":");
        boolean hasConsecutiveColons = false;
        for (final String part : parts) {
            if (part.length() > 0) {
                int partNumber;
                try {
                    partNumber = Integer.parseInt(part, 16);
                } catch (NumberFormatException e) {
                    return false;
                }

                if (partNumber < 0 || partNumber > 0xffff) {
                    return false;
                }
            } else {
                // two consecutive colons collapse many consecutive zeroes, eg 1:0:0:1 into 1::1
                if (hasConsecutiveColons) {
                    // only one set of consecutive colons are allowed
                    return false;
                }
                hasConsecutiveColons = true;
            }
        }

        return hasConsecutiveColons ? parts.length < 8 : parts.length == 8;
    }
}
