package kitchenpos.menu.domain;

import java.math.BigDecimal;
import kitchenpos.common.domain.Price;

public class Menu {

    private final Long id;
    private final String name;
    private final Price price;
    private final Long menuGroupId;

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
    }

    public Menu(final String name, final BigDecimal price, final Long menuGroupId) {
        this(null, name, price, menuGroupId);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.get();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }
}
