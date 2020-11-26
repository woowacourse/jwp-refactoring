package kitchenpos.product.domain;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Products {
    private final Map<Long, Product> products;

    public Products(List<Product> products) {
        this.products = products.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));
    }

    public Product findProductBy(Long productId) {
        return Optional.ofNullable(products.get(productId))
                .orElseThrow(() -> new IllegalArgumentException(String.format("해당하는 아이디(%s)의 상품을 찾지 못했습니다.", productId)));
    }
}
