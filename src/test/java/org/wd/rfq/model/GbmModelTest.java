package org.wd.rfq.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wd.rfq.simulation.StandardNormalGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class GbmModelTest {
    private GbmModel model;
    private final double delta = 1e-8;
    @Mock StandardNormalGenerator standardNormalGenerator;

    @BeforeEach
    void setUpGbmModel() {
        Mockito.lenient().when(standardNormalGenerator.getNextValue()).thenReturn(1.0);
    }

    @Test
    void vasicekModelEvolvesCorrectly() {
        double initialPrice = 1.0;
        double annualisedVol = 0.2;
        double annualisedMeanDrift = 10.0;
        model = new GbmModel(0, initialPrice, annualisedVol, annualisedMeanDrift,
                standardNormalGenerator);
        int nextTimestamp = 1000000;

        double yearFraction = nextTimestamp/1000.0/60.0/60.0/24.0/252.0;
        double brownianIncrement = Math.sqrt(yearFraction);

        double diffusionTerm = brownianIncrement*annualisedVol*initialPrice;
        double driftTerm = annualisedMeanDrift*yearFraction*initialPrice;

        double newPriceDelta = driftTerm + diffusionTerm;
        double newPrice = initialPrice + newPriceDelta;

        double modelNewPrice = model.evolvePriceUntil(nextTimestamp);

        assertEquals(newPrice, modelNewPrice, delta);
    }
}
