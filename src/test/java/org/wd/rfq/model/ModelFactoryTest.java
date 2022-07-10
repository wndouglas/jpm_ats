package org.wd.rfq.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ModelFactoryTest {

    private JSONObject constructJsonModelConfig() {
        String modelTestString = "{'swapSpreadRefreshRate': 0.01,
                'bondYieldRefreshRate": 0.1,
                'maxRfqRefreshRate': 1,
                "rfqRequestRandomTimeSeed": 0,
                "maxRfqNotional": 100,
                "rfqRequestRandomNotionalSeed": 1,
                "swapSpreadModel": {
            "modelType": "Vasicek",
                    "initialValue": 0.008,
                    "annualisedVol": 0.5,
                    "longTermMean": 0.01,
                    "meanReversionSpeed": 0.5,
                    "seed": 2
        },
        "bondYieldModel": {
            "modelType": "Vasicek",
                    "initialValue": 0.012,
                    "annualisedVol": 0.1,
                    "longTermMean": 0.02,
                    "meanReversionSpeed": 0.1,
                    "seed": 3
        }
}"
        return new JSONObject(modelTestString);
    }

    @Test
    void getModelTest() throws IOException {
        JSONObject jsonModelConfig = constructJsonModelConfig();
    }
}
