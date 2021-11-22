package kitchenpos.menu.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupResponse {

    private Long id;
    private String name;

    public static MenuGroupResponse of(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public static List<MenuGroupResponse> toList(List<MenuGroup> menuGroups) {
        return menuGroups.stream()
                .map(menuGroup -> new MenuGroupResponse(menuGroup.getId(), menuGroup.getName()))
                .collect(Collectors.toList());
    }

    protected MenuGroupResponse() {
    }

    private MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
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
