package kitchenpos.domain;

import java.math.BigDecimal;

public class Product {
    private Long id;
    private String name;
    private BigDecimal price;

    public Product(String name, BigDecimal price) {
        validate(name, price);
        this.name = name;
        this.price = price;
    }

    public Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
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

    private void validate(String name, BigDecimal price) {
        validateName(name);
        validatePrice(price);
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("상품의 이름은 비어있을 수 없습니다.");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("상품의 가격은 비어있을 수 없습니다.");
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("상품은 0원 미만일 수 없습니다.");
        }
    }

}

