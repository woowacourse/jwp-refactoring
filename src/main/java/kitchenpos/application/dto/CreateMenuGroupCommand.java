package kitchenpos.application.dto;

import kitchenpos.domain.menugroup.MenuGroup;

public class CreateMenuGroupCommand {
    private String name;

    public CreateMenuGroupCommand(final String name) {
        this.name = name;
    }

    public MenuGroup toDomain() {
        return new MenuGroup(name);
    }

    public String getName() {
        return name;
    }

}
