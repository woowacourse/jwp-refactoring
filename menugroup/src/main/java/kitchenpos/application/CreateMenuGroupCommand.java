package kitchenpos.application;

import com.fasterxml.jackson.annotation.JsonCreator;
import kitchenpos.domain.MenuGroup;

public class CreateMenuGroupCommand {

    private String name;

    @JsonCreator
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
