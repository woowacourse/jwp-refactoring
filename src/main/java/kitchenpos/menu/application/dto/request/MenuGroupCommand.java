package kitchenpos.menu.application.dto.request;

import kitchenpos.menu.domain.MenuGroup;

public record MenuGroupCommand(String name) {

    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }
}
