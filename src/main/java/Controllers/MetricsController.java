package Controllers;

import Entities.Ticket;
import Metrics.DeviceMetrics;
import Metrics.SourceMetrics;

import java.util.LinkedList;
import java.util.List;

public class MetricsController {
    private List<SourceMetrics> sourceMetrics;//index + 1 - номер источника
    private List<DeviceMetrics> deviceMetrics;//index + 1 - номер прибора

    public MetricsController(int numberOfSources, int numberOfDevices) {
        this.sourceMetrics = new LinkedList<>();
        this.deviceMetrics = new LinkedList<>();
        for (int i = 0; i < numberOfSources; i++) {
            sourceMetrics.add(new SourceMetrics());
        }
        for (int i = 0; i < numberOfDevices; i++) {
            deviceMetrics.add(new DeviceMetrics());
        }
    }

    void getGenerated(Ticket ticket) {
        this.sourceMetrics.get(ticket.getSourceNumber() - 1).ticketGenerated(ticket);
    }

    void getReceived(Ticket ticket) {
        this.sourceMetrics.get(ticket.getSourceNumber() - 1).ticketReceived(ticket);
    }

    void getSucceed(Ticket ticket, int deviceNum) {
        this.sourceMetrics.get(ticket.getSourceNumber() - 1).ticketSucceed(ticket);
        double newDeviceTimeWork = ticket.getFinishedTime() - ticket.getReceivedTime();
        this.deviceMetrics.get(deviceNum - 1).worked(newDeviceTimeWork);
    }

    void getFailed(Ticket ticket, double time) {
        System.out.println("FAILED: source number - " + ticket.getSourceNumber() + " ticket id - " + ticket.getRequestNumber());
        this.sourceMetrics.get(ticket.getSourceNumber() - 1).ticketFailed(ticket, time);
    }

    public long getNumberGeneratedRequests(int sourceIndex) {
        return sourceMetrics.get(sourceIndex).getNumberOfGeneratedTickets();
    }

    public double getAverageProcessingTime(int sourceIndex) {
        double timeProcessing = sourceMetrics.get(sourceIndex).getTimeProcessing();
        int numberOfSucceedTickets = sourceMetrics.get(sourceIndex).getNumberOfSucceedTickets();
        return timeProcessing / numberOfSucceedTickets;
    }

    public double getDispersionProcessingTime(int sourceIndex) {
        double squaredSum = 0.0;
        for(int i = 0; i < sourceMetrics.get(sourceIndex).getNumberOfSucceedTickets(); i++) {
            double tmp = sourceMetrics.get(sourceIndex).getSucceededTicketProcTime().get(i);
            squaredSum += Math.pow(tmp - this.getAverageProcessingTime(sourceIndex), 2) ;
        }
        int numberOfSucceedTickets = sourceMetrics.get(sourceIndex).getNumberOfSucceedTickets();
        return squaredSum / numberOfSucceedTickets;
    }

    public double getAverageWaitingTime(int sourceIndex) {
        double timeWaiting = sourceMetrics.get(sourceIndex).getTimeWaiting();
        int numberOfSucceedTickets = sourceMetrics.get(sourceIndex).getNumberOfSucceedTickets();
        return timeWaiting / numberOfSucceedTickets;
    }

    public double getDispersionWaitingTime(int sourceIndex) {
        double squaredSum = 0.0;
        for(int i = 0; i < sourceMetrics.get(sourceIndex).getNumberOfSucceedTickets(); i++) {
            double tmp = sourceMetrics.get(sourceIndex).getSucceededTicketWaitTime().get(i);
            squaredSum += Math.pow(tmp - this.getAverageWaitingTime(sourceIndex), 2) ;
        }
        int numberOfSucceedTickets = sourceMetrics.get(sourceIndex).getNumberOfSucceedTickets();
        return squaredSum / numberOfSucceedTickets;
    }

    public double getAverageTimeInSystem(int sourceIndex) {
        double timeInSystem = sourceMetrics.get(sourceIndex).getTimeInSystem();
        int numberOfSucceedTickets = sourceMetrics.get(sourceIndex).getNumberOfSucceedTickets();
        return timeInSystem / numberOfSucceedTickets;
    }

    public double getFailureProbability(int sourceIndex) {
        return (double) (sourceMetrics.get(sourceIndex).getNumberOfFailedTickets()
                / (double) sourceMetrics.get(sourceIndex).getNumberOfGeneratedTickets());
    }

    public double getUsageRate(int deviceIndex, double systemTime) {
        return deviceMetrics.get(deviceIndex).getWorkTime() / systemTime;
    }

    public long getAllNumberGeneratedRequests() {
        long result = 0;
        for (SourceMetrics statistic : sourceMetrics) {
            result += statistic.getNumberOfGeneratedTickets();
        }
        return result;
    }

    public long getAllNumberFailedRequests() {
        long result = 0;
        for (SourceMetrics statistic : sourceMetrics) {
            result += statistic.getNumberOfFailedTickets();
        }
        return result;
    }
}
