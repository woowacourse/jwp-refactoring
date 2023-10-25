package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.function.Consumer;
import kitchenpos.domain.Product;

public enum ProductFixture {

    FRIED_CHICKEN(1L, "후라이드치킨", BigDecimal.valueOf(16000L)),
    SPICY_CHICKEN(2L, "매운치킨", BigDecimal.valueOf(16000L));

    public final Long id;
    public final String name;
    public final BigDecimal price;

    ProductFixture(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Product computeDefaultMenu(Consumer<Product> consumer) {
        Product product = new Product();
        product.setId(1L);
        product.setName("후라이드치킨");
        product.setPrice(BigDecimal.valueOf(16000L));
        consumer.accept(product);
        return product;
    }

    public Product toEntity() {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}

