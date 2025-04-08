package com.pack.Laetitia.packManager.enums;

public enum TokenType {

    ACCESS("access_token"), REFRESH("refresh_token");

    private final String value;

    TokenType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean equals(TokenType type, TokenType token) {
        return type != null && token != null && type == token;
    }

}
