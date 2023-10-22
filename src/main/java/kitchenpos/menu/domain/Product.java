package kitchenpos.menu.domain;

public class Product {

  private final Long id;
  private final String name;
  private final Price price;

  public Product(final Long id, final String name, final Price price) {
    validatePrice(price);
    this.id = id;
    this.name = name;
    this.price = price;
  }


  public Product(final String name, final Price price) {
    this(null, name, price);
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Price getPrice() {
    return price;
  }

  private void validatePrice(final Price price) {
    if (price.isNull() || price.isLessThan(Price.ZERO)) {
      throw new IllegalArgumentException();
    }
  }
}
