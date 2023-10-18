package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Menu {

  private final Long id;
  private final String name;
  private final BigDecimal price;
  private final Long menuGroupId;
  private final List<MenuProduct> menuProducts;

  public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
      final List<MenuProduct> menuProducts) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.menuGroupId = menuGroupId;
    this.menuProducts = menuProducts;
  }

  public Menu(final String name, final BigDecimal price, final Long menuGroupId,
      final List<MenuProduct> menuProducts) {
    this(null, name, price, menuGroupId, menuProducts);
  }

  public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId) {
    this(id, name, price, menuGroupId, new ArrayList<>());
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

  public Long getMenuGroupId() {
    return menuGroupId;
  }

  public List<MenuProduct> getMenuProducts() {
    return menuProducts;
  }

}
