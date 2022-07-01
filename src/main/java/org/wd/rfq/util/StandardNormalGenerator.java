package org.wd.rfq.util;

import java.util.Random;

public class StandardNormalGenerator implements RandomGenerator {
    private final long seed;
    private final Random randomGenerator;

    public StandardNormalGenerator(long seed) {
        this.seed = seed;
        this.randomGenerator = new Random(seed);
    }

    public double getNextValue() {
        return randomGenerator.nextGaussian();
    }

    public long getSeed() {
        return seed;
    }
}
