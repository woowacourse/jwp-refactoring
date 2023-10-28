package kitchenpos.menu.infrastructure.persistence;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.product.domain.Product;

public class MenuMapper {

  private MenuMapper() {
  }

  public static MenuProduct mapToMenuProduct(
      final MenuProductEntity entity,
      final Product product
  ) {
    return new MenuProduct(
        entity.getSeq(),
        product,
        entity.getQuantity()
    );
  }

  public static Menu mapToMenu(
      final MenuEntity entity,
      final MenuGroup menuGroup,
      final List<MenuProduct> menuProducts
  ) {
    return new Menu(
        entity.getId(),
        entity.getName(),
        entity.getPrice(),
        menuGroup,
        menuProducts
    );
  }
}
