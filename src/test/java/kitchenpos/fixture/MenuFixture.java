package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {

  public static Menu 만냥치킨_2마리() {
    final MenuProduct menuProduct = new MenuProduct();
    menuProduct.setProductId(1L);
    menuProduct.setQuantity(2L);

    final Menu menu = new Menu();
    menu.setName("만냥치킨+만냥치킨");
    menu.setPrice(BigDecimal.valueOf(19000));
    menu.setMenuGroupId(1L);
    menu.setMenuProducts(List.of(menuProduct));
    return menu;
  }

  public static Menu 만냥치킨_2마리_잘못된_상품() {
    final MenuProduct menuProduct = new MenuProduct();
    menuProduct.setProductId(999L);
    menuProduct.setQuantity(2L);

    final Menu menu = new Menu();
    menu.setName("후라이드+후라이드");
    menu.setPrice(BigDecimal.valueOf(19000));
    menu.setMenuGroupId(1L);
    menu.setMenuProducts(List.of(menuProduct));
    return menu;
  }

}
