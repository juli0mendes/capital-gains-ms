package com.juli0mendes.capitalgainsms.application.ports.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@JsonNaming(PropertyNamingStrategy.KebabCaseStrategy.class)
public class LineDto {

    private List<OperationDto> operations;

    public LineDto() {
    }

    public List<OperationDto> getOperations() {
        return operations;
    }

    public LineDto setOperations(List<OperationDto> operations) {
        this.operations = operations;
        return this;
    }

    public static LineDto create(List<OperationDto> operations) {
        return new LineDto()
                .setOperations(operations);
    }
}
