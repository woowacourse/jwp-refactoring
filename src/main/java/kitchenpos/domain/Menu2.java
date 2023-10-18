package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Menu2 {

  private Long id;
  private String name;
  private BigDecimal price;
  private MenuGroup menuGroup;
  private List<MenuProduct2> menuProducts;

  public Menu2(
      final Long id,
      final String name,
      final BigDecimal price,
      final MenuGroup menuGroup,
      final List<MenuProduct2> menuProducts
  ) {
    validatePrice(price);
    this.id = id;
    this.name = name;
    this.price = price;
    this.menuGroup = menuGroup;
    this.menuProducts = menuProducts;
  }

  public Menu2(final String name, final BigDecimal price, final MenuGroup menuGroup) {
    this(null, name, price, menuGroup, null);
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

  public MenuGroup getMenuGroup() {
    return menuGroup;
  }

  public List<MenuProduct2> getMenuProducts() {
    return menuProducts;
  }
}
