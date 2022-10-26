package kitchenpos.newdomain;

import kitchenpos.newdomain.vo.Name;
import kitchenpos.newdomain.vo.Price;
import kitchenpos.newdomain.vo.Quantity;

public class Product {

    private Long id;
    private Name name;
    private Price price;

    public Product(final Name name, final Price price) {
        this.name = name;
        this.price = price;
    }

    public Product(final Long id, final Name name, final Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Price calculateTotalPrice(final Quantity quantity) {
        return price.multiply(quantity);
    }
}
