package com.juli0mendes.capitalgainsms.application.ports.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@JsonNaming(PropertyNamingStrategy.KebabCaseStrategy.class)
public class OperationDto {

    @JsonProperty("operation")
    private OperationType type;

    private BigDecimal unitCost;

    private Integer quantity;

    public OperationDto() {
    }

    public OperationType getType() {
        return type;
    }

    public OperationDto setType(OperationType type) {
        this.type = type;
        return this;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public OperationDto setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public OperationDto setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    @Override
    public String toString() {
        return "OperationDto{" +
                "type=" + type +
                ", unitCost=" + unitCost +
                ", quantity=" + quantity +
                '}';
    }

    public static OperationDto create(OperationType type, BigDecimal unitCost, Integer quantity) {
        return new OperationDto()
                .setType(type)
                .setUnitCost(unitCost)
                .setQuantity(quantity);
    }

}
