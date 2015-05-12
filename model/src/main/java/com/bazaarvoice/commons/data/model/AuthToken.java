package com.bazaarvoice.commons.data.model;

import java.io.Serializable;

public class AuthToken implements Serializable {
    private String _username;
    private long _timeout;
    private String _auth;

    public AuthToken(String username, long timeout, String auth) {
        _username = username;
        _timeout = timeout;
        _auth = auth;
    }

    /* For GWT Serialization */
    private AuthToken() {
    }

    public String getUsername() {
        return _username;
    }

    public long getTimeout() {
        return _timeout;
    }

    public String getAuth() {
        return _auth;
    }

    public static class AuthTokenTokenizer {
        public static String toString(AuthToken auth) {
            return auth.getUsername() + ":" + auth.getTimeout() + ":" + auth.getAuth();
        }

        public static AuthToken toToken(String auth) {
            if (auth == null) {
                return null;
            }

            String[] s = auth.split(":");
            if (s.length != 3) {
                return null;
            }

            try {
                return new AuthToken(s[0], Long.parseLong(s[1]), s[2]);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }
}