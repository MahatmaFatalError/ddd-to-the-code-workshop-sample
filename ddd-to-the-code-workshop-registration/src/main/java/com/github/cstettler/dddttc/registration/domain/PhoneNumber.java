package com.github.cstettler.dddttc.registration.domain;

import com.github.cstettler.dddttc.stereotype.ValueObject;

@ValueObject
public class PhoneNumber {

    private final String value;

    private PhoneNumber(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    public static PhoneNumber phoneNumber(String value) {
        return new PhoneNumber(value);
    }

}
