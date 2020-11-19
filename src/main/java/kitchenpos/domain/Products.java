package kitchenpos.domain;

import java.util.List;

import javax.persistence.Embeddable;

@Embeddable
public class Products {
    private List<Product> products;

    public Products() {
    }

    public Products(List<Product> products) {
        this.products = products;
    }

    public Product findById(Long id) {
        return products.stream()
                .filter(p -> id.equals(p.getId()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
