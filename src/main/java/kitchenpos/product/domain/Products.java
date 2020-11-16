package kitchenpos.product.domain;

import java.util.List;

public class Products {
    private final List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public Product findProductBy(Long productId) {
        return products.stream()
                .filter(product -> product.isSameId(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("해당하는 아이디(%s)의 상품을 찾지 못했습니다.", productId)));
    }
}
