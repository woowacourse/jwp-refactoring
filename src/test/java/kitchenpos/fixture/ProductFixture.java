package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;

public class ProductFixture {

    public static ProductRequest 뿌링클_19000_REQUEST = generateProductRequest("뿌링클", BigDecimal.valueOf(19000));
    public static ProductRequest 맛초킹_20000_REQUEST = generateProductRequest("맛초킹", BigDecimal.valueOf(20000));
    public static ProductRequest 뿌링클_NULL_REQUEST = generateProductRequest("뿌링클", null);
    public static ProductRequest 뿌링클_UNDER_ZERO_REQUEST = generateProductRequest("뿌링클", BigDecimal.valueOf(-1));
    public static Product 뿌링클_19000 = generateNormal("뿌링클", 19000);
    public static Product 간지치킨_20000 = generateNormal("간지치킨", 20000);
    public static Product 뿌링클_NULL = generateNullPrice("뿌링클");
    public static Product 뿌링클_INVALID = generateInvalidPrice("뿌링클");

    public static ProductRequest generateProductRequest(final String productName, final BigDecimal productPrice) {
        return new ProductRequest(productName, productPrice);
    }

    public static Product generateProduct(final String productName, final int productPrice) {
        return new Product(productName, BigDecimal.valueOf(productPrice));
    }

    public static Product generateProductWithId(final String productName, final int productPrice, final Long id) {
        return new Product(id, productName, BigDecimal.valueOf(productPrice));
    }

    public static Product generateNormal(final String productName, final int productPrice) {
        return new Product(productName, BigDecimal.valueOf(productPrice));
    }

    private static Product generateNullPrice(final String productName) {
        return new Product(productName, null);
    }

    private static Product generateInvalidPrice(final String productName) {
        return new Product(productName, BigDecimal.valueOf(-1));
    }
}
