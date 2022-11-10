package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.request.ProductRequest;

public class ProductFixture {

    public static ProductRequest 뿌링클_19000_REQUEST = generateProductRequest("뿌링클", BigDecimal.valueOf(19000));
    public static ProductRequest 맛초킹_20000_REQUEST = generateProductRequest("맛초킹", BigDecimal.valueOf(20000));
    public static ProductRequest 뿌링클_NULL_REQUEST = generateProductRequest("뿌링클", null);
    public static ProductRequest 뿌링클_UNDER_ZERO_REQUEST = generateProductRequest("뿌링클", BigDecimal.valueOf(-1));

    public static Product 사이다 = generateProduct("사이다", BigDecimal.valueOf(1000));
    public static Product 뿌링클 = generateProduct("뿌링클", BigDecimal.valueOf(19000));
    public static Product 맛초킹 = generateProduct("맛초킹", BigDecimal.valueOf(19000));
    public static Product 맛초킹_저장안됨 = generateProductWithId(-1L, "맛초킹", BigDecimal.valueOf(19000));

    public static ProductRequest generateProductRequest(final String productName, final BigDecimal productPrice) {
        return new ProductRequest(productName, productPrice);
    }

    public static Product generateProduct(final String productName, final BigDecimal productPrice) {
        return new Product(productName, new Price(productPrice));
    }

    public static Product generateProductWithId(final Long id,
                                                final String productName,
                                                final BigDecimal productPrice) {
        return new Product(id, productName, new Price(productPrice));
    }
}
