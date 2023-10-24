package kitchenpos.application.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;

public class MenuGroupResponse {

    private Long id;

    private String name;

    private List<MenuResponse> menus;

    public MenuGroupResponse() {
    }

    public MenuGroupResponse(final Long id, final String name, final List<MenuResponse> menus) {
        this.id = id;
        this.name = name;
        this.menus = menus;
    }

    public static MenuGroupResponse from(final MenuGroup menuGroup) {
        final List<MenuResponse> menus = menuGroup.getMenus().stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());

        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName(), menus);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<MenuResponse> getMenus() {
        return menus;
    }
}
