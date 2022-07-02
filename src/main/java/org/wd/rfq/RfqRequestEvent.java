package org.wd.rfq;

public class RfqRequestEvent {
    private final long notional;
    private final long timestamp;

    RfqRequestEvent(long notional, long timestamp) {
        this.notional = notional;
        this.timestamp = timestamp;
    }

    public long getNotional() {
        return notional;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        long notionalMillions = this.notional/1000000;
        return "RfqRequestEvent[timestamp: " + this.timestamp + ", notional (m): " + notionalMillions + "]";
    }
}
