package kitchenpos.menu.domain;

import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

public class MenuChangeEvent extends ApplicationEvent {
    private OrderedMenu orderedMenu;
    private LocalDateTime updatedAt;

    public MenuChangeEvent(Object source, OrderedMenu orderedMenu, LocalDateTime updatedAt) {
        super(source);
        this.orderedMenu = orderedMenu;
        this.updatedAt = updatedAt;
    }

    public OrderedMenu getTemporaryMenu() {
        return orderedMenu;
    }

    public Long getMenuId() {
        return orderedMenu.getTempMenuId();
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
