package org.wd.rfq.model;

import org.wd.rfq.simulation.RandomGenerator;

abstract class StochasticModel implements Model {
    protected long currentInternalTimestamp;
    protected double currentPrice;
    protected final RandomGenerator randomGenerator;

    public StochasticModel(RandomGenerator randomGenerator, long initialTimestamp, double initialPrice) {
        this.randomGenerator = randomGenerator;
        this.currentInternalTimestamp = initialTimestamp;
        this.currentPrice = initialPrice;
    }

    protected long getTimeDelta(long timeStamp) {
        return timeStamp - this.currentInternalTimestamp;
    }

    /**
     * Timestamps are in milliseconds, so we must convert
     * our millisecond time-deltas into year fractions.
     */
    protected double getYearFraction(long timeDelta) {
        double numSeconds = timeDelta/1000.0;
        double numMinutes = numSeconds/60.0;
        double numHours = numMinutes/60.0;
        double numDays = numHours/24.0;
        return numDays/252.0;
    }
}
