package kitchenpos.application.dto;

import kitchenpos.domain.menugroup.MenuGroup;

public class CreateMenuGroupDto {

    private Long id;
    private String name;

    public CreateMenuGroupDto(final MenuGroup menuGroup) {
        this.id = menuGroup.getId();
        this.name = menuGroup.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
