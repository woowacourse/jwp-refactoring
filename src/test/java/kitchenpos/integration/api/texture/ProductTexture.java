package kitchenpos.integration.api.texture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public enum ProductTexture {
    강정치킨(new Product("강정치킨", new BigDecimal(17000))),
    민초치킨(new Product("민초치킨", new BigDecimal(18000)));

    private final Product product;

    ProductTexture(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
}
