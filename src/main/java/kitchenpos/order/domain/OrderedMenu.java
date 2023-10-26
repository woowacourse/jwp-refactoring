package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

import java.math.BigDecimal;

public class OrderedMenu {
    private final Long id;
    private final String name;
    private final BigDecimal price;

    public OrderedMenu(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static OrderedMenu from(Menu menu) {
        return new OrderedMenu(menu.getId(), menu.getName(), menu.getPrice());
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
