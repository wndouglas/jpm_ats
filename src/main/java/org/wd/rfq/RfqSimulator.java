package org.wd.rfq;

import org.json.JSONObject;
import org.wd.rfq.model.Model;
import org.wd.rfq.model.ModelFactory;
import org.wd.rfq.util.RandomGenerator;
import org.wd.rfq.util.UniformGenerator;

import java.util.logging.Logger;

public class RfqSimulator {
    private final Logger LOGGER = Logger.getGlobal();

    private final double swapSpreadRefreshRate;
    private final double bondYieldRefreshRate;
    private final int maxRfqRefreshRate;
    private final RandomGenerator rfqRandomRequestSimulator;
    private final Model swapSpreadModel;
    private final Model bondYieldModel;

    private RfqSimulator(RfqSimulatorBuilder builder) {
        swapSpreadRefreshRate = builder.swapSpreadRefreshRate;
        bondYieldRefreshRate = builder.bondYieldRefreshRate;
        maxRfqRefreshRate = builder.maxRfqRequestRate;
        rfqRandomRequestSimulator = builder.rfqRandomRequestSimulator;
        swapSpreadModel = builder.swapSpreadModel;
        bondYieldModel = builder.bondYieldModel;

        LOGGER.info("Constructing RFQ Simulator with parameters:");
        LOGGER.info("* swapSpreadRefreshRate: " + swapSpreadRefreshRate);
        LOGGER.info("* bondYieldRefreshRate: " + bondYieldRefreshRate);
        LOGGER.info("* maxRfqRefreshRate: " + maxRfqRefreshRate);
        LOGGER.info("* swapSpreadModel: " + swapSpreadModel);
        LOGGER.info("* bondYieldModel: " + bondYieldModel);
    }

    public void run() {
        DataManager dataManager = new DataManager();

        PricingThread swapSpreadThread = new PricingThread("swapSpreadThread", swapSpreadRefreshRate, swapSpreadModel, dataManager, DataManager.SWAP_SPREAD_KEY);
        PricingThread bondThread = new PricingThread("bondYieldThread", bondYieldRefreshRate, bondYieldModel, dataManager, DataManager.BOND_YIELD_KEY);
        RfqThread rfqThread = new RfqThread("rfqThread", dataManager, rfqRandomRequestSimulator);
        swapSpreadThread.start();
        bondThread.start();
        rfqThread.start();
    }

    public double getSwapSpreadRefreshRate() {
        return swapSpreadRefreshRate;
    }

    public double getBondYieldRefreshRate() {
        return bondYieldRefreshRate;
    }

    @Override
    public String toString() {
        return "RfqSimulator[" +
                "swapSpreadRefreshRate: " + this.swapSpreadRefreshRate +
                "bondYieldRefreshRate: " + this.bondYieldRefreshRate +
                "maxRfqRefreshRate: " + this.maxRfqRefreshRate +
                "swapSpreadModel: " + this.swapSpreadModel +
                "bondYieldModel: " + this.bondYieldModel +
                "]";
    }

    public static class RfqSimulatorBuilder
    {
        private double swapSpreadRefreshRate;
        private double bondYieldRefreshRate;
        private int maxRfqRequestRate;
        private RandomGenerator rfqRandomRequestSimulator;
        private Model swapSpreadModel;
        private Model bondYieldModel;

        public RfqSimulatorBuilder swapSpreadRefreshRate(double swapSpreadRefreshRate) {
            this.swapSpreadRefreshRate = swapSpreadRefreshRate;
            return this;
        }

        public RfqSimulatorBuilder bondYieldRefreshRate(double bondYieldRefreshRate) {
            this.bondYieldRefreshRate = bondYieldRefreshRate;
            return this;
        }

        public RfqSimulatorBuilder rfqRandomRequestSimulator(long rfqRandomRequestSeed, int maxRfqRefreshRate) {
            this.maxRfqRequestRate = maxRfqRefreshRate;
            this.rfqRandomRequestSimulator = new UniformGenerator(rfqRandomRequestSeed, maxRfqRefreshRate);
            return this;
        }

        public RfqSimulatorBuilder swapSpreadModel(JSONObject swapSpreadModelConfig) {
            this.swapSpreadModel = ModelFactory.getModel(swapSpreadModelConfig);
            return this;
        }

        public RfqSimulatorBuilder bondYieldModel(JSONObject bondYieldModelConfig) {
            this.bondYieldModel = ModelFactory.getModel(bondYieldModelConfig);
            return this;
        }

        public RfqSimulator build() {
            RfqSimulator rfqSimulator = new RfqSimulator(this);
            validateRfqSimulator(rfqSimulator);
            return rfqSimulator;
        }

        public static RfqSimulator buildFromJsonString(String jsonString) {
            JSONObject configJsonObject = new JSONObject(jsonString);
            double swapSpreadRefreshRate = configJsonObject.getDouble("swapSpreadRefreshRate");
            double bondYieldRefreshRate = configJsonObject.getDouble("bondYieldRefreshRate");
            int maxRfqRefreshRate = configJsonObject.getInt("maxRfqRefreshRate");
            long rfqRandomRequestSeed = configJsonObject.getLong("rfqRandomRequestSeed");
            JSONObject swapSpreadModelConfig = configJsonObject.getJSONObject("swapSpreadModel");
            JSONObject bondYieldModelConfig = configJsonObject.getJSONObject("bondYieldModel");
            return new RfqSimulatorBuilder()
                    .swapSpreadRefreshRate(swapSpreadRefreshRate)
                    .bondYieldRefreshRate(bondYieldRefreshRate)
                    .swapSpreadModel(swapSpreadModelConfig)
                    .bondYieldModel(bondYieldModelConfig)
                    .rfqRandomRequestSimulator(rfqRandomRequestSeed, maxRfqRefreshRate)
                    .build();
        }

        private void validateRfqSimulator(RfqSimulator simulator) {
            // Can add validation to ensure simulator has been constructed in a valid state.
        }
    }
}
