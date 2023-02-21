package Entities;

public class Ticket implements Comparable<Ticket> {
    private String requestNumber;
    private int sourceNumber;
    private double creationTime;
    private double receivedTime;
    private double finishedTime;

    public Ticket(int sourceNumber, int numberOfSourceTicket, double creationTime) {
        this.sourceNumber = sourceNumber;
        this.creationTime = creationTime;
        this.receivedTime = -1;
        this.finishedTime = -1;
        this.requestNumber = sourceNumber + "." + numberOfSourceTicket;
    }

    public void setReceivedTime(double receivedTime) {
        this.receivedTime = receivedTime;
    }

    public void setFinishedTime(double finishedTime) {
        this.finishedTime = finishedTime;
    }

    public String getRequestNumber() {
        return requestNumber;
    }

    public int getSourceNumber() {
        return sourceNumber;
    }

    public double getCreationTime() {
        return creationTime;
    }

    public double getReceivedTime() {
        return receivedTime;
    }

    public double getFinishedTime() {
        return finishedTime;
    }

    @Override
    public int compareTo(Ticket o) {
        return Double.compare(this.creationTime, o.creationTime);
    }
}
