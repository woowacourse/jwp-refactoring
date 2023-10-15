package kitchenpos.support;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class FixtureFactory {

    public static Product forSaveProduct(final String name, final BigDecimal price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static Product savedProduct(final Long id, final String name, final BigDecimal price) {
        final Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static MenuGroup forSaveMenuGroup(final String groupName) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(groupName);
        return menuGroup;
    }

    public static MenuGroup savedMenuGroup(final Long id, final String groupName) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(groupName);
        return menuGroup;
    }
}
