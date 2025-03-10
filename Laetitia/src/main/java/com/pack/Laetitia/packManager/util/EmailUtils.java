package com.pack.Laetitia.packManager.util;

public class EmailUtils {

    public static String getEmailMessage(String name, String host, String key){

        return "Hello, " + name + "\n\n Your new account has been created"+ getVerificationURL(host, key);
    }

    public static String getResetPasswordMessage(String name, String host, String key){

        return "Hello, " + name + "\n\nYou requested to reset your password." +
                " Click the link below to complete the process:\n" + getResetPasswordURL(host, key);
    }

    public static String getVerificationURL(String host, String key){

        return "http://" + host + "/verify/account?key=" + key;
    }

    public static String getResetPasswordURL(String host, String key){

        return "http://" + host + "/verify/password?ky=key=" + key;
    }
}
