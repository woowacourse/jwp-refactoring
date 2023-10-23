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
            throw new IllegalArgumentException("존재하지 않는 상품에 대한 요청이 있습니다.");
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
