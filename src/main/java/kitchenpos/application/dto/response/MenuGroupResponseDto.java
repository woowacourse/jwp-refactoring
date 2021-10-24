package kitchenpos.application.dto.response;

import kitchenpos.domain.MenuGroup;

public class MenuGroupResponseDto {

    private Long id;
    private String name;

    private MenuGroupResponseDto() {
    }

    public MenuGroupResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponseDto from(MenuGroup menuGroup) {
        return new MenuGroupResponseDto(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
