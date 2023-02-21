package Controllers;

import Entities.Device;
import Entities.Ticket;

import java.util.LinkedList;
import java.util.List;

public class DeviceController {
    private List<Device> devices;
    private Device toReceiveDevice;
    private Device toFinishDevice;
    private MetricsController metricsController;
    private double eventTime;

    public DeviceController(MetricsController statisticsController, int devicesNumber, double a, double b) {
        this.metricsController = statisticsController;
        devices = new LinkedList<>();
        for (int i = 0; i < devicesNumber; i++) {
            devices.add(new Device(i + 1, a, b));
        }
        this.toFinishDevice = devices.get(0);
        this.toReceiveDevice = devices.get(0);
        this.setEventTime(); //изначально устанавливаем максимальное значение,
        // чтобы начать работу программу с источникрв и буфера
    }

    private void chooseToFinishDevice() { //ищем заявку, у которой finishedTime меньше всех
        for (Device device : devices) {
            if (!device.isFree()) {
                if (this.toFinishDevice.isFree()) {
                    this.toFinishDevice = device;
                } else if (this.toFinishDevice.getCurrentRequest().getFinishedTime() > device.getCurrentRequest().getFinishedTime()) {
                    this.toFinishDevice = device;
                }
            }
        }
    }

    private void chooseToReceiveDevice() { //приоритет постановки на обслуживание по номеру прибора
        for (Device device : devices) {
            if (device.isFree()) {
                this.toReceiveDevice = device;
                return;
            }
        }
    }

    private void setEventTime() { //event time - самое раннее время окончание заявки из заявок на приборах
        this.eventTime = Double.MAX_VALUE;
        for (Device device : devices) {
            if (!device.isFree() && device.getCurrentRequest().getFinishedTime() < this.eventTime) {
                this.eventTime = device.getCurrentRequest().getFinishedTime();
            }
        }
    }

    public void receiveRequest(Ticket ticket) {
        this.chooseToReceiveDevice();
        toReceiveDevice.receiveRequest(ticket);
        this.metricsController.getReceived(ticket);
        this.setEventTime();
    }

    public Ticket finishRequest() {
        this.chooseToFinishDevice();
        this.metricsController.getSucceed(toFinishDevice.getCurrentRequest(), toFinishDevice.getNumber());
        Ticket finishedTicket = this.toFinishDevice.finishRequest();
        this.setEventTime();
        return finishedTicket;
    }

    public boolean isFull() {
        for (Device device : devices) {
            if (device.isFree()) {
                return false;
            }
        }
        return true;
    }

    public double getEventTime() {
        return eventTime;
    }

    public int getNumberOfDevices() {
        return devices.size();
    }

    public Device getReceiveDevice() {
        return toReceiveDevice;
    }

    public Device getFinishDevice() {
        return toFinishDevice;
    }
}
