package kitchenpos;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class TestFixtures {

    public static Product createProduct() {
        return Product.builder()
                .id(1L)
                .name("상품이름")
                .price(BigDecimal.valueOf(1000))
                .build();
    }

    public static MenuGroup createMenuGroup() {
        return MenuGroup.builder()
                .id(1L)
                .name("메뉴그룹이름")
                .build();
    }

    public static MenuProduct createMenuProduct() {
        return MenuProduct.builder()
                .id(1L)
                .menuId(1L)
                .productId(1L)
                .quantity(1L)
                .build();
    }

    public static Menu createMenu() {
        return Menu.builder()
                .id(1L)
                .name("메뉴이름")
                .price(BigDecimal.valueOf(1000))
                .menuGroupId(1L)
                .menuProducts(Collections.singletonList(createMenuProduct()))
                .build();
    }
}
