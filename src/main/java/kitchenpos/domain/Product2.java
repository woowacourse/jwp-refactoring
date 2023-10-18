package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Product2 {

  private Long id;
  private String name;
  private BigDecimal price;

  public Product2(final Long id, final String name, final BigDecimal price) {
    validatePrice(price);
    this.id = id;
    this.name = name;
    this.price = price;
  }

  public Product2(final String name, final BigDecimal price) {
    this(null, name, price);
  }

  private void validatePrice(final BigDecimal price) {
    if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException();
    }
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getPrice() {
    return price;
  }
}
