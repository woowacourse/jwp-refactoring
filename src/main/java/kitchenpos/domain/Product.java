package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    protected Product() {
    }

    public Product(String name, BigDecimal price) {
        this(null, name, price);
    }

    public Product(Long id, String name, BigDecimal price) {
        validateName(name);
        validatePrice(price);

        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validateName(String name) {
        if (name.isBlank() || name.length() > 255) {
            throw new ProductException("상품의 이름이 유효하지 않습니다.");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price)
                || price.compareTo(BigDecimal.ZERO) < 0
                || price.compareTo(BigDecimal.valueOf(Math.pow(10, 20))) >= 0
        ) {
            throw new ProductException("상품의 가격이 유효하지 않습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
