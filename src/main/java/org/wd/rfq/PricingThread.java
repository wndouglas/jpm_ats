package org.wd.rfq;

import java.util.logging.Logger;

public class PricingThread implements Runnable {
    private Thread t;
    private String threadName;
    private long refreshRateMillis;
    private long iterationNumber;

    private static final Logger LOGGER = Logger.getGlobal();

    public PricingThread(String threadName, double refreshRate) {
        this.threadName = threadName;
        this.refreshRateMillis = (long)refreshRate*1000;
        LOGGER.info("Creating thread: " + threadName);
        iterationNumber = 0;
    }

    @Override
    public void run() {
        LOGGER.info("Running thread: " + threadName);

        while(true) {
            try {
                LOGGER.info("Thread: " + threadName + ", iteration: " + iterationNumber);
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
}
