package kitchenpos.menu.presentation.dto;

import kitchenpos.menu.application.dto.request.MenuGroupCommand;

public record MenuGroupRequest(String name) {

    public MenuGroupCommand toCommand() {
        return new MenuGroupCommand(name);
    }
}
