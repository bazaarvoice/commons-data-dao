package com.bazaarvoice.commons.data.model.json.schema.validation.formats;

public class IP4AddressFormatValidator extends AbstractTextFormatValidator {
    public IP4AddressFormatValidator() {
        super("IPv4");
    }

    @Override
    public boolean validate(String str) {
        String[] parts = str.split("\\.");
        if (parts.length != 4) {
            return false;
        }

        for (final String part : parts) {
            int partNumber;
            try {
                partNumber = Integer.parseInt(part);
            } catch (NumberFormatException e) {
                return false;
            }

            if (partNumber < 0 || partNumber > 0xff) {
                return false;
            }
        }

        return true;
    }
}
