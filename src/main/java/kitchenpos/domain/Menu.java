package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Menu {

  private Long id;
  private String name;
  private BigDecimal price;
  private MenuGroup menuGroup;
  private List<MenuProduct> menuProducts;

  public Menu(
      final Long id,
      final String name,
      final BigDecimal price,
      final MenuGroup menuGroup,
      final List<MenuProduct> menuProducts
  ) {
    validatePrice(price);
    this.id = id;
    this.name = name;
    this.price = price;
    this.menuGroup = menuGroup;
    this.menuProducts = menuProducts;
  }

  public Menu(final String name, final BigDecimal price, final MenuGroup menuGroup) {
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

  public List<MenuProduct> getMenuProducts() {
    return menuProducts;
  }
}
