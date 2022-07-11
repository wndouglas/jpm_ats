package org.wd.rfq.logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpreadCalculatorTest {
    private final double delta = 1e-8;
    @Test
    void getSpreadCorrectSpreadWithZeroNotional() {
        double minSpread = SpreadCalculator.MIN_SPREAD_BPS;
        long maxNotional = (long) 1e7;
        long currentNotional = 0;

        double expectedSpread = minSpread/1e4;
        SpreadCalculator spreadCalculator = new SpreadCalculator(maxNotional);
        double spread = spreadCalculator.getSpread(currentNotional);

        assertEquals(expectedSpread, spread, delta);
    }

    @Test
    void getSpreadCorrectWithMaxNotional() {
        double maxSpread = SpreadCalculator.MAX_SPREAD_BPS;
        long maxNotional = (long) 1e7;

        double expectedSpread = maxSpread/1e4;

        SpreadCalculator spreadCalculator = new SpreadCalculator(maxNotional);
        double spread = spreadCalculator.getSpread(maxNotional);

        assertEquals(expectedSpread, spread, delta);
    }
}
