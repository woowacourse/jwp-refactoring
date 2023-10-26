package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;

public class MenuCreatedEvent {
    private Long id;
    private Long menuGroupId;

    public MenuCreatedEvent(Long id, Long menuGroupId) {
        this.id = id;
        this.menuGroupId = menuGroupId;
    }

    public static MenuCreatedEvent from(Menu menu) {
        return new MenuCreatedEvent(menu.getId(), menu.getMenuGroupId());
    }

    public Long getId() {
        return id;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }
}
