package kitchenpos;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    private static final String NAME = "육회초밥";
    private static final BigDecimal PRICE = BigDecimal.valueOf(15900);

    public static Product createProduct() {
        return createProduct(NAME, PRICE);
    }

    public static Product createProduct(Long id) {
        return createProduct(id, NAME, PRICE);
    }

    public static Product createProduct(String name, BigDecimal price) {
        return createProduct(null, name, price);
    }

    public static Product createProduct(Long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
