package ba.unsa.etf.cinebh.specification;

import ba.unsa.etf.cinebh.model.address.City;
import ba.unsa.etf.cinebh.model.venue.Venue;
import org.springframework.data.jpa.domain.Specification;

public class VenueSpecification {
  public static Specification<Venue> nameLike(final String contains) {
    return (root, query, builder) -> {
      query.distinct(true);
      return builder.like(builder.lower(root.get("name")), "%" + contains.toLowerCase() + "%");
    };
  }

  public static Specification<Venue> hasProjectionInCity(final City city) {
    return (root, query, builder) -> {
      query.distinct(true);
      return builder.and(root.get("city").in(city));
    };
  }
}
