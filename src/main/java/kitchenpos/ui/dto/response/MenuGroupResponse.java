package kitchenpos.ui.dto.response;

import kitchenpos.application.dto.response.MenuGroupResponseDto;

public class MenuGroupResponse {

    private Long id;
    private String name;

    private MenuGroupResponse() {
    }

    public MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse from(MenuGroupResponseDto menuGroupResponseDto) {
        return new MenuGroupResponse(menuGroupResponseDto.getId(), menuGroupResponseDto.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
