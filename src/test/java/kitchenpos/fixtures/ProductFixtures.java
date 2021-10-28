package kitchenpos.fixtures;

import java.math.BigDecimal;
import kitchenpos.application.dto.ProductRequest;
import kitchenpos.domain.Product;

public class ProductFixtures {

    private static final String PRODUCT_NAME = "기본 상품명";
    private static final int PRICE = 10000;
    private static final long PRODUCT_ID = 1L;

    public static Product createProduct(
        Long id,
        String name,
        long price
    ) {
        return new Product(id, name, BigDecimal.valueOf(price * 100, 2));
    }

    public static Product createProduct() {
        return createProduct(PRODUCT_ID, PRODUCT_NAME, PRICE);
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
