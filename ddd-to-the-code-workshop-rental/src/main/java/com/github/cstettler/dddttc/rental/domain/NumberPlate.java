package com.github.cstettler.dddttc.rental.domain;

import com.github.cstettler.dddttc.stereotype.ValueObject;

@ValueObject
public class NumberPlate {

    private String value;

    private NumberPlate(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    public static NumberPlate numberPlate(String value) {
        return new NumberPlate(value);
    }

}
