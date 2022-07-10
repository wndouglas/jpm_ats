package org.wd.rfq.model;

import org.wd.rfq.simulation.StandardNormalGenerator;

class GbmModel extends StochasticModel {
    private final double annualisedVol;
    private final double annualisedMeanDrift;

    public GbmModel(long initialTimeStamp, double initialPrice, double annualisedVol, double annualisedMeanDrift,
                    StandardNormalGenerator stdNormalGenerator) {
        super(stdNormalGenerator, initialTimeStamp, initialPrice);
        this.annualisedVol = annualisedVol;
        this.annualisedMeanDrift = annualisedMeanDrift;
    }

    @Override
    public double evolvePriceUntil(long timeStamp) {
        double brownianIncrement = evolveRandomGenerator(timeStamp);
        double diffusionTerm = brownianIncrement*annualisedVol;

        long timeDelta = getTimeDelta(timeStamp);
        double driftTerm = annualisedMeanDrift*getYearFraction(timeDelta);

        double newPriceDelta = currentPrice*(driftTerm + diffusionTerm);
        double newPrice = currentPrice + newPriceDelta;

        currentInternalTimestamp = timeStamp;
        currentPrice = newPrice;
        return newPrice;
    }

    /**
     * In the case of the Vasicek model our random generator is a standard Brownian Motion, so we must multiply the
     * standard normal by the sqrt of the time-delta.
     */
    @Override
    public double evolveRandomGenerator(long timeStamp) {
        double yearFraction = getYearFraction(getTimeDelta(timeStamp));
        return this.randomGenerator.getNextValue()*Math.sqrt(yearFraction);
    }

    @Override
    public String toString() {
        return "GbmModel[" +
                "currentInternalTimestamp: " + this.currentInternalTimestamp +
                ", currentPrice: " + this.currentPrice +
                ", annualisedVol: " + this.annualisedVol +
                ", annualisedMeanDrift: " + this.annualisedMeanDrift +
                "]";
    }
}
