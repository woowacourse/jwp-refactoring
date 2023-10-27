package kitchenpos.menugroup.application.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menugroup.application.domain.MenuGroup;
import kitchenpos.product.domain.Name;

public class MenuGroupResponse {
    private Long id;
    private Name name;

    public MenuGroupResponse(final Long id, final Name name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse from(final MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public static List<MenuGroupResponse> from(final List<MenuGroup> menuGroups) {
        return menuGroups
                .stream().map(menuGroup -> new MenuGroupResponse(menuGroup.getId(), menuGroup.getName()))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }
}
