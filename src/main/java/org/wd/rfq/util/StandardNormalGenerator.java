package org.wd.rfq.util;

import java.util.Random;

public class StandardNormalGenerator implements RandomGenerator {
    private final Random randomGenerator;

    public StandardNormalGenerator(long seed) {
        this.randomGenerator = new Random(seed);
    }

    public double getNextValue() {
        return randomGenerator.nextGaussian();
    }
}
