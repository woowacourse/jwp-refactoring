package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class Products {

    private final List<Product> products;

    public Products(Product... products) {
        this(Arrays.asList(products));
    }

    public Products(List<Product> products) {
        this.products = products;
    }

    public BigDecimal totalPrice(MenuProduct menuProduct) {
        return findById(menuProduct.getProductId())
            .totalPrice(menuProduct.getQuantity());
    }

    public Product findById(Long id) {
        return products.stream()
            .filter(product -> product.isId(id))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
