package kitchenpos.product;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "product")
@Entity
public class Product {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Convert(converter = PriceConverter.class)
    private Price price;

    public Product() {
    }

    public Product(String name, BigDecimal price) {
        this.id = null;
        this.name = name;
        this.price = new Price(price);
    }

    public Price multiplyWithQuantity(long quantity) {
        return price.multiplyWithQuantity(quantity);
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
