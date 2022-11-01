package kitchenpos.application.fixture;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import kitchenpos.domain.MenuProduct;

public class MenuProductFixtures {

    public static final MenuProduct generateMenuProduct(final Long productId, final long quantity) {
        return generateMenuProduct(null, null, productId, quantity, BigDecimal.valueOf(16000));
    }

    public static final MenuProduct generateMenuProduct(final Long seq,
                                                        final Long menuId,
                                                        final Long productId,
                                                        final long quantity,
                                                        final BigDecimal price) {
        try {
            Constructor<MenuProduct> constructor = MenuProduct.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            MenuProduct menuProduct = constructor.newInstance();
            Class<? extends MenuProduct> clazz = menuProduct.getClass();

            Field seqField = clazz.getDeclaredField("seq");
            seqField.setAccessible(true);
            seqField.set(menuProduct, seq);

            Field menIdField = clazz.getDeclaredField("menuId");
            menIdField.setAccessible(true);
            menIdField.set(menuProduct, menuId);

            Field productIdField = clazz.getDeclaredField("productId");
            productIdField.setAccessible(true);
            productIdField.set(menuProduct, productId);

            Field quantityField = clazz.getDeclaredField("quantity");
            quantityField.setAccessible(true);
            quantityField.set(menuProduct, quantity);

            Field priceField = clazz.getDeclaredField("price");
            priceField.setAccessible(true);
            priceField.set(menuProduct, price);

            return menuProduct;
        } catch (final Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
