package kitchenpos.menu.domain;

import java.math.BigDecimal;

import org.springframework.context.ApplicationEvent;

public class MenuCreateEvent extends ApplicationEvent {

    private final Long menuId;
    private final String name;
    private final BigDecimal price;

    public MenuCreateEvent(final Object source, final Menu menu) {
        super(source);
        this.menuId = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
