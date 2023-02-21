package Controllers;

import Entities.Ticket;
import Entities.Source;

import java.util.LinkedList;
import java.util.List;

public class SourceController {
    private List<Source> sources;
    private Source toSentSource;
    private double eventTime;
    private MetricsController metricsController;

    public SourceController(MetricsController statisticsController, int sourcesNumber, double lambda) {
        this.metricsController = statisticsController;
        this.sources = new LinkedList<>();
        for (int i = 0; i < sourcesNumber; i++) {
            sources.add(new Source(i + 1, lambda));
        }
        this.toSentSource = sources.get(0);
        this.chooseSource();
        this.setEventTime();
    }

    public void chooseSource() { //ищем заявку, у которой createdTime меньше всех
        for (Source source : sources) {
            if (this.toSentSource.getCurrentTicket().getCreationTime() > source.getCurrentTicket().getCreationTime()) {
                this.toSentSource = source;
            }
        }
    }

    private void setEventTime() { //event time - самое раннее время создания заявки из заявок на источниках
        this.eventTime = this.toSentSource.getCurrentTicket().getCreationTime();
    }

    public Ticket sendRequest() {
        Ticket ticket = this.toSentSource.sendTicket();
        this.chooseSource();
        this.setEventTime();
        this.metricsController.getGenerated(toSentSource.getCurrentTicket());
        return ticket;
    }

    public double getEventTime() {
        return eventTime;
    }

    public Source getSentSource() {
        return toSentSource;
    }

    public int getNumberOfSources() {
        return sources.size();
    }
}
