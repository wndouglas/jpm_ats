package org.wd.rfq;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class RfqResponseEvent {
    private final double paySwapRate;
    private final double receiveSwapRate;
    private final long timestamp;
    private final long notional;
    private final DecimalFormat decimalFormat;

    public RfqResponseEvent(double paySwapRate, double receiveSwapRate, long timestamp, long notional) {
        this.paySwapRate = paySwapRate;
        this.receiveSwapRate = receiveSwapRate;
        this.timestamp = timestamp;
        this.notional = notional;

        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setDecimalSeparator('.');
        this.decimalFormat = new DecimalFormat("#.#####", symbols);
    }

    public double getPaySwapRate() {
        return paySwapRate;
    }

    public double getReceiveSwapRate() {
        return receiveSwapRate;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        long notionalMillions = this.notional/1000000;
        return "RfqResponseEvent[" +
                "timestamp: " + this.timestamp +
                ", notional (m): " + notionalMillions +
                ", pay (client perspective): " + (long)Math.floor(this.paySwapRate*1e4)/100.0 + "%" +
                ", receive bps (client perspective): " + (long)Math.floor(this.receiveSwapRate*1e4)/100.0 + "%" +
                "]";
    }
}
