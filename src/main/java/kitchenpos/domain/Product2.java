package kitchenpos.domain;

import java.math.BigDecimal;

public class Product2 {

  private Long id;
  private String name;
  private BigDecimal price;

  public Product2(final Long id, final String name, final BigDecimal price) {
    this.id = id;
    this.name = name;
    this.price = price;
  }

  public Product2(final String name, final BigDecimal price) {
    this(null, name, price);
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
