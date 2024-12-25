package ba.unsa.etf.cineapp.scheduled;

import ba.unsa.etf.cineapp.model.ticket.Ticket;
import ba.unsa.etf.cineapp.repository.projection.ProjectionRepository;
import ba.unsa.etf.cineapp.repository.ticket.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@Slf4j
public class TicketScheduler {
  private final TicketRepository ticketRepository;
  private final ProjectionRepository projectionRepository;

  @Autowired
  public TicketScheduler(TicketRepository ticketRepository, ProjectionRepository projectionRepository) {
    this.ticketRepository = ticketRepository;
    this.projectionRepository = projectionRepository;
  }

  @Scheduled(cron = "0 0/15 * * * *")
  public void deleteReservedSeatsForExpiredProjections() {
    LocalTime currentHour = LocalTime.now().truncatedTo(ChronoUnit.MINUTES).plusHours(1);
    log.info("Task for deleting reserved seats started at {}", LocalTime.now().truncatedTo(ChronoUnit.MINUTES));

    List<Ticket> reservations = ticketRepository.getTodaysTickets();

    for (Ticket r : reservations) {
      Time projectionTime = r.getProjection().getTime();
      LocalTime localProjectionTime = projectionTime.toLocalTime();

      if (localProjectionTime.isBefore(currentHour) || localProjectionTime.equals(currentHour)) {
        ticketRepository.delete(r);
      }
    }
  }
}
