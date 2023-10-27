package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.vo.Money;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price", nullable = false))
    private Money price;

    protected Product() {
    }

    public Product(final String name, final BigDecimal price) {
        this(null, name, price);
    }

    private Product(final Long id, final String name, final BigDecimal price) {
        final Money money = Money.valueOf(price);
        validate(money);
        this.id = id;
        this.name = name;
        this.price = money;
    }

    private void validate(final Money price) {
        if (Objects.isNull(price) || price.isSmallerThan(Money.empty())) {
            throw new IllegalArgumentException("상품 가격은 0 이하 혹은 null일 수 없습니다.");
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
