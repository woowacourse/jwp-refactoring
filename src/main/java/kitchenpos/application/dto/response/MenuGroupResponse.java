package kitchenpos.application.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupResponse {

    private final long id;
    private final String name;

    private MenuGroupResponse(final long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse from(final MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public static List<MenuGroupResponse> from(final List<MenuGroup> menuGroups) {
        return menuGroups.stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
