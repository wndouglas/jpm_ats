package org.wd.rfq.simulation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class StandardNormalGeneratorTest {
    private StandardNormalGenerator standardNormalGenerator;
    private final long seed = 1;

    @BeforeEach
    void setStandardNormalGenerator() {
        standardNormalGenerator = new StandardNormalGenerator(seed);
    }

    @Test
    void nextValueIsDistinct() {
        double firstValue = standardNormalGenerator.getNextValue();
        double secondValue = standardNormalGenerator.getNextValue();

        assertNotEquals(firstValue, secondValue);
    }
}
