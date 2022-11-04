package kitchenpos.common.fixture;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import kitchenpos.product.Product;

public class ProductFixtures {

    public static final Product generateProduct(final String name) {
        return generateProduct(null, name, BigDecimal.valueOf(16000));
    }

    public static final Product generateProduct(final String name, final BigDecimal price) {
        return generateProduct(null, name, price);
    }

    public static final Product generateProduct(final Long id, final String name, final BigDecimal price) {

        try {
            Constructor<Product> constructor = Product.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            Product product = constructor.newInstance();
            Class<? extends Product> clazz = product.getClass();

            Field idField = clazz.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(product, id);

            Field nameField = clazz.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(product, name);

            Field priceField = clazz.getDeclaredField("price");
            priceField.setAccessible(true);
            priceField.set(product, price);

            return product;
        } catch (final Exception e) {
            throw new RuntimeException();
        }
    }
}
