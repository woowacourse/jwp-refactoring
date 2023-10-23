package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Price price;

    public Product() {
    }

    public Product(Long id, String name, Price price) {
        validate(name, price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validate(String name, Price price) {
        if (name == null) {
            throw new IllegalArgumentException("상품의 이름은 필수입니다.");
        }
        if (price == null) {
            throw new IllegalArgumentException("상품의 가격은 필수입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
