package com.juli0mendes.capitalgainsms.application.ports.in;

import com.juli0mendes.capitalgainsms.application.ports.out.TaxDto;

import java.util.List;

public interface CapitalGainsPortIn {

    List<TaxDto> calculateTax(List<OperationDto> operations);
}
