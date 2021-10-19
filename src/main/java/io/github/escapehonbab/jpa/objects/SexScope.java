package io.github.escapehonbab.jpa.objects;

import lombok.Getter;

import java.io.Serializable;

@Getter
public enum SexScope implements Serializable {
    SCOPE_ALL(0),
    SCOPE_MALE(1),
    SCOPE_FEMALE(2);

    private final int scope;

    SexScope(int scope) {
        this.scope = scope;
    }

    public static SexScope fromScope(int scope) {
        if (scope == SCOPE_ALL.getScope())
            return SCOPE_ALL;
        else if (scope == SCOPE_FEMALE.getScope())
            return SCOPE_FEMALE;
        else return SCOPE_MALE;
    }

    public boolean equals(Sex sex) {
        if (sex.equals(Sex.MALE) && scope == SCOPE_MALE.getScope())
            return true;
        return sex.equals(Sex.FEMALE) && scope == SCOPE_FEMALE.getScope();
    }
}

