package kitchenpos.menu.domain;

import java.math.BigDecimal;
import kitchenpos.product.domain.Price;

public class Menu {
    private final Long id;
    private final String name;
    private final Price price;
    private final Long menuGroupId;

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        this(null, name, new Price(price), menuGroupId);
    }

    public Menu(Long id, String name, Price price, Long menuGroupId) {
        this.price = price;
        this.id = id;
        this.name = name;
        this.menuGroupId = menuGroupId;
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
