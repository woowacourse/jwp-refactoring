package kitchenpos.ui;

import java.math.BigDecimal;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;

public class DomainFixture {

    public static Product getProduct() {
        final Product product = new Product();
        product.setId(1L);
        product.setName("productName");
        product.setPrice(BigDecimal.valueOf(1000L));
        return product;
    }

    public static MenuGroup getMenuGroup() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("menuGroup");
        return menuGroup;
    }

}
