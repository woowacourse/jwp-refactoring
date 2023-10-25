package kitchenpos.application.dto;

import kitchenpos.domain.menugroup.MenuGroup;

public class ReadMenuGroupDto {

    private Long id;
    private String name;

    public ReadMenuGroupDto(final MenuGroup menuGroup) {
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
