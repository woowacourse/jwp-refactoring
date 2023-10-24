package kitchenpos.menu.application.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;

public class MenuQueryResponse {

  private Long id;
  private String name;
  private BigDecimal price;
  private Long menuGroupId;
  private List<MenuProductQueryResponse> menuProducts;

  public MenuQueryResponse(final Long id, final String name, final BigDecimal price,
      final Long menuGroupId,
      final List<MenuProductQueryResponse> menuProducts) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.menuGroupId = menuGroupId;
    this.menuProducts = menuProducts;
  }

  public MenuQueryResponse() {
  }

  public static MenuQueryResponse from(final Menu menu) {
    return new MenuQueryResponse(
        menu.getId(),
        menu.getName(),
        menu.getPrice().getValue(),
        menu.getMenuGroupId(),
        menu.getMenuProducts()
            .stream()
            .map(MenuProductQueryResponse::from)
            .collect(Collectors.toList()));
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

  public List<MenuProductQueryResponse> getMenuProducts() {
    return menuProducts;
  }

}
