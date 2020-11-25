package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

import kitchenpos.exception.InvalidPriceException;

public class Menu {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;

    public Menu() {
    }

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        this(null, name, price, menuGroupId);
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId) {
        validatePrice(price);

        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException();
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }
}
