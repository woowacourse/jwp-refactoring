package kitchenpos.domain.fixture;

import java.math.BigDecimal;

import kitchenpos.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
public class ProductFixture {

    private Long id;
    private String name;
    private BigDecimal price;

    private ProductFixture() {
    }

    public static ProductFixture 후라이드_치킨() {
        final ProductFixture productFixture = new ProductFixture();
        productFixture.name = "후라이드 치킨";
        return productFixture;
    }

    public ProductFixture 가격(final BigDecimal price) {
        this.price = price;
        return this;
    }

    public Product build() {
        final Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
