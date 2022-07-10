package org.wd.rfq;

import org.json.JSONObject;
import org.wd.rfq.concurrency.PricingThread;
import org.wd.rfq.concurrency.RfqRequestThread;
import org.wd.rfq.concurrency.RfqResponseThread;
import org.wd.rfq.event.RfqRequestEvent;
import org.wd.rfq.logic.SpreadCalculator;
import org.wd.rfq.model.Model;
import org.wd.rfq.model.ModelFactory;
import org.wd.rfq.simulation.RandomGenerator;
import org.wd.rfq.simulation.UniformGenerator;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Logger;

public class RfqSimulator {
    private static final Logger LOGGER = Logger.getGlobal();
    public static final long NOTIONAL_UNIT = 1_000_000;
    private final double swapSpreadRefreshRate;
    private final double bondYieldRefreshRate;
    private final int maxRfqRefreshRate;
    private final long maxRfqNotional;
    private final SpreadCalculator spreadCalculator;
    private final RandomGenerator rfqRequestRandomTimeSimulator;
    private final RandomGenerator rfqRequestRandomNotionalSimulator;
    private final Model swapSpreadModel;
    private final Model bondYieldModel;

    private RfqSimulator(RfqSimulatorBuilder builder) {
        swapSpreadRefreshRate = builder.swapSpreadRefreshRate;
        bondYieldRefreshRate = builder.bondYieldRefreshRate;
        maxRfqRefreshRate = builder.maxRfqRequestRate;
        maxRfqNotional = builder.maxRfqNotional;
        spreadCalculator = builder.spreadCalculator;
        rfqRequestRandomTimeSimulator = builder.rfqRequestRandomTimeSimulator;
        rfqRequestRandomNotionalSimulator = builder.rfqRequestRandomNotionalSimulator;
        swapSpreadModel = builder.swapSpreadModel;
        bondYieldModel = builder.bondYieldModel;

        LOGGER.info("Constructing RFQ Simulator with parameters:");
        LOGGER.info("* swapSpreadRefreshRate: " + swapSpreadRefreshRate);
        LOGGER.info("* bondYieldRefreshRate: " + bondYieldRefreshRate);
        LOGGER.info("* maxRfqNotional: " + maxRfqNotional);
        LOGGER.info("* maxRfqRefreshRate: " + maxRfqRefreshRate);
        LOGGER.info("* swapSpreadModel: " + swapSpreadModel);
        LOGGER.info("* bondYieldModel: " + bondYieldModel);
    }

    public void run() {
        DataManager dataManager = new DataManager();
        BlockingQueue<RfqRequestEvent> eventQueue = new LinkedBlockingDeque<>();

        PricingThread swapSpreadThread = new PricingThread(
                "swapSpreadThread", swapSpreadRefreshRate, swapSpreadModel, dataManager, DataManager.SWAP_SPREAD_KEY);
        swapSpreadThread.start();

        PricingThread bondYieldThread = new PricingThread(
                "bondYieldThread", bondYieldRefreshRate, bondYieldModel, dataManager, DataManager.BOND_YIELD_KEY);
        bondYieldThread.start();

        RfqRequestThread rfqRequestThread = new RfqRequestThread("rfqRequestThread", rfqRequestRandomTimeSimulator, rfqRequestRandomNotionalSimulator, eventQueue);
        rfqRequestThread.start();

        RfqResponseThread rfqResponseThread = new RfqResponseThread("rfqResponseThread", dataManager, eventQueue, spreadCalculator);
        rfqResponseThread.start();
    }

    @Override
    public String toString() {
        return "RfqSimulator[" +
                "swapSpreadRefreshRate: " + this.swapSpreadRefreshRate +
                "bondYieldRefreshRate: " + this.bondYieldRefreshRate +
                "maxRfqRefreshRate: " + this.maxRfqRefreshRate +
                "maxRfqNotional: " + this.maxRfqNotional +
                "swapSpreadModel: " + this.swapSpreadModel +
                "bondYieldModel: " + this.bondYieldModel +
                "]";
    }

    public static class RfqSimulatorBuilder
    {
        private double swapSpreadRefreshRate;
        private double bondYieldRefreshRate;
        private int maxRfqRequestRate;
        private long maxRfqNotional;
        private SpreadCalculator spreadCalculator;
        private RandomGenerator rfqRequestRandomTimeSimulator;
        private RandomGenerator rfqRequestRandomNotionalSimulator;
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

        public RfqSimulatorBuilder rfqRequestRandomTimeSimulator(long rfqRequestRandomTimeSeed, int maxRfqRefreshRate) {
            this.maxRfqRequestRate = maxRfqRefreshRate;
            this.rfqRequestRandomTimeSimulator = new UniformGenerator(rfqRequestRandomTimeSeed, maxRfqRefreshRate);
            return this;
        }

        public RfqSimulatorBuilder rfqRequestRandomNotionalSimulator(long rfqRequestRandomNotionalSeed, int maxRfqNotional) {
            this.maxRfqNotional = maxRfqNotional*NOTIONAL_UNIT;
            this.spreadCalculator = new SpreadCalculator(this.maxRfqNotional);
            this.rfqRequestRandomNotionalSimulator = new UniformGenerator(rfqRequestRandomNotionalSeed, maxRfqNotional);
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
            validateRfqSimulator();
            return rfqSimulator;
        }

        public static RfqSimulator buildFromJsonString(String jsonString) {
            JSONObject configJsonObject = new JSONObject(jsonString);
            double swapSpreadRefreshRate = configJsonObject.getDouble("swapSpreadRefreshRate");
            double bondYieldRefreshRate = configJsonObject.getDouble("bondYieldRefreshRate");
            int maxRfqRefreshRate = configJsonObject.getInt("maxRfqRefreshRate");
            long rfqRequestRandomTimeSeed = configJsonObject.getLong("rfqRequestRandomTimeSeed");
            int maxRfqNotional = configJsonObject.getInt("maxRfqNotional");
            long rfqRequestRandomNotionalSeed = configJsonObject.getLong("rfqRequestRandomNotionalSeed");
            JSONObject swapSpreadModelConfig = configJsonObject.getJSONObject("swapSpreadModel");
            JSONObject bondYieldModelConfig = configJsonObject.getJSONObject("bondYieldModel");

            return new RfqSimulatorBuilder()
                    .swapSpreadRefreshRate(swapSpreadRefreshRate)
                    .bondYieldRefreshRate(bondYieldRefreshRate)
                    .swapSpreadModel(swapSpreadModelConfig)
                    .bondYieldModel(bondYieldModelConfig)
                    .rfqRequestRandomTimeSimulator(rfqRequestRandomTimeSeed, maxRfqRefreshRate)
                    .rfqRequestRandomNotionalSimulator(rfqRequestRandomNotionalSeed, maxRfqNotional)
                    .build();
        }

        private void validateRfqSimulator() {
            // Can add validation to ensure simulator has been constructed in a valid state.
        }
    }
}
