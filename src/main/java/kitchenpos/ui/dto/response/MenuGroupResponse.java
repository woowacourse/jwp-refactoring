package kitchenpos.ui.dto.response;

import kitchenpos.application.dto.MenuGroupCreationDto;
import kitchenpos.application.dto.MenuGroupDto;

public class MenuGroupResponse {

    private Long id;
    private String name;

    private MenuGroupResponse() {}

    public MenuGroupResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse from(final MenuGroupDto menuGroupDto) {
        return new MenuGroupResponse(menuGroupDto.getId(), menuGroupDto.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
