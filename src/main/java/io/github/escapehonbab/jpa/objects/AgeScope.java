package io.github.escapehonbab.jpa.objects;

import lombok.Getter;

import java.io.Serializable;

@Getter
public enum AgeScope implements Serializable {
    SCOPE_RANDOM(0),
    SCOPE_20_25(1),
    SCOPE_26_30(2),
    SCOPE_31(3);
    private final int scope;

    AgeScope(int scope) {
        this.scope = scope;
    }

    public static AgeScope fromScope(int scope) {
        if (scope == SCOPE_RANDOM.getScope())
            return SCOPE_RANDOM;
        else if (scope == SCOPE_20_25.getScope())
            return SCOPE_20_25;
        else if (scope == SCOPE_26_30.getScope())
            return SCOPE_26_30;
        else return SCOPE_31;
    }

}
