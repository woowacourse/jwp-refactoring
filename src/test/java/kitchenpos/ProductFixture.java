package kitchenpos;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import kitchenpos.domain.Product;

public class ProductFixture {
    public static Product createProductWithoutId() {
        final Product product = new Product();
        product.setName("강정치킨");
        product.setPrice(BigDecimal.valueOf(17000));
        return product;
    }

    public static Product createProductWithId(final Long productId) {
        final Product product = new Product();
        product.setName("강정치킨");
        product.setPrice(BigDecimal.valueOf(17000));
        product.setId(productId);
        return product;
    }

    public static Product createProductWithPrice(final BigDecimal price) {
        final Product product = new Product();
        product.setName("강정치킨");
        product.setPrice(price);
        product.setId(1L);
        return product;
    }

    public static List<Product> createProducts() {
        return Arrays.asList(createProductWithId(1L), createProductWithId(2L));
    }
}
