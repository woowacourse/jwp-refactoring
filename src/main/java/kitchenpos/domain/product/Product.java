package kitchenpos.domain.product;

import java.math.BigDecimal;
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
        if (isNull(name)) {
            throw new IllegalArgumentException("상품명이 없습니다.");
        }
        if (isNull(price)) {
            throw new IllegalArgumentException("상품 가격이 없습니다.");
        }
        this.name = name;
        this.price = price;
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

    public void setName(final String name) {
        this.name = name;
    }

    public Money getPrice() {
        return price;
    }



}
