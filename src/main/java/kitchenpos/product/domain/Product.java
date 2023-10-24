package kitchenpos.product.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.Money;
import kitchenpos.common.exception.KitchenPosException;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String name;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "price", precision = 19, scale = 2, nullable = false))
    private Money price;

    protected Product() {
    }

    public Product(Long id, String name, Money price) {
        validate(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validate(Money price) {
        if (price == null) {
            throw new KitchenPosException("상품의 가격은 null이 될 수 없습니다.");
        }
        if (price.isLessThan(Money.ZERO)) {
            throw new KitchenPosException("상품의 가격은 0보다 작을 수 없습니다.");
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
