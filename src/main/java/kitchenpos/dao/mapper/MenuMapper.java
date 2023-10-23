package kitchenpos.dao.mapper;

import java.util.List;
import kitchenpos.dao.entity.MenuEntity;
import kitchenpos.dao.entity.MenuProductEntity;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

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
