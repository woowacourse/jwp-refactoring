package kitchenpos.menu_group.persistence.entity;

import kitchenpos.menu_group.domain.MenuGroup;

public class MenuGroupEntity {

    private Long id;
    private String name;

    public MenuGroupEntity(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroupEntity() {
    }

    public static MenuGroupEntity from(final MenuGroup menuGroup) {
        return new MenuGroupEntity(menuGroup.getId(), menuGroup.getName());
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
