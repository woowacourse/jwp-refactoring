package kitchenpos.domain;

import javax.persistence.Embeddable;
import java.util.List;

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
