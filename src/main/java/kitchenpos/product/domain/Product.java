package kitchenpos.product.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

public class Product {

    private final Long id;
    private final Name name;
    private final Price price;

    public Product(Long id, Name name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(Name name, Price price) {
        this(null, name, price);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
