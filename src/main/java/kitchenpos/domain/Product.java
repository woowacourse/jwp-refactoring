package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kitchenpos.support.domain.BaseEntity;
import kitchenpos.support.money.Money;

@Entity
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;

    private Money price;

    protected Product() {
    }

    public Product(String name, Money price) {
        this(null, name, price);
    }

    public Product(Long id, String name, Money price) {
        validate(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validate(Money price) {
        if (Objects.isNull(price) || price.isLessThan(Money.ZERO)) {
            throw new IllegalArgumentException("상품의 가격은 0원 이상이어야 합니다.");
        }
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
