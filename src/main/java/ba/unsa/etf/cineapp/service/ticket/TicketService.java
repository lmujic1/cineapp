package ba.unsa.etf.cineapp.service.ticket;

import ba.unsa.etf.cineapp.dto.request.other.EmailRequest;
import ba.unsa.etf.cineapp.dto.request.ticket.TicketRequest;
import ba.unsa.etf.cineapp.dto.response.NumberOfElementsResponse;
import ba.unsa.etf.cineapp.exception.ResourceNotFoundException;
import ba.unsa.etf.cineapp.model.auth.User;
import ba.unsa.etf.cineapp.model.projection.Projection;
import ba.unsa.etf.cineapp.model.ticket.Ticket;
import ba.unsa.etf.cineapp.model.ticket.Type;
import ba.unsa.etf.cineapp.repository.projection.ProjectionRepository;
import ba.unsa.etf.cineapp.repository.ticket.TicketRepository;
import ba.unsa.etf.cineapp.service.other.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TicketService {

  private TicketRepository ticketRepository;

  @Autowired
  private ProjectionRepository projectionRepository;

  @Autowired
  private EmailService emailService;

  public List<Ticket> getTickets(final User user){
    return ticketRepository.findTicketsByUser(user);
  }

  public List<Ticket> getUpcomingPurchases(final User user) { return  ticketRepository.findUpcomingBuysByUser(user); }

  public List<Ticket> getPastPurchases(final User user) { return  ticketRepository.findPastBuysByUser(user); }

  public Long getTicketsNumber(final User user) {
    return ticketRepository.countTickets(user);
  }

  public NumberOfElementsResponse getNumberOfElements(final User user) {
    int upcoming = (int) ticketRepository.countUpcoming(user);
    int archived = (int) ticketRepository.countPast(user);
    return new NumberOfElementsResponse(0, 0, upcoming, archived);
  }

  public Ticket findById(final Long id){
    return ticketRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket with provided id not found!"));
  }

  public String makePurchase(final Ticket ticket) {
    ticket.setType(Type.BUY);
    ticketRepository.save(ticket);
    String ticketDetails = "Movie: " + ticket.getProjection().getMovie().getName() + "\nDate: " + ticket.getDate() + "\nTime: " + ticket.getProjection().getTime().toString().substring(0, 5) + "\nVenue: " +
                                ticket.getProjection().getVenue().getName() + ", " + ticket.getProjection().getVenue().getStreet() + ticket.getProjection().getVenue().getStreetNumber() + "\nSeats: " + String.join(", ", ticket.getSeats());
    return emailService.sendEmail(
        new EmailRequest(
            ticket.getUser().getEmail(),
            "Payment Confirmation",
            "Hi " + ticket.getUser().getFirstName() + " " + ticket.getUser().getLastName() + ", \n\nWe are pleased to confirm your payment for the following details:\n\n" +
            ticketDetails +
            "\n\nThank you for choosing us, we look forward to providing you with an enjoyable movie experience.\n" +
            "\n -The Cinebh Team"));
  }

  public String cancelReservation(final Ticket ticket){
    ticketRepository.delete(ticket);
    return "Ticket successfully canceled!";
  }

  public String create(final TicketRequest request, final User user) {
    Projection projection = projectionRepository.findById(request.getProjectionId())
        .orElseThrow(() -> new ResourceNotFoundException("Invalid projection id!"));

    List<Ticket> tickets = ticketRepository.findTicketsByProjectionAndDate(projection, request.getDate());

    if (request.getSeats().isEmpty()) {
      throw new IllegalArgumentException("Please select seats!");
    }

    Set<String> finalReservedSeats = tickets.stream()
        .flatMap(ticket -> ticket.getSeats().stream())
        .collect(Collectors.toSet());

    boolean seatsAvailable = request.getSeats().stream()
        .noneMatch(seat -> finalReservedSeats.contains(seat));

    if (!seatsAvailable) {
      throw new IllegalArgumentException("Seats are already reserved!");
    }

    Ticket ticket = new Ticket(request.getSeats(), request.getDate(),
        request.getPrice(), request.getType(), user, projection);

    ticketRepository.save(ticket);
    projectionRepository.save(projection);

    String ticketDetails = "Movie: " + projection.getMovie().getName() + "\nDate: " + request.getDate() +
                                "\nTime: " + projection.getTime().toString().substring(0, 5) + "\nVenue: " +
                                projection.getVenue().getName() + ", " + projection.getVenue().getStreet() +
                                projection.getVenue().getStreetNumber() + "\nSeats: " + String.join(", ", request.getSeats());

    if (request.getType().equals(Type.RESERVATION)) {
      return emailService.sendEmail(new EmailRequest(
          user.getEmail(),
          "Ticket Confirmation",
          "Hi " + user.getFirstName() + " " + user.getLastName() + ", \n\nWe are pleased to confirm your ticket for the following details:\n\n" +
          ticketDetails +
          "\n\nPlease note that your ticket will expire if the tickets are not purchased at least one hour before the showtime. \nTo ensure your seats are secured, we recommend completing your purchase well in advance." +
          "\n\nThank you for choosing us, we look forward to providing you with an enjoyable movie experience.\n" +
          "\n -The Cinebh Team"));
    } else {
      return emailService.sendEmail(new EmailRequest(
          user.getEmail(),
          "Payment Confirmation",
          "Hi " + user.getFirstName() + " " + user.getLastName() + ", \n\nWe are pleased to confirm your payment for the following details:\n\n" +
          ticketDetails +
          "\n\nThank you for choosing us, we look forward to providing you with an enjoyable movie experience.\n" +
          "\n -The Cinebh Team"));
    }
  }

  public List<Ticket> getTicketsForProjection(final Projection projection, final Date date) {
    return ticketRepository.findTicketsByProjectionAndDate(projection, date);
  }

  public List<Ticket> getAllTicketsForProjection(final Projection projection, final Date date) {
    return ticketRepository.findAllTicketsByProjectionAndDate(projection, date);
  }
}

