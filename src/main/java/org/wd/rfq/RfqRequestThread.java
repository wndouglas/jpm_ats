package org.wd.rfq;

import org.wd.rfq.simulation.RandomGenerator;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class RfqRequestThread implements Runnable {
    private Thread t;
    private String threadName;
    private long iterationNumber;
    private final RandomGenerator rfqRequestRandomTimeSimulator;
    private final RandomGenerator rfqRequestRandomNotionalSimulator;
    private final BlockingQueue<RfqRequestEvent> eventQueue;

    private static final Logger LOGGER = Logger.getGlobal();

    public RfqRequestThread(
            String threadName,
            RandomGenerator rfqRequestRandomTimeSimulator,
            RandomGenerator rfqRequestRandomNotionalSimulator,
            BlockingQueue<RfqRequestEvent> eventQueue) {
        LOGGER.info("Creating thread: " + threadName);
        this.threadName = threadName;
        iterationNumber = 0;
        this.rfqRequestRandomTimeSimulator = rfqRequestRandomTimeSimulator;
        this.rfqRequestRandomNotionalSimulator = rfqRequestRandomNotionalSimulator;
        this.eventQueue = eventQueue;
    }

    @Override
    public void run() {
        while(true) {
            try {
                long refreshRateMillis = (long)(rfqRequestRandomTimeSimulator.getNextValue()*1000);
                Thread.sleep(refreshRateMillis);

                long rfqNotional = (long)(rfqRequestRandomNotionalSimulator.getNextValue()*1000000);
                long requestTimestamp = System.currentTimeMillis();

                RfqRequestEvent rfqEvent = new RfqRequestEvent(rfqNotional, requestTimestamp);

                LOGGER.info("Thread: " + threadName + ", iteration: " + iterationNumber + ", client submitted RFQ: " + rfqEvent);

                eventQueue.put(rfqEvent);

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
