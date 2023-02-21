package Metrics;

import Entities.Ticket;

import java.util.LinkedList;
import java.util.List;

public class SourceMetrics {
    private int numberOfGeneratedTickets;//количество сгенерированных заявок одного источника
    private int numberOfFailedTickets;//количество непринятых заявок одного источника
    private int numberOfSucceedTickets;//количество обработанных заявок одного источника

    private double timeInSystem;//время жизни заявки одного источника
    private double timeWaiting;//время между созданием и получением прибора
    private double timeProcessing;//время обработки прибором заявки одного источника
    private double squaredTimeProcessing;
    private double squaredTimeWaiting;

    private List<Double> succeededTicketProcTime;
    private List<Double> succeededTicketWaitTime;


    public SourceMetrics() {
        this.numberOfGeneratedTickets = 0;
        this.numberOfFailedTickets = 0;
        this.numberOfSucceedTickets = 0;

        this.timeInSystem = 0;
        this.timeWaiting = 0;
        this.timeProcessing = 0;
        this.squaredTimeProcessing= 0;
        this.squaredTimeWaiting= 0;

        succeededTicketProcTime = new LinkedList<>();
        succeededTicketWaitTime = new LinkedList<>();
    }

    public int getNumberOfGeneratedTickets() {
        return numberOfGeneratedTickets;
    }

    public int getNumberOfFailedTickets() {
        return numberOfFailedTickets;
    }

    public int getNumberOfSucceedTickets() {
        return numberOfSucceedTickets;
    }

    public double getTimeInSystem() {
        return timeInSystem;
    }

    public double getTimeWaiting() {
        return timeWaiting;
    }

    public double getTimeProcessing() {
        return timeProcessing;
    }

    public double getSquaredTimeProcessing() {
        return squaredTimeProcessing;
    }

    public double getSquaredTimeWaiting() {
        return squaredTimeWaiting;
    }

    public void ticketSucceed(Ticket ticket) {
        this.numberOfSucceedTickets++;

        this.timeProcessing += ticket.getFinishedTime() - ticket.getReceivedTime();
        this.squaredTimeProcessing += Math.pow(ticket.getFinishedTime() - ticket.getReceivedTime(), 2) ;
        this.timeInSystem += ticket.getFinishedTime() - ticket.getCreationTime();

        this.succeededTicketProcTime.add(ticket.getFinishedTime() - ticket.getReceivedTime());
    }

    public List<Double> getSucceededTicketProcTime() {
        return succeededTicketProcTime;
    }

    public List<Double> getSucceededTicketWaitTime() {
        return succeededTicketWaitTime;
    }

    public void ticketFailed(Ticket ticket, double currentTime) {
        this.numberOfFailedTickets++;

        this.timeInSystem += currentTime - ticket.getCreationTime();
    }

    public void ticketGenerated(Ticket ticket) {
        this.numberOfGeneratedTickets++;
    }

    public void ticketReceived(Ticket ticket) {
        this.timeWaiting += ticket.getReceivedTime() - ticket.getCreationTime();
        this.squaredTimeWaiting += Math.pow(ticket.getReceivedTime() - ticket.getCreationTime(), 2);
        this.succeededTicketWaitTime.add(ticket.getReceivedTime() - ticket.getCreationTime());
    }
}
