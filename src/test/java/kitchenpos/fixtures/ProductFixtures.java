package kitchenpos.fixtures;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.common.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.service.dto.ProductRequest;

public class ProductFixtures {

    private static final String PRODUCT_NAME = "기본 상품명";
    private static final int PRICE = 10000;
    private static final long PRODUCT_ID = 1L;

    public static Product createProduct(
        Long id,
        String name,
        long price
    ) {
        return new Product(id, name, new Price(BigDecimal.valueOf(price)));
    }

    public static Product createProduct() {
        return createProduct(PRODUCT_ID, PRODUCT_NAME, PRICE);
    }

    public static List<Product> createProducts() {
        return Arrays.asList(createProduct(), createProduct(2L, PRODUCT_NAME, PRICE));
    }

    public static ProductRequest createProductRequest(long price) {
        Product product = createProduct(PRODUCT_ID, PRODUCT_NAME, price);
        return new ProductRequest(product.getName(), product.getPrice());
    }

    public static ProductRequest createProductRequest() {
        Product product = createProduct();
        return new ProductRequest(product.getName(), product.getPrice());
    }
}
