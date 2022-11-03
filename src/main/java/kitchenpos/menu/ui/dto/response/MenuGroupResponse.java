package kitchenpos.menu.ui.dto.response;

import kitchenpos.menu.application.dto.MenuGroupDto;

public class MenuGroupResponse {

    private Long id;
    private String name;

    private MenuGroupResponse() {
    }

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

    @Override
    public String toString() {
        return "MenuGroupResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
