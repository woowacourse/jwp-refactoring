package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product 뿌링클_19000 = generateNormal("뿌링클", 19000);
    public static Product 간지치킨_20000 = generateNormal("간지치킨", 20000);
    public static Product 뿌링클_NULL = generateNullPrice("뿌링클");
    public static Product 뿌링클_INVALID = generateInvalidPrice("뿌링클");

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
