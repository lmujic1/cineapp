package ba.unsa.etf.cineapp.controller.ticket;

import ba.unsa.etf.cineapp.dto.request.auth.PaymentRequest;
import ba.unsa.etf.cineapp.dto.request.ticket.TicketRequest;
import ba.unsa.etf.cineapp.dto.response.NumberOfElementsResponse;
import ba.unsa.etf.cineapp.dto.response.PaymentResponse;
import ba.unsa.etf.cineapp.exception.ResourceNotFoundException;
import ba.unsa.etf.cineapp.model.auth.User;
import ba.unsa.etf.cineapp.model.projection.Projection;
import ba.unsa.etf.cineapp.model.ticket.Ticket;
import ba.unsa.etf.cineapp.service.auth.JWTService;
import ba.unsa.etf.cineapp.service.auth.PaymentService;
import ba.unsa.etf.cineapp.service.auth.UserService;
import ba.unsa.etf.cineapp.service.projection.ProjectionService;
import ba.unsa.etf.cineapp.service.ticket.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/tickets")
public class TicketController {

  @Autowired
  private TicketService ticketService;

  @Autowired
  private PaymentService paymentService;

  @Autowired
  private UserService userService;

  @Autowired
  private JWTService jwtService;

  @Autowired
  private ProjectionService projectionService;

  @GetMapping("/user")
  public ResponseEntity<List<Ticket>> getTicketsForUser(@RequestHeader("Authorization") String token) {
    String username = jwtService.getUsernameFromToken(token.replace("Bearer ", ""));
    User user = userService.getUserByUsername(username);
    return ResponseEntity.ok(ticketService.getTickets(user));
  }


  @GetMapping("/projection/{id}")
  public ResponseEntity<List<Ticket>> getTicketsForProjection(@PathVariable long id, Date date) {
    Projection p = projectionService.findById(id);
    return ResponseEntity.ok(ticketService.getAllTicketsForProjection(p, date));
  }

  @GetMapping("/count-elements")
  public ResponseEntity<NumberOfElementsResponse> getNumberOfElements(@RequestHeader("Authorization") String token) {
    String username = jwtService.getUsernameFromToken(token.replace("Bearer ", ""));
    User user = userService.getUserByUsername(username);
    return ResponseEntity.ok(ticketService.getNumberOfElements(user));
  }

  @GetMapping("/user/upcoming-projections")
  public ResponseEntity<List<Ticket>> getUpcomingPurchasesForUser(@RequestHeader("Authorization") String token) {
    String username = jwtService.getUsernameFromToken(token.replace("Bearer ", ""));
    User user = userService.getUserByUsername(username);
    return ResponseEntity.ok(ticketService.getUpcomingPurchases(user));
  }

  @GetMapping("/user/past-projections")
  public ResponseEntity<List<Ticket>> getPastPurchasesForUser(@RequestHeader("Authorization") String token) {
    String username = jwtService.getUsernameFromToken(token.replace("Bearer ", ""));
    User user = userService.getUserByUsername(username);
    return ResponseEntity.ok(ticketService.getPastPurchases(user));
  }


  @GetMapping("/user/count")
  public ResponseEntity<Long> getTicketsNumber(@RequestHeader("Authorization") String token) {
    String username = jwtService.getUsernameFromToken(token.replace("Bearer ", ""));
    User user = userService.getUserByUsername(username);
    return ResponseEntity.ok(ticketService.getTicketsNumber(user));
  }


  @PutMapping("/{id}/buy-ticket")
  public ResponseEntity<String> buyTicket(@PathVariable long id) {
    Ticket ticket = ticketService.findById(id);
    return ResponseEntity.ok(ticketService.makePurchase(ticket));
  }

  @PostMapping
  public ResponseEntity<String> createTicket(@RequestBody TicketRequest request) {
    User user;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
      user = (User) authentication.getPrincipal();
    } else {
      throw new ResourceNotFoundException("Invalid token");
    }
    try {
      String response = ticketService.create(request, user);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
  }

  @PostMapping("/create-payment-intent")
  public ResponseEntity<PaymentResponse> createPaymentIntent(@RequestBody PaymentRequest request) {
    try {
      PaymentResponse response = paymentService.createPaymentIntent(request);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteTicket(@PathVariable long id, @RequestHeader("Authorization") String token) {
    String username = jwtService.getUsernameFromToken(token.replace("Bearer ", ""));
    User user = userService.getUserByUsername(username);
    Ticket ticket = ticketService.findById(id);
    if (!user.equals(ticket.getUser())) ResponseEntity.badRequest().body("Error: Invalid user!");
    return ResponseEntity.ok(ticketService.cancelReservation(ticket));
  }
}

