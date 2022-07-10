package org.wd.rfq.simulation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class UniformGeneratorTest {
    private static UniformGenerator uniformGenerator;
    private static final long seed = 1;
    private static final int maxValue = 10;

    @BeforeAll
    static void setUniformGenerator() {
        uniformGenerator = new UniformGenerator(seed, maxValue);
    }

    @RepeatedTest(20)
    void valueIsLessThanOrEqualsMax() {
        double value = uniformGenerator.getNextValue();
        assertTrue(value <= maxValue);
    }

    @RepeatedTest(20)
    void valueIsGreaterThanOrEqualsOne() {
        double value = uniformGenerator.getNextValue();
        assertTrue(value >= 1.0);
    }
}
