package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class Menu2 {

  private Long id;
  private String name;
  private BigDecimal price;
  private MenuGroup menuGroup;
  private List<MenuProduct> menuProducts;

  public Menu2(
      final Long id,
      final String name,
      final BigDecimal price,
      final MenuGroup menuGroup
  ) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.menuGroup = menuGroup;
  }

  public Menu2(final String name, final BigDecimal price, final MenuGroup menuGroup) {
    this.name = name;
    this.price = price;
    this.menuGroup = menuGroup;
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
