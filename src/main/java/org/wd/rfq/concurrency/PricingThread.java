package org.wd.rfq.concurrency;

import org.wd.rfq.DataManager;
import org.wd.rfq.model.Model;

import java.util.logging.Logger;

/**
 * The PricingThread runs a model at a constant refresh rate in its own thread,
 * updating the DataManager instance
 */
public class PricingThread implements Runnable {
    private Thread t;
    private final String threadName;
    private final long refreshRateMillis;
    private long iterationNumber;
    private final Model model;
    private final DataManager dataManager;
    private final String midKey;
    private static final Logger LOGGER = Logger.getGlobal();

    public PricingThread(String threadName, double refreshRate, Model model, DataManager dataManager, String midKey) {
        LOGGER.info("Creating thread: " + threadName);
        this.threadName = threadName;
        refreshRateMillis = (long)(refreshRate*1000);
        iterationNumber = 0;
        this.model = model;
        this.dataManager = dataManager;
        this.midKey = midKey;
    }

    @Override
    public void run() {
        LOGGER.info("Running thread: " + threadName);

        while(true) {
            try {
                long currentTimeStamp = System.currentTimeMillis();
                double currentMid = model.evolvePriceUntil(currentTimeStamp);
                dataManager.setCurrentTimestamp(currentTimeStamp);
                dataManager.setMid(midKey, currentMid);

                LOGGER.info("Thread: " + threadName + ", current mid for " + midKey + ": " + (long)Math.floor(currentMid*1e6)/1e4 + "%, as of timestamp: " + currentTimeStamp);

                Thread.sleep(refreshRateMillis);
                iterationNumber += 1;

                if (iterationNumber == Long.MAX_VALUE) {
                    break;
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

    @Override
    public String toString() {
        return "PricingThread[" +
                "threadName: " + this.threadName +
                ", refreshRateMillis: " + this.refreshRateMillis +
                ", model: " + this.model +
                ", dataManager: " + this.dataManager +
                ", midKey: " + this.midKey +
                "]";
    }
}
