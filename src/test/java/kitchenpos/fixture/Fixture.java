package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;

public class Fixture {

    public static Product 상품_강정치킨() {
        Product product = new Product();
        product.setName("강정치킨");
        product.setPrice(BigDecimal.valueOf(17000));

        return product;
    }

    public static MenuGroup 메뉴_그룹_추천상품() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천상품");
        return menuGroup;
    }

}
