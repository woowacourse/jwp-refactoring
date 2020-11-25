package kitchenpos.application.command;

import javax.validation.constraints.NotBlank;

import kitchenpos.domain.model.menugroup.MenuGroup;

public class CreateMenuGroupCommand {
    @NotBlank
    private String name;

    private CreateMenuGroupCommand() {
    }

    public CreateMenuGroupCommand(String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(null, name);
    }

    public String getName() {
        return name;
    }
}
