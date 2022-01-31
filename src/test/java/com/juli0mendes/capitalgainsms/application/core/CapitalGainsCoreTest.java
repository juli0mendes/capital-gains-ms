package com.juli0mendes.capitalgainsms.application.core;

import com.juli0mendes.capitalgainsms.application.ports.in.OperationDto;
import com.juli0mendes.capitalgainsms.application.ports.out.TaxDto;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import static com.juli0mendes.capitalgainsms.application.ports.in.OperationType.BUY;
import static com.juli0mendes.capitalgainsms.application.ports.in.OperationType.SELL;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@DisplayName("Core - Capital Gains")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CapitalGainsCoreTest {

    @Autowired
    private CapitalGainsCore capitalGainsCore;

    @Test
    @DisplayName("1 - Should return zeroed rates for purchases and sales with total value less than 20000")
    public void shouldReturnZeroedRatesForPurchasesAndSalesWithTotalValueLessThan20000() {

        var operations = Arrays.asList(
                OperationDto.create(BUY, new BigDecimal(15), 100),
                OperationDto.create(SELL, new BigDecimal(15), 50),
                OperationDto.create(BUY, new BigDecimal(15), 50)
        );

        var taxesDto = this.capitalGainsCore.calculateTax(operations);

        for (TaxDto taxDto : taxesDto) {
            assertEquals(new BigDecimal(0), taxDto.getValue());
        }
    }

    @Test
    @DisplayName("2 - Should return zero for purchases and charge profit tax and not charge loss tax")
    public void shouldReturnZeroForPurchasesAndChargeProfitTaxAndNotChargeLossTax() {

        var operations = Arrays.asList(
                OperationDto.create(BUY, new BigDecimal(10), 10000),
                OperationDto.create(SELL, new BigDecimal(20), 5000),
                OperationDto.create(SELL, new BigDecimal(5), 5000)
        );

        var taxesDto = this.capitalGainsCore.calculateTax(operations);

        assertEquals(new BigDecimal(0), taxesDto.get(0).getValue());
        assertEquals(new BigDecimal(10000.00).setScale(2, RoundingMode.DOWN), taxesDto.get(1).getValue());
        assertEquals(new BigDecimal(0), taxesDto.get(2).getValue());
    }

    @Test
    @DisplayName("3 - Should return zero for purchases and not charge loss tax and charge profit tax minus loss")
    public void shouldReturnZeroForPurchasesAndNotChargeLossTaxAndChargeProfitTaxMinusLoss() {

        var operations = Arrays.asList(
                OperationDto.create(BUY, new BigDecimal(10), 10000),
                OperationDto.create(SELL, new BigDecimal(5), 5000),
                OperationDto.create(SELL, new BigDecimal(20), 5000)
        );

        var taxesDto = this.capitalGainsCore.calculateTax(operations);

        assertEquals(new BigDecimal(0), taxesDto.get(0).getValue());
        assertEquals(new BigDecimal(0), taxesDto.get(1).getValue());
        assertEquals(new BigDecimal(5000.00).setScale(2, RoundingMode.DOWN), taxesDto.get(2).getValue());
    }

    @Test
    @DisplayName("4- Should return zero for purchases and not charge when the unit price is less than the weighted average price")
    public void shouldReturnZeroForPurchasesSndNotChargeWhenTheUnitPriceIsLessThanTheWeightedAveragePrice() {

        var operations = Arrays.asList(
                OperationDto.create(BUY, new BigDecimal(10), 10000),
                OperationDto.create(BUY, new BigDecimal(25), 5000),
                OperationDto.create(SELL, new BigDecimal(15), 10000)
        );

        var taxesDto = this.capitalGainsCore.calculateTax(operations);

        for (TaxDto taxDto : taxesDto) {
            assertEquals(new BigDecimal(0), taxDto.getValue());
        }
    }

    @Test
    @DisplayName("5 - Should return zero for purchases and not charge when the unit price is less than the weighted average price and charge tax for profit")
    public void shouldReturnZeroForPurchasesAndNotChargeWhenTheUnitPriceIsLessThanTheWeightedAveragePriceAndchargeTaxForProfit() {

        var operations = Arrays.asList(
                OperationDto.create(BUY, new BigDecimal(10), 10000),
                OperationDto.create(BUY, new BigDecimal(25), 5000),
                OperationDto.create(SELL, new BigDecimal(15), 10000),
                OperationDto.create(SELL, new BigDecimal(25), 5000)
        );

        var taxesDto = this.capitalGainsCore.calculateTax(operations);

        assertEquals(new BigDecimal(0), taxesDto.get(0).getValue());
        assertEquals(new BigDecimal(0), taxesDto.get(1).getValue());
        assertEquals(new BigDecimal(0), taxesDto.get(2).getValue());
        assertEquals(new BigDecimal(10000.00).setScale(2, RoundingMode.DOWN), taxesDto.get(3).getValue());
    }

    @Test
    @DisplayName("6 - Should return zero for purchases and not charge when there was a loss and charge when there was a profit deducting the loss")
    public void shouldReturnZeroForPurchasesAndNotChargeWhenThereWasALossAndChargeWhenThereWasAProfitDeductingTheLoss() {

        var operations = Arrays.asList(
                OperationDto.create(BUY, new BigDecimal(10), 10000),
                OperationDto.create(SELL, new BigDecimal(2), 5000),
                OperationDto.create(SELL, new BigDecimal(20), 2000),
                OperationDto.create(SELL, new BigDecimal(20), 2000),
                OperationDto.create(SELL, new BigDecimal(25), 1000)
        );

        var taxesDto = this.capitalGainsCore.calculateTax(operations);

        assertEquals(new BigDecimal(0), taxesDto.get(0).getValue());
        assertEquals(new BigDecimal(0), taxesDto.get(1).getValue());
        assertEquals(new BigDecimal(0), taxesDto.get(2).getValue());
        assertEquals(new BigDecimal(0), taxesDto.get(3).getValue());
        assertEquals(new BigDecimal(3000.00).setScale(2, RoundingMode.DOWN), taxesDto.get(4).getValue());
    }

}