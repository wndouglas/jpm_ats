package org.wd.rfq.logic;

public class SpreadCalculator {
    private final static double MIN_SPREAD_BPS = 1;
    private final static double MAX_SPREAD_BPS = 10;
    private final long maxNotional;

    public SpreadCalculator(long maxNotional)  {
        this.maxNotional = maxNotional;
    }

    /**
     * We apply a quadratic function to calculate the spread on the swap rate, starting at MIN_SPREAD_BPS and ending at
     * MAX_SPREAD_BPS. This is an attempt at a reasonable functional form for the bid-ask spread applied to an RFQ.
     */
    public double getSpread(long notional) {
        double percentageSpread = 1.0 - Math.pow(maxNotional - notional, 2)/Math.pow(maxNotional, 2);
        return (percentageSpread*MAX_SPREAD_BPS + (1-percentageSpread)*MIN_SPREAD_BPS)/1e4;
    }

    @Override
    public String toString() {
        return "SpreadCalculator[" +
                "maxNotional: " + this.maxNotional +
                "]";
    }
}
