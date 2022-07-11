package org.wd.rfq.model;

import org.wd.rfq.simulation.StandardNormalGenerator;

class VasicekModel extends StochasticModel {
    private final double annualisedVol;
    private final double longTermMean;
    private final double meanReversionSpeed;

    public VasicekModel(long initialTimeStamp, double initialPrice, double annualisedVol, double longTermMean,
                        double meanReversionSpeed, StandardNormalGenerator stdNormalGenerator) {
        super(stdNormalGenerator, initialTimeStamp, initialPrice);
        this.annualisedVol = annualisedVol;
        this.longTermMean = longTermMean;
        this.meanReversionSpeed = meanReversionSpeed;
    }

    @Override
    public double evolvePriceUntil(long timeStamp) {
        double brownianIncrement = evolveRandomGenerator(timeStamp);
        double diffusionTerm = brownianIncrement*annualisedVol;

        long timeDelta = getTimeDelta(timeStamp);
        double driftTerm = meanReversionSpeed*(longTermMean - currentPrice)*getYearFraction(timeDelta);

        double newPriceDelta = driftTerm + diffusionTerm;
        double newPrice = currentPrice + newPriceDelta;

        currentInternalTimestamp = timeStamp;
        currentPrice = newPrice;
        return newPrice;
    }

    /**
     * In the case of the Vasicek model our random generator is a standard Brownian Motion, so we must multiply the
     * standard normal by the sqrt of the time-delta.
     */
    private double evolveRandomGenerator(long timeStamp) {
        double yearFraction = getYearFraction(getTimeDelta(timeStamp));
        return this.randomGenerator.getNextValue()*Math.sqrt(yearFraction);
    }

    @Override
    public String toString() {
        return "VasicekModel[" +
                    "currentInternalTimestamp: " + this.currentInternalTimestamp +
                    ", currentPrice: " + this.currentPrice +
                    ", annualisedVol: " + this.annualisedVol +
                    ", longTermMean: " + this.longTermMean +
                    ", meanReversionSpeed: " + this.meanReversionSpeed
                    + "]";
    }
}
