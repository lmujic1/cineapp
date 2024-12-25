package ba.unsa.etf.cineapp.dto.request.ticket;

import ba.unsa.etf.cineapp.model.ticket.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequest {
  private Long projectionId;
  private List<String> seats;
  private Date date;
  private Integer price;
  private Type type;
}

