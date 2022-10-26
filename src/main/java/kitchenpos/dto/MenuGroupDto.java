package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupDto {
    private Long id;
    private String name;

    public MenuGroupDto() {
    }

    public MenuGroupDto(MenuGroup menuGroup) {
        this.id = menuGroup.getId();
        this.name = menuGroup.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
