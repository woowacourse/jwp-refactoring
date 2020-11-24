package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Menu {

    private Long id;
    private String name;
    private Price price;
    private Long menuGroupId;

    private Menu() {
    }

    public Menu(Long id, String name, Price price, Long menuGroupId) {
        validate(name, price, menuGroupId);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(String name, Price price, Long menuGroupId) {
        this(null, name, price, menuGroupId);
    }

    private void validate(String name, Price price, Long menuGroupId) {
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new IllegalArgumentException("menu name cannot be empty.");
        }
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException("menu price cannot be null.");
        }
        if (Objects.isNull(menuGroupId)) {
            throw new IllegalArgumentException("menu group id cannot be null.");
        }
    }

    public boolean isPriceBiggerThen(Price price) {
        return this.price.isBiggerThen(price.getValue());
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }
}
