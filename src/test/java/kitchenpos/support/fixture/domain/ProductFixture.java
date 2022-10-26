package kitchenpos.support.fixture.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;

public enum ProductFixture {

    APPLE_1000("apple", 1000),
    PEAR_2000("pear", 2000),
    BANANA_3000("banana", 3000)
    ;

    private final Name name;
    private final Price price;

    ProductFixture(String name, Integer price) {
        this.name = Name.of(name);
        this.price = Price.valueOf(price);
    }

    public Product getProduct() {
        return new Product(name, price);
    }

    public Product getProduct(Long id) {
        return new Product(id, name, price);
    }
}
