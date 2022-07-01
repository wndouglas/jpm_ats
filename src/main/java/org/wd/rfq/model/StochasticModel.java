package org.wd.rfq.model;

import org.wd.rfq.util.RandomGenerator;

public abstract class StochasticModel implements Model {
    protected final RandomGenerator randomGenerator;

    public StochasticModel(RandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    public abstract double evolveRandomGenerator(long timeStamp);
}
