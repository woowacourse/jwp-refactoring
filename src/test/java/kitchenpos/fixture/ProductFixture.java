package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product 뿌링클_19000 = generateNormal("뿌링클", 19000);
    public static Product 간지치킨_20000 = generateNormal("간지치킨", 20000);
    public static Product 뿌링클_NULL = generateNullPrice("뿌링클");
    public static Product 뿌링클_INVALID = generateInvalidPrice("뿌링클");

    public static Product generateProduct(final String productName, final int productPrice) {
        Product product = new Product();
        product.setName(productName);
        product.setPrice(BigDecimal.valueOf(productPrice));
        return product;
    }

    public static Product generateProductWithId(final String productName, final int productPrice, final Long id) {
        Product product = new Product();
        product.setId(id);
        product.setName(productName);
        product.setPrice(BigDecimal.valueOf(productPrice));
        return product;
    }

    public static Product generateNormal(final String productName, final int productPrice) {
        Product product = new Product();
        product.setName(productName);
        product.setPrice(BigDecimal.valueOf(productPrice));
        return product;
    }

    private static Product generateNullPrice(final String productName) {
        Product product = new Product();
        product.setName(productName);
        return product;
    }

    private static Product generateInvalidPrice(final String productName) {
        Product product = new Product();
        product.setName(productName);
        product.setPrice(BigDecimal.valueOf(-1));
        return product;
    }
}
