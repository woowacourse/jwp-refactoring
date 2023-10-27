package kitchenpos.product.domain;

public class Product {

    private final Long id;
    private final String name;
    private final Price price;

    public Product(final Long id, final String name, final Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(final String name, final Price price) {
        this(null, name, price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
