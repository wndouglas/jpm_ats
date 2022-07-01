package org.wd.rfq.model;

import org.wd.rfq.util.StandardNormalGenerator;

public class VasicekModel extends StochasticModel {
    private long currentInternalTimestamp;
    private double currentPrice;
    private final double annualisedVol;
    private final double longTermMean;
    private final double meanReversionSpeed;

    public VasicekModel(long initialTimeStamp, double initialPrice, double annualisedVol, double longTermMean,
                        double meanReversionSpeed, StandardNormalGenerator stdNormalGenerator) {
        super(stdNormalGenerator);
        this.currentInternalTimestamp = initialTimeStamp;
        this.currentPrice = initialPrice;
        this.annualisedVol = annualisedVol;
        this.longTermMean = longTermMean;
        this.meanReversionSpeed = meanReversionSpeed;
    }


    @Override
    public double evolvePriceUntil(long timeStamp) {
        double previousInternalTimestamp = this.currentInternalTimestamp;

        double brownianIncrement = evolveRandomGenerator(timeStamp);
        double diffusionTerm = brownianIncrement*annualisedVol;

        long timeDelta = getTimeDelta(timeStamp);
        double driftTerm = meanReversionSpeed*(longTermMean - currentPrice)*getYearFraction(timeDelta);

        double newPrice = driftTerm + diffusionTerm;

        currentInternalTimestamp = timeStamp;
        currentPrice = newPrice;
        return newPrice;
    }

    private long getTimeDelta(long timeStamp) {
        return timeStamp - this.currentInternalTimestamp;
    }

    /**
     * Timestamps are in milliseconds, so we must convert
     * our millisecond time-deltas into year fractions.
     */
    private double getYearFraction(long timeDelta) {
        double numSeconds = timeDelta/1000.0;
        double numMinutes = numSeconds/60.0;
        double numHours = numMinutes/60.0;
        double numDays = numHours/24.0;
        return numDays/252.0;
    }

    /**
     * In the case of the Vasicek model our random generator is a standard GBM, so we must multiply the
     * standard normal by the sqrt of the time-delta.
     */
    @Override
    public double evolveRandomGenerator(long timeStamp) {
        double yearFraction = getYearFraction(getTimeDelta(timeStamp));
        return this.randomGenerator.getNextValue()*Math.sqrt(yearFraction);
    }

    @Override
    public String toString() {
        return "VasicekModel[" +
                    "currentInternalTimestamp: " + this.currentInternalTimestamp +
                    " currentPrice: " + this.currentPrice +
                    " annualisedVol: " + this.annualisedVol +
                    " longTermMean: " + this.longTermMean +
                    " meanReversionSpeed: " + this.meanReversionSpeed
                    + "]";
    }
}
