package kitchenpos.domain.menu.service.dto;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProducts;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class MenuResponse {

    private final Long id;

    private final String name;

    private final Long price;

    private final MenuGroupResponse menuGroup;

    private final List<MenuProductResponse> menuProductResponses;

    public MenuResponse(final Long id, final String name, final Long price, final MenuGroupResponse menuGroup, final List<MenuProductResponse> menuProductResponses) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProductResponses = menuProductResponses;
    }

    public static MenuResponse toDto(final Menu menu) {
        final MenuGroup menuGroup = menu.getMenuGroup();
        final MenuGroupResponse menuGroupResponse = MenuGroupResponse.toDto(menuGroup);

        final MenuProducts menuProducts = menu.getMenuProducts();
        final List<MenuProductResponse> menuProductResponses = menuProducts.getMenuProducts().stream()
                .map(MenuProductResponse::toDto)
                .collect(toList());

        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice().getPrice().longValue(), menuGroupResponse, menuProductResponses);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public MenuGroupResponse getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProductResponse> getMenuProductResponses() {
        return menuProductResponses;
    }
}
