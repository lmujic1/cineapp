package ba.unsa.etf.cinebh.repository.ticket;

import ba.unsa.etf.cinebh.model.auth.User;
import ba.unsa.etf.cinebh.model.projection.Projection;
import ba.unsa.etf.cinebh.model.ticket.Ticket;
import ba.unsa.etf.cinebh.model.ticket.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
  @Transactional
  @Modifying
  @Query("DELETE FROM Ticket r WHERE r.projection = :projection AND r.type = :type")
  void deleteByProjectionAndType(final Projection projection, final Type type);

  @Query("SELECT r FROM Ticket r WHERE r.user = :user AND r.type = 'RESERVATION'")
  List<Ticket> findTicketsByUser(final User user);

  @Query("SELECT r FROM Ticket r WHERE r.projection = :projection AND r.date = :date AND r.type = 'RESERVATION'")
  List<Ticket> findTicketsByProjectionAndDate(final Projection projection, Date date);

  @Query("SELECT r FROM Ticket r WHERE r.user = :user AND r.type = 'BUY' AND (r.date > CURRENT_DATE OR (r.date = CURRENT_DATE AND r.projection.time >= CURRENT_TIME))")
  List<Ticket> findUpcomingBuysByUser(final User user);

  @Query("SELECT r FROM Ticket r WHERE r.user = :user AND r.type = 'BUY' AND (r.date < CURRENT_DATE OR (r.date = CURRENT_DATE AND r.projection.time < CURRENT_TIME))")
  List<Ticket> findPastBuysByUser(final User user);

  @Query("SELECT COUNT(r) FROM Ticket r WHERE r.user = :user AND r.type = 'RESERVATION'")
  long countTickets(final User user);

  @Query("SELECT COUNT(r) FROM Ticket r WHERE r.user = :user AND r.type = 'BUY' AND (r.date > CURRENT_DATE OR (r.date = CURRENT_DATE AND r.projection.time >= CURRENT_TIME))")
  long countUpcoming(final User user);

  @Query("SELECT COUNT(r) FROM Ticket r WHERE r.user = :user AND r.type = 'BUY' AND (r.date < CURRENT_DATE OR (r.date = CURRENT_DATE AND r.projection.time < CURRENT_TIME))")
  long countPast(final User user);

  @Query("SELECT r FROM Ticket r WHERE r.date=CURRENT_DATE() AND r.type='RESERVATION'")
  List<Ticket> getTodaysTickets();
}

