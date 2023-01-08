package com.walletsquire.apiservice.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ActivityUtilities {

    private static BigDecimal getDividedAmount(BigDecimal a, BigDecimal b) {

        int debug = 0;

        if (debug == 1) {
            System.out.println("getDividedAmount start");
        }

        BigDecimal dividedAmount = a.divide(b, 2, RoundingMode.UP);
        if (debug == 1) {
            System.out.println("getDividedAmount : after division : " + dividedAmount);
        }

        dividedAmount = dividedAmount.setScale(2, RoundingMode.UP);
        if (debug == 1) {
            System.out.println("getDividedAmount : set scale      : " + dividedAmount);
        }

        if (debug == 1) {
            System.out.println("getDividedAmount end : " + dividedAmount);
        }
        return dividedAmount;
    }

    public static boolean hasExtraPenny(BigDecimal a, BigDecimal b) {

        int debug = 0;

        if (debug == 1) {
            System.out.println("hasExtraPenny start");
        }


        BigDecimal dividedAmount = getDividedAmount(a, b);

        if (dividedAmount.multiply(b).compareTo(a) != 0) {
            if (debug == 1) {
                System.out.println("has extra penny");
            }
            return true;
        }

        if (debug == 1) {
            System.out.println("no extra penny");
        }
        return false;
    }

    public static BigDecimal calculatedSplitAmount(BigDecimal a, BigDecimal b) {

        int debug = 0;

        if (debug == 1) {
            System.out.println("calculateSplitAmount start");
        }

        BigDecimal dividedAmount = getDividedAmount(a, b);

        if (dividedAmount.multiply(b).compareTo(a) != 0) {
            if (debug == 1) {
                System.out.println("calculateSplitAmount (1) : " + dividedAmount.subtract(BigDecimal.valueOf(.01)));
            }
            return dividedAmount.subtract(BigDecimal.valueOf(.01));
        }

        if (debug == 1) {
            System.out.println("calculateSplitAmount (0) : " + dividedAmount);
        }
        return dividedAmount;
    }

}
