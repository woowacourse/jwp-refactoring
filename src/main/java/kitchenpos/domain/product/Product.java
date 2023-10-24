package kitchenpos.domain.product;

import kitchenpos.domain.common.Money;
import kitchenpos.domain.common.Name;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Embedded
    private Name name;
    @NotNull
    @Embedded
    private Money price;

    protected Product() {
    }

    private Product(final Long id, final Name name, final Money price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Product of(final Name name, final Money price) {
        return new Product(null, name, price);
    }

    public static Product of(final String name, final long price) {
        return new Product(null, Name.of(name), Money.valueOf(price));
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }
}
