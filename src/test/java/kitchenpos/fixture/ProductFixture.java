package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;

public class ProductFixture {

    private static final Long ID = 1L;
    private static final String NAME = "PRODUCT_NAME";
    private static final BigDecimal PRICE = BigDecimal.TEN;

    public static Product createProduct(Long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static Product createProduct(Long id) {
        return createProduct(id, NAME, PRICE);
    }

    public static ProductRequest createProductRequest() {
        return new ProductRequest(NAME, PRICE);
    }

    public static ProductRequest createProductRequest(BigDecimal price) {
        return new ProductRequest(NAME, price);
    }

    public static ProductResponse createProductResponse() {
        return new ProductResponse(ID, NAME, PRICE);
    }
}
