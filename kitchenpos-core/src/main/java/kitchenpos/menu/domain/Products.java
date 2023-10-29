package kitchenpos.menu.domain;

import java.util.List;

public class Products {

    private final List<Product> products;

    public Products(final List<Product> products) {
        this.products = products;
    }

    public Product findProductById(final long productId) {
        return products.stream()
                       .filter(product -> product.getId().equals(productId))
                       .findFirst()
                       .orElseThrow(() -> new IllegalArgumentException("잘못된 상품 아이디입니다."));
    }
}
