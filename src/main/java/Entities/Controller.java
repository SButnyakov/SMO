package Entities;

import Controllers.BufferController;
import Controllers.DeviceController;
import Controllers.SourceController;
import Controllers.MetricsController;
import Enums.Action;

import java.text.DecimalFormat;

import static Enums.Action.*;

public class Controller {
    private double sourceEventTime;
    private double deviceEventTime;
    private int amountOfTickets;
    private MetricsController metricsController;
    private DeviceController deviceController;
    private SourceController sourceController;
    private BufferController bufferController;

    private Object[][] calendar;
    private Object[][] buffer;
    private int currentCalendarAction;
    private int currentBufferAction;

    public Controller(int amountOfTickets, int sourcesNumber, int devicesNumber, int bufferNumber,
                      double a, double b, double lambda) {
        this.amountOfTickets = amountOfTickets;
        this.metricsController = new MetricsController(sourcesNumber, devicesNumber);
        this.deviceController = new DeviceController(this.metricsController, devicesNumber, a, b );
        this.sourceController = new SourceController(this.metricsController, sourcesNumber, lambda);
        this.bufferController = new BufferController(this.metricsController, bufferNumber);
        this.sourceEventTime = 0;
        this.deviceEventTime = 0;

        this.calendar = new Object[(int) (amountOfTickets * amountOfTickets)][5];
        this.buffer = new Object[(int) (amountOfTickets * amountOfTickets)][3];
        currentCalendarAction = 0;
        currentBufferAction = 0;
    }

    public void autoMode() {
        while (metricsController.getAllNumberGeneratedRequests() < this.amountOfTickets) {
            stepByStepMode();
        }
    }

    public void stepByStepMode() {
        this.sourceEventTime = this.sourceController.getEventTime();
        this.deviceEventTime = this.deviceController.getEventTime();

        if (this.deviceEventTime <= this.sourceEventTime) {

            Ticket finishedTicket = deviceController.finishRequest();
            this.setCalendarStatus(FINISH, finishedTicket);

            if (!bufferController.isEmpty()) {
                Ticket receivedTicket = bufferController.sendRequest();
                deviceController.receiveRequest(receivedTicket);
                this.setCalendarStatus(RECEIVE, receivedTicket);
            }
        } else {
            this.setCalendarStatus(GENERATE, null);

            Ticket sendTicket = sourceController.sendRequest();
            Ticket failedTicket = bufferController.receiveRequest(sendTicket);
            if (failedTicket != null) {
                this.setCalendarStatus(FAIL, failedTicket);
            }
            this.setCalendarStatus(STORE, sendTicket);

            if (!deviceController.isFull()) {
                //this.setCalendarStatus(SENT, null);

                Ticket receivedTicket = bufferController.sendRequest();
                this.setCalendarStatus(SENT, receivedTicket);
                deviceController.receiveRequest(receivedTicket);

                this.setCalendarStatus(RECEIVE, receivedTicket);
            }
        }
    }

    public Object[][] getSourceStatus() { //для отображения в autoMode
        int amountOfSources = sourceController.getNumberOfSources();
        Object[][] result = new Object[amountOfSources][8];
        DecimalFormat format = new DecimalFormat("0.00");
        for (int i = 0; i < amountOfSources; i++) {
            result[i] = new Object[]{i + 1, format.format(metricsController.getNumberGeneratedRequests(i)),
                    format.format(metricsController.getFailureProbability(i)), format.format(metricsController.getAverageTimeInSystem(i)),
                    format.format(metricsController.getAverageWaitingTime(i)), format.format(metricsController.getAverageProcessingTime(i)),
                    format.format(metricsController.getDispersionWaitingTime(i)), format.format(metricsController.getDispersionProcessingTime(i))};
        }
        return result;
    }

    public Object[] getCalendarStatus(int index) {
        return calendar[index];
    }//для отображения в stepByStepMode

    private void setCalendarStatus(Action action, Ticket ticket) {
        DecimalFormat format = new DecimalFormat("0.00");
        switch (action) {
            case FINISH ->
                    calendar[currentCalendarAction] = new Object[]{"Device" + deviceController.getFinishDevice().getNumber(),
                            format.format(ticket.getFinishedTime()), "Finished request",
                            metricsController.getAllNumberGeneratedRequests(), metricsController.getAllNumberFailedRequests()};
            case RECEIVE ->
                    calendar[currentCalendarAction] = new Object[]{"Device" + deviceController.getReceiveDevice().getNumber(),
                            format.format(ticket.getReceivedTime()), "Received request",
                            metricsController.getAllNumberGeneratedRequests(), metricsController.getAllNumberFailedRequests()};
            case GENERATE ->
                    calendar[currentCalendarAction] = new Object[]{"Source" + sourceController.getSentSource().getNumber(),
                            format.format(sourceEventTime), "Generated request",
                            metricsController.getAllNumberGeneratedRequests() + 1, metricsController.getAllNumberFailedRequests()};
            case STORE ->
                    calendar[currentCalendarAction] = new Object[]{"Buffer" + bufferController.getStoreBuffer().getNumber(), "",
                            "Store request " + ticket.getRequestNumber()};
            case SENT ->
                    calendar[currentCalendarAction] = new Object[]{"Buffer" + bufferController.getSentBuffer().getNumber(),"",
                            "Sent request " + ticket.getRequestNumber()};
            case FAIL ->
                    calendar[currentCalendarAction] = new Object[]{"Buffer" + bufferController.getStoreBuffer().getNumber(),"",
                            "Failed request " + ticket.getRequestNumber()};
        }
        setBufferStatus();
        this.currentCalendarAction++;
    }

    public Object[][] getDeviceStatus() { //для отображения в autoMode
        int amountOfDevices = deviceController.getNumberOfDevices();
        Object[][] result = new Object[amountOfDevices][2];
        DecimalFormat format = new DecimalFormat("#.##");
        for (int i = 0; i < amountOfDevices; i++) {
            result[i] = new Object[]{i + 1, format.format(metricsController.getUsageRate(i, this.getSystemTime()))};
        }
        return result;
    }

    public Object[][] getBufferStatus(int currentStep) { //для отображения в stepByStepMode
        int numberOfBuffers = bufferController.getNumberOfBuffers();
        Object[][] result = new Object[numberOfBuffers][4];
        for (int i = 0; i < numberOfBuffers; i++) {
            result[i] = buffer[currentStep * numberOfBuffers + i];
        }
        return result;
    }

    public void setBufferStatus() {
        int numberOfBuffers = bufferController.getNumberOfBuffers();
        DecimalFormat format = new DecimalFormat("0.00");
        for (int i = 0; i < numberOfBuffers; i++) {
            if (!bufferController.isEmpty(i)) {
                buffer[currentBufferAction] = new Object[]{i + 1, format.format(bufferController.getReceiveTime(i)),
                        bufferController.getCurrentTicketId(i)};
            } else {
                buffer[currentBufferAction] = new Object[]{i + 1, 0.0, 0};
            }
            currentBufferAction += 1;
        }
    }

    public long getAllNumberOfGeneratedRequests() {//для отображения в stepByStepMode
        return metricsController.getAllNumberGeneratedRequests();
    }

    public long getAmountOfRequests() {
        return this.amountOfTickets;
    }//для отображения в stepByStepMode

    public double getSystemTime() {
        return Double.max(sourceEventTime, deviceEventTime);
    }
}
