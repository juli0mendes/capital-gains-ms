package com.juli0mendes.capitalgainsms.application.ports.in;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OperationType {

    BUY("buy"),
    SELL("sell");

    final String value;

    OperationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }
}
