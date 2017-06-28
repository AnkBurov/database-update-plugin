package io.ankburov.gradle.dbupdate.impl

class StringUtils {

    static boolean isNotBlank(CharSequence charSequence) {
        return !isBlank(charSequence)
    }

    static boolean isBlank(CharSequence charSequence) {
        int strLen
        if (charSequence == null || (strLen = charSequence.length()) == 0) {
            return true
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(charSequence.charAt(i))) {
                return false;
            }
        }
        return true
    }
}
