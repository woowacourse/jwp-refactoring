package kitchenpos;

import java.math.BigDecimal;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;

public class TestFixtures {

    public static Product createProduct() {
        return Product.builder()
                .id(1L)
                .name("상품이름")
                .price(BigDecimal.TEN)
                .build();
    }

    public static MenuGroup createMenuGroup(){
        return MenuGroup.builder()
                .id(1L)
                .name("메뉴그룹이름")
                .build();
    }
}
