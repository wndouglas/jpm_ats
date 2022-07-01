package org.wd.rfq;

import org.json.JSONObject;
import org.wd.rfq.model.Model;
import org.wd.rfq.model.ModelFactory;

import java.util.logging.Logger;

public class RfqSimulator {
    private final Logger LOGGER = Logger.getGlobal();

    private final double refreshRate;
    private final Model swapSpreadModel;
    private final Model bondModel;

    private RfqSimulator(RfqSimulatorBuilder builder) {
        this.refreshRate = builder.refreshRate;
        this.swapSpreadModel = builder.swapSpreadModel;
        this.bondModel = builder.bondModel;

        LOGGER.info("Constructing RFQ Simulator with parameters:");
        LOGGER.info("* refreshRate: " + refreshRate);
        LOGGER.info("* swapSpreadModel: " + swapSpreadModel);
        LOGGER.info("* bondModel: " + bondModel);
    }

    public void run() {
        PricingThread p1 = new PricingThread("pricingThread", refreshRate);
        p1.start();

        PricingThread p2 = new PricingThread("pricingThreadAlt", refreshRate);
        p2.start();
    }

    public double getRefreshRate() {
        return refreshRate;
    }

    @Override
    public String toString() {
        return "RfqSimulator[" +
                "refreshRate: " + this.refreshRate +
                "swapSpreadModel: " + this.swapSpreadModel +
                "bondModel: " + this.bondModel +
                "]";
    }

    public static class RfqSimulatorBuilder
    {
        private double refreshRate;
        private Model swapSpreadModel;
        private Model bondModel;

        public RfqSimulatorBuilder refreshRate(double refreshRate) {
            this.refreshRate = refreshRate;
            return this;
        }

        public RfqSimulatorBuilder swapSpreadModel(JSONObject swapSpreadModelConfig) {
            this.swapSpreadModel = ModelFactory.getModel(swapSpreadModelConfig);
            return this;
        }

        public RfqSimulatorBuilder bondModel(JSONObject bondModelConfig) {
            this.bondModel = ModelFactory.getModel(bondModelConfig);
            return this;
        }

        public RfqSimulator build() {
            RfqSimulator rfqSimulator = new RfqSimulator(this);
            validateRfqSimulator(rfqSimulator);
            return rfqSimulator;
        }

        public static RfqSimulator buildFromJsonString(String jsonString) {
            JSONObject configJsonObject = new JSONObject(jsonString);
            double refreshRate = configJsonObject.getDouble("refreshRate");
            JSONObject swapSpreadModelConfig = configJsonObject.getJSONObject("swapSpreadModel");
            JSONObject bondModelConfig = configJsonObject.getJSONObject("bondModel");
            return new RfqSimulatorBuilder()
                    .refreshRate(refreshRate)
                    .swapSpreadModel(swapSpreadModelConfig)
                    .bondModel(bondModelConfig)
                    .build();
        }

        private void validateRfqSimulator(RfqSimulator simulator) {
            // Can add validation to ensure simulator has been constructed in a valid state.
        }
    }
}
