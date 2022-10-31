package kitchenpos.ui.dto;

import kitchenpos.application.dto.request.MenuGroupCommand;

public record MenuGroupRequest(String name) {

    public MenuGroupCommand toCommand() {
        return new MenuGroupCommand(name);
    }
}
