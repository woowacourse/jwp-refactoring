package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    private static final Long ID = 1L;
    private static final String NAME = "PRODUCT_NAME";
    private static final BigDecimal PRICE = BigDecimal.TEN;

    public static Product createProduct() {
        Product product = new Product();
        product.setId(ID);
        product.setName(NAME);
        product.setPrice(PRICE);
        return product;
    }

    public static Product createProduct(Long id) {
        Product product = new Product();
        product.setId(id);
        product.setName(NAME);
        product.setPrice(PRICE);
        return product;
    }

    public static Product createProduct(Long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static Product createProduct(BigDecimal price) {
        Product product = new Product();
        product.setId(ID);
        product.setName(NAME);
        product.setPrice(price);
        return product;
    }
}
