package kitchenpos.ui.factory;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFactory {

    public static Product create(Long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
