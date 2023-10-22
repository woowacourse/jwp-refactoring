package kitchenpos.domain;

import kitchenpos.domain.vo.Name;
import kitchenpos.domain.vo.Price;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    protected Product() {
    }

    public Product(final Name name, final Price price) {
        this(null, name, price);
    }

    protected Product(final Long id, final Name name, final Price price) {
        validate(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validate(final Price price) {
        if (isPriceNullOrNegative(price)) {
            throw new IllegalArgumentException("상품의 가격은 null 이거나 음수일 수 없습니다.");
        }
    }

    private boolean isPriceNullOrNegative(final Price price) {
        return price == null || price.getValue().compareTo(BigDecimal.ZERO) < 0;
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
