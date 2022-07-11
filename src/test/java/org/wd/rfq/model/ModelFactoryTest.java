package org.wd.rfq.model;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ModelFactoryTest {
    private JSONObject constructVasicekJsonModelConfig() {
        String modelTestString =
                        "{'modelType': 'Vasicek',\n" +
                        "'initialValue': 0.008,\n" +
                        "'annualisedVol': 0.5,\n" +
                        "'longTermMean': 0.01,\n" +
                        "'meanReversionSpeed': 0.5,\n" +
                        "'seed': 2}";
        return new JSONObject(modelTestString);
    }

    private JSONObject constructGbmJsonModelConfig() {
        String modelTestString =
                "{'modelType': 'GBM',\n" +
                        "'initialValue': 0.008,\n" +
                        "'annualisedVol': 0.5,\n" +
                        "'annualisedMeanDrift': 0.01,\n" +
                        "'seed': 2}";
        return new JSONObject(modelTestString);
    }

    private JSONObject constructInvalidJsonModelConfig() {
        String modelTestString = "{'modelType': 'Invalid'}";
        return new JSONObject(modelTestString);
    }

    @Test
    void getVasicekModelTest() {
        JSONObject jsonModelConfig = constructVasicekJsonModelConfig();
        Model vasicekModel = ModelFactory.getModel(jsonModelConfig);
        assertInstanceOf(VasicekModel.class, vasicekModel);
    }

    @Test
    void getGbmModelTest() {
        JSONObject jsonModelConfig = constructGbmJsonModelConfig();
        Model gbmModel = ModelFactory.getModel(jsonModelConfig);
        assertInstanceOf(GbmModel.class, gbmModel);
    }

    @Test
    void InvalidModelThrowsInvalidParameterException() {
        JSONObject jsonModelConfig = constructInvalidJsonModelConfig();
        assertThrows(InvalidParameterException.class, () -> {ModelFactory.getModel(jsonModelConfig);});
    }
}
