package com.bazaarvoice.commons.data.model;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class AuthTokenTest {
    private static final String USER_NAME = "localAdmin";
    private static final int TIMEOUT = 4096;
    private static final String AUTH = "BUNCHOFRANDOMTHINGS";

    public void testMimick() {
        AuthToken token = new AuthToken(USER_NAME, TIMEOUT, AUTH);
        String text = AuthToken.AuthTokenTokenizer.toString(token);
        AuthToken reconstructed = AuthToken.AuthTokenTokenizer.toToken(text);

        Assert.assertEquals(reconstructed.getUsername(), token.getUsername());
        Assert.assertEquals(reconstructed.getTimeout(), token.getTimeout());
        Assert.assertEquals(reconstructed.getAuth(), token.getAuth());
    }

    public void testNullToToken() {
        Assert.assertNull(AuthToken.AuthTokenTokenizer.toToken(null));
    }

    public void testNotParseableToNull() {
        Assert.assertNull(AuthToken.AuthTokenTokenizer.toToken(""));
    }

    public void testNumberFormatExceptionToNull() {
        Assert.assertNull(AuthToken.AuthTokenTokenizer.toToken(USER_NAME + ":" + "BAD_TIMEOUT_VAL_4096" + ":" + AUTH));
    }
}