package kitchenpos.domain.product;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kitchenpos.domain.Money;

import static java.util.Objects.isNull;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;

    @Embedded
    @AttributeOverride(name = "value", column = @javax.persistence.Column(name = "price"))
    private Money price;

    protected Product() {
    }

    public Product(final String name, final Money price) {
        this(null, name, price);
    }

    public Product(final Long id, final String name, final Money price) {
        if (isNull(name)) {
            throw new IllegalArgumentException("상품명이 없습니다.");
        }
        if (isNull(price)) {
            throw new IllegalArgumentException("상품 가격이 없습니다.");
        }
        this.id = id;
        this.name = name;
        this.price = price;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }



}
