package kitchenpos.domain.product;

import java.math.BigDecimal;
import java.util.List;

public class Products {

    private List<Product> products;

    public Products(final List<Product> products) {
        this.products = products;
    }

    public void validateProductsCnt(final int requestProductCnt) {
        if (products.size() != requestProductCnt) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal getTotalPrice() {
        return products.stream()
            .map(Product::getPrice)
            .reduce(BigDecimal::add)
            .get();
    }

    public List<Product> getProducts() {
        return products;
    }
}
