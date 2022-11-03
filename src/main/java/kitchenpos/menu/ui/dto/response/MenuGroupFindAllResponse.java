package kitchenpos.menu.ui.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupFindAllResponse {

    private Long id;
    private String name;

    public MenuGroupFindAllResponse() {
    }

    private MenuGroupFindAllResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static List<MenuGroupFindAllResponse> from(final List<MenuGroup> menuGroups) {
        return menuGroups.stream()
                .map(menuGroup -> new MenuGroupFindAllResponse(menuGroup.getId(), menuGroup.getName()))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
