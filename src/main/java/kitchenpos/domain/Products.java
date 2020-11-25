package kitchenpos.domain;

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

    public Product findProductById(Long productId) {
        return Optional.ofNullable(products.get(productId))
            .orElseThrow(() -> new IllegalArgumentException(String.format("해당 상품 아이디(%s)가 존재하지 않습니다.", productId)));
    }
}
