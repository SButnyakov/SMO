package Entities;

public class Source {
    private int number;
    private double lambda;
    private Ticket currentTicket;
    private int numberOfGeneratedTickets;//нуже для формирования ticket id

    public Source(int number, double lambda) {
        this.number = number;
        this.lambda = lambda;
        this.currentTicket = null;
        this.numberOfGeneratedTickets = 0;
        this.generateTicket();
    }

    private double distributionLaw() {
        return Math.log(1 - Math.random()) / (-this.lambda);
    }

    public void generateTicket() {
        double deltaTime = distributionLaw();
        double prevTime = this.currentTicket == null ? 0 : this.currentTicket.getCreationTime();
        System.out.println("SOURCE: source number - " + number + " generating to "
                + (prevTime + deltaTime));
        numberOfGeneratedTickets++;
        this.currentTicket = new Ticket(this.number, this.numberOfGeneratedTickets, prevTime + deltaTime);
    }

    public Ticket sendTicket() {
        Ticket ticketToSend = this.currentTicket;
        System.out.println("SOURCE: source number - " + ticketToSend.getSourceNumber() + " ticket id - "
                + ticketToSend.getRequestNumber() + " sent");
        this.generateTicket();
        return ticketToSend;
    }

    public Ticket getCurrentTicket() {
        return currentTicket;
    }

    public int getNumber() {
        return number;
    }
}
