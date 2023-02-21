package Metrics;

public class DeviceMetrics {
    private double workTime;
    private long numberOfTicketsProcessed;

    public DeviceMetrics() {
        this.numberOfTicketsProcessed = 0;
        this.workTime = 0;
    }

    public double getWorkTime() {
        return workTime;
    }

    public void worked(double workTime) {
        this.workTime += workTime;
        this.numberOfTicketsProcessed++;
    }
}
