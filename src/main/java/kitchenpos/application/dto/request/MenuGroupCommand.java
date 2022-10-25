package kitchenpos.application.dto.request;

import kitchenpos.domain.MenuGroup;

public record MenuGroupCommand(String name) {

    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }
}
