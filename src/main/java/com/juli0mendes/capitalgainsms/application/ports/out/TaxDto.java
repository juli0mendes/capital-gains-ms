package com.juli0mendes.capitalgainsms.application.ports.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@JsonNaming(PropertyNamingStrategy.KebabCaseStrategy.class)
public class TaxDto {

    @JsonProperty("tax")
    private BigDecimal value;

    public TaxDto() {
    }

    public BigDecimal getValue() {
        return value;
    }

    public TaxDto setValue(BigDecimal value) {
        this.value = value;
        return this;
    }

    public static TaxDto create(BigDecimal value) {
        return new TaxDto()
                .setValue(value);
    }

    @Override
    public String toString() {
        return "TaxDto{" +
                "value=" + value +
                '}';
    }
}
