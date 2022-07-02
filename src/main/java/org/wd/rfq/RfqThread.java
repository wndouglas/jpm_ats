package org.wd.rfq;

import org.wd.rfq.model.Model;
import org.wd.rfq.util.RandomGenerator;

import java.util.logging.Logger;

public class RfqThread implements Runnable {
    private Thread t;
    private String threadName;
    private long iterationNumber;
    private final DataManager dataManager;
    private final RandomGenerator randomGenerator;

    private static final Logger LOGGER = Logger.getGlobal();

    public RfqThread(String threadName, DataManager dataManager, RandomGenerator randomGenerator) {
        LOGGER.info("Creating thread: " + threadName);
        this.threadName = threadName;
        iterationNumber = 0;
        this.dataManager = dataManager;
        this.randomGenerator = randomGenerator;
    }

    @Override
    public void run() {
        while(true) {
            try {
                try {
                    Double currentSwapMid = dataManager.getSwapRateMid();
                    Long currentSwapTimestamp = dataManager.getCurrentTimestamp();

                    LOGGER.info("Thread: " + threadName + ", iteration: " + iterationNumber + ", current swap mid: " + currentSwapMid + ", as of timestamp: " + currentSwapTimestamp);

                    long refreshRateMillis = (long)(randomGenerator.getNextValue()*1000);
                    Thread.sleep(refreshRateMillis);
                    iterationNumber += 1;

                    if (iterationNumber == Long.MAX_VALUE) {
                        break;
                    }
                }
                catch (NullPointerException e) {
                    LOGGER.info("Tried to RFQ with no prices available - ignoring");
                }

            } catch (InterruptedException e) {
                LOGGER.info("Thread: " + threadName + " interrupted");
            }
        }
    }

    public void start() {
        LOGGER.info("Starting thread: " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
}
