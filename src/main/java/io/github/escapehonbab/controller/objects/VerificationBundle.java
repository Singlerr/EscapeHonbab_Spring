package io.github.escapehonbab.controller.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationBundle {
    public String phoneNumber;
    public String code;

    public VerificationBundle() {
    }
}
