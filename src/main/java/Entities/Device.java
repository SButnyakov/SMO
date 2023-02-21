package Entities;

public class Device {

    private int number;

    private double a;
    private double b;
    private double prevProcessTime;
    private Ticket currentRequest;

    public Device(int number, double a, double b) {
        this.a = a;
        this.b = b;
        this.number = number;

        this.prevProcessTime = 0;
    }

    private double distributionLaw() {return a + (b - a) * (Math.random());    }

    public void receiveRequest(Ticket request) {
        double deltaTime = distributionLaw();
        System.out.println("DEVICE: device number - " + this.number
                + " request number - " + request.getRequestNumber() + " received");
        System.out.println("set to: " + (deltaTime + prevProcessTime));
        if (this.prevProcessTime > request.getCreationTime()) {
            // Т.е. девайс освободился, принял с буффера
            request.setFinishedTime(prevProcessTime + deltaTime);
            request.setReceivedTime(prevProcessTime);
        } else {
            // Т.е. не стоял на буфферах
            request.setFinishedTime(request.getCreationTime() + deltaTime);
            request.setReceivedTime(request.getCreationTime());
        }

        this.currentRequest = request;
    }

    public Ticket finishRequest() {
        // Вызов метода, когда освободился прибор и его уже занесли в события
        Ticket requestToFinish = currentRequest;
        System.out.println("DEVICE: device number - " + this.number +
                " request number - " + currentRequest.getRequestNumber() + "finished");
        this.prevProcessTime = this.currentRequest.getFinishedTime();
        this.currentRequest = null;
        return requestToFinish;
    }

    public boolean isFree() {
        return currentRequest == null;
    }

    public Ticket getCurrentRequest() {
        return currentRequest;
    }

    public int getNumber() {
        return number;
    }
}
