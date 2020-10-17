package kitchenpos.factory;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import kitchenpos.domain.Product;

@Component
public class ProductFactory {
    public Product create(Long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public Product create(String name, BigDecimal price) {
        return create(null, name, price);
    }

    public Product create(String name) {
        return create(null, name, null);
    }
}
