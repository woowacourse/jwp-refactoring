package kitchenpos.factory;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFactory {

    private Long id;
    private String name;
    private BigDecimal price;

    private ProductFactory() {

    }

    private ProductFactory(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductFactory builder() {
        return new ProductFactory();
    }

    public static ProductFactory copy(Product product) {
        return new ProductFactory(
            product.getId(),
            product.getName(),
            product.getPrice()
        );
    }

    public ProductFactory id(Long id) {
        this.id = id;
        return this;
    }

    public ProductFactory name(String name) {
        this.name = name;
        return this;
    }

    public ProductFactory price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Product build() {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
