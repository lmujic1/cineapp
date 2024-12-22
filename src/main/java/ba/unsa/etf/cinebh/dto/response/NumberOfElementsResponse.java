package ba.unsa.etf.cinebh.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NumberOfElementsResponse {
  private Integer drafts;
  private Integer currently;
  private Integer upcoming;
  private Integer archived;
}

