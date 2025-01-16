package com.pack.Laetitia.packManager.util;

public class EmailUtils {

    public static String getEmailMessage(String name, String host, String token){

        return "Hello, " + name + "\n\n Your new account has been created"+ getVerifictionURL(host, token);
    }

    public static String getResetPasswordMessage(String name, String host, String token){

        return "Hello, " + name + "\n\nYou requested to reset your password." +
                " Click the link below to complete the process:\n" + getResetPasswordURL(host, token);
    }

    public static String getVerifictionURL(String host, String token){

        return "http://" + host + "/verify/account?token=" + token;
    }

    public static String getResetPasswordURL(String host, String token){

        return "http://" + host + "/verify/password?token=" + token;
    }
}
