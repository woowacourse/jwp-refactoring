package kitchenpos.menu.presentation.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;

public class MenuResponse {

    private final Long id;

    private final String name;

    private final long price;

    private final Long menuGroupId;

    private final List<MenuProductResponse> menuProducts;

    private MenuResponse(final Long id,
                         final String name,
                         final long price,
                         final Long menuGroupId,
                         final List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(final Menu menu) {
        return new MenuResponse(menu.getId(),
                                menu.getName(),
                                menu.getPrice().getValue().longValue(),
                                menu.getMenuGroupId(),
                                MenuProductResponse.convertToList(menu.getMenuProducts()));
    }

    public static List<MenuResponse> convertToList(final List<Menu> menus) {
        return menus.stream()
                    .map(MenuResponse::from)
                    .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
