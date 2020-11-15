package kitchenpos.application.fixture;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Product;

public class ProductFixture {
    public static Product createWithOutId(BigDecimal price) {
        Product product = new Product();
        product.setId(null);
        product.setName("TEST_PRODUCT");
        product.setPrice(price);

        return product;
    }

    public static List<Product> getProducts(int... price) {
        return Arrays.stream(price)
            .mapToObj(p -> createWithOutId(BigDecimal.valueOf(p)))
            .collect(Collectors.toList());
    }
}
