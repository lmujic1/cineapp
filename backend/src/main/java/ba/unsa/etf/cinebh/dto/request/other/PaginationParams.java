package ba.unsa.etf.cinebh.dto.request.other;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationParams {
  private Integer page;
  private Integer size;
}

