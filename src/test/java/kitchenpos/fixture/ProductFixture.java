package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

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

    public static Product createProduct() {
        return createProduct(ID, NAME, PRICE);
    }

    public static Product createProduct(Long id) {
        return createProduct(id, NAME, PRICE);
    }

    public static Product createProduct(BigDecimal price) {
        return createProduct(ID, NAME, price);
    }
}
