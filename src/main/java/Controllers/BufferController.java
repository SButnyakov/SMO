package Controllers;

import Entities.Buffer;
import Entities.Ticket;

import java.util.LinkedList;
import java.util.List;

public class BufferController {
    private List<Buffer> buffer;
    private Buffer toStoreBuffer;
    private Buffer toSentBuffer;
    private MetricsController metricsController;

    public BufferController(MetricsController statisticsController, int bufferNumber) {
        this.metricsController = statisticsController;
        buffer = new LinkedList<>();
        for (int i = 0; i < bufferNumber; i++) {
            buffer.add(new Buffer(i + 1));
        }
        this.toSentBuffer = buffer.get(0);
        this.toStoreBuffer = buffer.get(0);
    }

    public void chooseToStore() {
        if (this.isFull()) {
            System.out.println("BUFFER IS FULL");
            for (Buffer el : buffer) {
                //ищем самую старую заявку в буфере
                if (el.getCurrentRequest().getCreationTime() < this.toStoreBuffer.getCurrentRequest().getCreationTime()) {
                    this.toStoreBuffer = el;
                }
            }
        } else {
            for (Buffer el : buffer) {
                if (el.isFree()) {
                    this.toStoreBuffer = el;
                    return;
                }
            }
        }
    }

    public void chooseToSent() {
        //  LIFO выбираем последнюю пришедшую заявку
        toSentBuffer = null;
        for (Buffer el : buffer) {
            if (toSentBuffer == null && !el.isFree()) {
                toSentBuffer = el;
            } else if (!el.isFree() && toSentBuffer != null) {
                if (el.getReceiveTime() > toSentBuffer.getReceiveTime()) {
                    toSentBuffer = el;
                } else if (el.getReceiveTime() == toSentBuffer.getReceiveTime()
                        && el.getCurrentRequest().getCreationTime() < toSentBuffer.getCurrentRequest().getCreationTime()) {
                    toSentBuffer = el;
                }
            }
        }
    }

    public Ticket sendRequest() {
        chooseToSent();
        return toSentBuffer.sendTicket();
    }

    public Ticket receiveRequest(Ticket ticket) {
        this.chooseToStore();
        Ticket failedTicked = null;
        if (this.isFull()) {
            System.out.println(" BUFFER: " + toStoreBuffer.getCurrentRequest().getRequestNumber() +
                    " replaced by " + ticket.getRequestNumber());
            this.metricsController.getFailed(ticket, ticket.getCreationTime());
            failedTicked = toStoreBuffer.getCurrentRequest();
        } else {
            System.out.println("BUFFER " + buffer.indexOf(toStoreBuffer) + ": source number - " + ticket.getSourceNumber()
                    + " ticket id-" + ticket.getRequestNumber() + " received");
        }
        toStoreBuffer.receiveTicket(ticket);
        return failedTicked;
    }

    public boolean isFull() {
        for (Buffer el : buffer) {
            if (el.isFree()) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        for (Buffer el : buffer) {
            if (!el.isFree()) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty(int index) {
        return buffer.get(index).isFree();
    }

    public int getNumberOfBuffers() {
        return buffer.size();
    } //для вывода статуса буфера

    public String getCurrentTicketId(int index) {//для вывода статуса буфера
        return buffer.get(index).getCurrentRequestNumber();
    }

    public double getReceiveTime(int index) {
        return buffer.get(index).getReceiveTime();
    }//для вывода статуса буфера

    public Buffer getStoreBuffer() {
        return toStoreBuffer;
    }

    public Buffer getSentBuffer() {
        return toSentBuffer;
    }
}
