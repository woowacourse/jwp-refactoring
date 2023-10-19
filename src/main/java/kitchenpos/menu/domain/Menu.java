package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.product.domain.Price;

public class Menu {

  private final Long id;
  private final String name;
  private final Price price;
  private final Long menuGroupId;
  private final List<MenuProduct> menuProducts;

  public Menu(final Long id, final String name, final Price price, final Long menuGroupId,
      final List<MenuProduct> menuProducts) {
    validatePrice(price);
    this.id = id;
    this.name = name;
    this.price = price;
    this.menuGroupId = menuGroupId;
    this.menuProducts = menuProducts;
  }

  public Menu(final String name, final Price price, final Long menuGroupId,
      final List<MenuProduct> menuProducts) {
    this(null, name, price, menuGroupId, menuProducts);
  }

  public Menu(final Long id, final String name, final Price price, final Long menuGroupId) {
    this(id, name, price, menuGroupId, new ArrayList<>());
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

  public Long getMenuGroupId() {
    return menuGroupId;
  }

  public List<MenuProduct> getMenuProducts() {
    return menuProducts;
  }

  private void validatePrice(final Price price) {
    if (price.isNull() || price.isLessThan(Price.ZERO)) {
      throw new IllegalArgumentException();
    }
  }
}
