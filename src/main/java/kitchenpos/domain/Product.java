package kitchenpos.domain;

import java.util.Objects;

@Deprecated
public class Product {
    private Long id;
    private String name;
    private Price price;

    public Product(Long id, String name, long price) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
    }

    public Product(String name, long price) {
        this(null, name, price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(name, product.name) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
