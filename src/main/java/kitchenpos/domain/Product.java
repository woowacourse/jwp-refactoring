package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.exception.ProductException.InvalidProductNameException;
import org.springframework.lang.NonNull;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @NonNull
    private String name;
    @Embedded
    private Price price;

    protected Product() {
    }

    private Product(final String name, final Price price) {
        this.name = name;
        this.price = price;
    }

    public static Product of(final String name, final BigDecimal price) {
        validateName(name);
        return new Product(name, Price.from(price));
    }

    private static void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new InvalidProductNameException();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }
}
