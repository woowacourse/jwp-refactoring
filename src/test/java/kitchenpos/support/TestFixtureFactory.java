package kitchenpos.support;

import java.math.BigDecimal;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;

public class TestFixtureFactory {

    private TestFixtureFactory() {
    }

    public static Product 상품을_생성한다(final String name, final BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static MenuGroup 메뉴_그룹을_생성한다(final String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
}
