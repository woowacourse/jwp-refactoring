package kitchenpos.menu.fixture;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;

public class MenuFixture {

    public static Menu createMenu() {
        final List<MenuProduct> menuProducts = Collections.singletonList(
                new MenuProduct(1L, 2));
        return Menu.builder()
                .id(1L)
                .name("후라이드+후라이드")
                .price(BigDecimal.valueOf(19_000L))
                .menuGroupId(1L)
                .menuProducts(new MenuProducts(menuProducts))
                .build();
    }
}
