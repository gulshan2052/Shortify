package com.robotlab.shortify.utils;

import io.seruco.encoding.base62.Base62;

import java.net.URI;
import java.security.SecureRandom;

public class UrlUtils {

    public static Boolean isUrlValid(String url) {
        try {
            URI uri = new URI(url);
            return uri.getScheme() != null && uri.getHost() != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isAlphaNumeric(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        for (char c : text.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static String encodeBase62(String s) {
        Base62 base62 = Base62.createInstance();
        final byte[] encoded = base62.encode(s.getBytes());
        return new String(encoded);
    }

}
