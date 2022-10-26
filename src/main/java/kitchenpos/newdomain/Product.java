package kitchenpos.newdomain;

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
}
