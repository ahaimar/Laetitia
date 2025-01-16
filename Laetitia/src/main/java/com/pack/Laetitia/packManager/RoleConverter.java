package com.pack.Laetitia.packManager;

import com.pack.Laetitia.packManager.enums.Authority;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Authority, String> {

            // This class is a JPA Attribute Converter that maps between.

    @Override
    public String convertToDatabaseColumn(Authority authority) {

        if (authority == null) {
            return null;
        }
        return authority.getValue();
    }

    @Override
    public Authority convertToEntityAttribute(String code) {

        if (code == null) {
            return null;
        }
        return Stream.of(Authority.values())
                .filter(authority -> authority.getValue().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("\nUnknown authority: " + code + "\n")
                );
    }
}
