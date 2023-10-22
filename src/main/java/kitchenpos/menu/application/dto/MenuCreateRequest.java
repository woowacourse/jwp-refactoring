package kitchenpos.menu.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Price;

public class MenuCreateRequest {

  private String name;
  private BigDecimal price;
  private Long menuGroupId;
  private List<MenuProductCreateRequest> menuProducts;

  public MenuCreateRequest(final String name, final BigDecimal price, final Long menuGroupId,
      final List<MenuProductCreateRequest> menuProducts) {
    this.name = name;
    this.price = price;
    this.menuGroupId = menuGroupId;
    this.menuProducts = menuProducts;
  }

  public MenuCreateRequest() {
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

  public List<MenuProductCreateRequest> getMenuProducts() {
    return menuProducts;
  }

  public Menu toMenu() {
    return new Menu(
        name,
        new Price(price),
        menuGroupId,
        menuProducts.stream()
            .map(MenuProductCreateRequest::toMenuProduct)
            .collect(Collectors.toList())
    );
  }
}
