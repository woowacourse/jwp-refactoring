package kitchenpos.menu.domain;

import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

public class MenuChangeEvent extends ApplicationEvent {
    private TemporaryMenu temporaryMenu;
    private LocalDateTime updatedAt;

    public MenuChangeEvent(Object source, TemporaryMenu temporaryMenu, LocalDateTime updatedAt) {
        super(source);
        this.temporaryMenu = temporaryMenu;
        this.updatedAt = updatedAt;
    }

    public TemporaryMenu getTemporaryMenu() {
        return temporaryMenu;
    }

    public Long getMenuId() {
        return temporaryMenu.getTempMenuId();
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
