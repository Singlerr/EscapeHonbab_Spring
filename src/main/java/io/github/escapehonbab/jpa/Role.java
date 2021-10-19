package io.github.escapehonbab.jpa;

import lombok.Getter;

@Getter
public enum Role {
    USER("USER"),
    ADMIN("ADMIN");

    String value;

    Role(String value) {
        this.value = value;
    }
}
