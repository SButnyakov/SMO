package Entities;

public class Buffer {
    private int number;
    private Ticket currentTicket;

    public Buffer(int number) {
        this.number = number;
        currentTicket = null;
    }

    public boolean isFree() {
        return currentTicket == null;
    }

    public void receiveTicket(Ticket ticket) {
        this.currentTicket = ticket;
    }

    public Ticket sendTicket() {
        Ticket ticketToSend = this.currentTicket;
        this.currentTicket = null;
        return ticketToSend;
    }

    public Ticket getCurrentRequest() {
        return currentTicket;
    }

    public int getSourceNumber() {
        return this.currentTicket.getSourceNumber();
    }

    public String getCurrentRequestNumber() { //для вывода в пошаговом режиме
        return this.currentTicket.getRequestNumber();
    }

    public double getReceiveTime() { //для вывода в пошаговом режиме
        return this.currentTicket.getCreationTime();
    }

    public int getNumber() {
        return number;
    }
}
