package kitchenpos.domain.product;

import kitchenpos.domain.vo.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;

import java.math.BigDecimal;

public class Product {
    @Id
    private Long id;
    private String name;
    private Money price;

    public Product(String name, BigDecimal price) {
        this(null, name, price);
    }

    @PersistenceCreator
    public Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = new Money(price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }
}
