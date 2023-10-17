package kitchenpos.dao.entity;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.MenuProduct;

public class MenuEntity {

  private Long id;
  private String name;
  private BigDecimal price;
  private Long menuGroupId;

  public MenuEntity(
      final Long id,
      final String name,
      final BigDecimal price,
      final Long menuGroupId
  ) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.menuGroupId = menuGroupId;
  }

  public MenuEntity(final String name, final BigDecimal price, final Long menuGroupId) {
    this.name = name;
    this.price = price;
    this.menuGroupId = menuGroupId;
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
}
