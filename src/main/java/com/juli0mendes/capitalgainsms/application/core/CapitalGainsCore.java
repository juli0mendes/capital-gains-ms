package com.juli0mendes.capitalgainsms.application.core;

import com.juli0mendes.capitalgainsms.application.ports.in.CapitalGainsPortIn;
import com.juli0mendes.capitalgainsms.application.ports.in.OperationDto;
import com.juli0mendes.capitalgainsms.application.ports.out.TaxDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.juli0mendes.capitalgainsms.application.ports.in.OperationType.BUY;

@Service
public class CapitalGainsCore implements CapitalGainsPortIn {

    private static final Logger log = LoggerFactory.getLogger(CapitalGainsCore.class);

    private final static BigDecimal MAX_EXEMPTION_AMOUNT = new BigDecimal(20000);
    private final static BigDecimal PERCENTAGE_TAX_PAID = new BigDecimal(0.2);

    private BigDecimal weightedAveragePrice = new BigDecimal(0.0);

    @Override
    public List<TaxDto> calculateTax(List<OperationDto> operations) {

        log.info("calculate-tax; start; system;");
        log.info("calculate-tax; total-operations=\"{}\";", operations.size());

        var taxesDto = new ArrayList<TaxDto>();
        var currentBalance = new BigDecimal(0.0);

        weightedAveragePrice = this.calculateWeightedAveragePrice(operations);

        for (OperationDto operation : operations) {
            log.info("Operation=\"{}\";", operation.toString());

            if (this.isBuy(operation)) {
                taxesDto.add(TaxDto.create(new BigDecimal(0)));
                continue;
            }

            if (this.isLoss(operation.getUnitCost(), weightedAveragePrice)) {
                taxesDto.add(TaxDto.create(new BigDecimal(0)));
                currentBalance = currentBalance.add(weightedAveragePrice.subtract(operation.getUnitCost()));
                currentBalance = currentBalance.multiply(new BigDecimal(operation.getQuantity())).multiply(new BigDecimal(-1));
                continue;
            }

            BigDecimal totalOperation = this.calculateTotalOperation(operation);

            if (totalOperation.compareTo(MAX_EXEMPTION_AMOUNT) == -1) {
                taxesDto.add(TaxDto.create(new BigDecimal(0)));
                currentBalance.add(totalOperation.subtract(MAX_EXEMPTION_AMOUNT));
                continue;
            }

            var profit = this.calculateProfit(operation, weightedAveragePrice);
            log.info("calculate-tax; profit=\"{}\";", profit);

            currentBalance = profit.add(currentBalance);
            log.info("calculate-tax; current-balance=\"{}\";", currentBalance);

            if (this.currentBalanceIsLess1(currentBalance))
                taxesDto.add(TaxDto.create(new BigDecimal(0.0)));
            else
                taxesDto.add(this.calculateTax(currentBalance));
        }

        log.info("calculate-tax; end; system;");

        return taxesDto;
    }

    private boolean isBuy(OperationDto operation) {
        if (BUY == operation.getType())
            return true;
        return false;
    }

    private BigDecimal calculateWeightedAveragePrice(List<OperationDto> operations) {

        log.info("calculate-weighted-average-price; start; system;");

        var totalValue = new BigDecimal(0.0);
        var quantityExchange = 0;

        var operationsBuy = operations
                .stream()
                .filter(operationDto -> BUY == operationDto.getType())
                .collect(Collectors.toList());

        for (OperationDto operation : operationsBuy) {
            var sumExchange = new BigDecimal(operation.getQuantity()).multiply(operation.getUnitCost());
            totalValue = totalValue.add(sumExchange);
            quantityExchange += operation.getQuantity();
        }

        var weightedAveragePrice = totalValue.divide(new BigDecimal(quantityExchange), 2, RoundingMode.HALF_UP);

        log.info("calculate-weighted-average-price; end; system;");

        return weightedAveragePrice;
    }

    private boolean isLoss(BigDecimal unitCost, BigDecimal weightedAveragePrice) {
        if (unitCost.compareTo(weightedAveragePrice) == -1)
            return true;
        return false;
    }

    private BigDecimal calculateTotalOperation(OperationDto operation) {
        return operation.getUnitCost().multiply(new BigDecimal(operation.getQuantity()));
    }

    private TaxDto calculateTax(BigDecimal profit) {
        var taxValue = profit.multiply(PERCENTAGE_TAX_PAID);
        return TaxDto.create(this.roundValueToTwoDecimalPlaces(taxValue));
    }

    private BigDecimal calculateProfit(OperationDto operation, BigDecimal weightedAveragePrice) {
        log.info("calculate-profit; start; system;");

        var profit = new BigDecimal(0.0);
        profit = profit.add(operation.getUnitCost().subtract(weightedAveragePrice));

        profit = profit.multiply(new BigDecimal(operation.getQuantity()));

        log.info("calculate-profit; end; system;");

        return profit;
    }

    private BigDecimal roundValueToTwoDecimalPlaces(BigDecimal value) {
        return value.setScale(2, RoundingMode.DOWN);
    }

    private boolean currentBalanceIsLess1(BigDecimal currentBalance) {
        return (currentBalance.compareTo(new BigDecimal(0.0)) == -1)
                || (currentBalance.compareTo(new BigDecimal(0.0)) == -0);
    }
}
