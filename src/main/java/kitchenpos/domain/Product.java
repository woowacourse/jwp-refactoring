package kitchenpos.domain;

import java.util.Objects;

public class Product {
    private Long id;
    private String name;
    private Price price;

    @Deprecated
    public Product() {
    }

    public Product(String name, long price) {
        this.name = name;
        this.price = new Price(price);
    }

    public Long getId() {
        return id;
    }

    @Deprecated
    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Deprecated
    public void setName(final String name) {
        this.name = name;
    }

    public long getPrice() {
        return price.getValue();
    }

    @Deprecated
    public void setPrice(final long price) {
        this.price = new Price(price);
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
