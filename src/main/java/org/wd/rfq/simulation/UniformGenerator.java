package org.wd.rfq.simulation;

import java.util.Random;

public class UniformGenerator implements RandomGenerator {
    private final Random randomGenerator;
    private final int minValue;
    private final int maxValue;

    public UniformGenerator(long seed, int maxValue) {
        this.randomGenerator = new Random(seed);
        this.minValue = 1;
        this.maxValue = maxValue;

    }

    public double getNextValue() {
        return randomGenerator.nextInt((maxValue - minValue) + 1) + minValue;
    }
}
