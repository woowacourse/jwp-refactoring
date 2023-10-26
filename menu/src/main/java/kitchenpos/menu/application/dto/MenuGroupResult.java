package kitchenpos.menu.application.dto;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupResult {

    private final Long id;
    private final String name;

    public MenuGroupResult(
            final Long id,
            final String name
    ) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResult from(final MenuGroup menuGroup) {
        return new MenuGroupResult(
                menuGroup.getId(),
                menuGroup.getName()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
