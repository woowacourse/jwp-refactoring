package kitchenpos.menu.ui.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;

public class MenuFindAllResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductFindAllResponse> menuProducts;

    public MenuFindAllResponse() {
    }

    public MenuFindAllResponse(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                               final List<MenuProductFindAllResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static List<MenuFindAllResponse> from(final List<Menu> menus) {
        return menus.stream()
                .map(MenuFindAllResponse::from)
                .collect(Collectors.toList());
    }

    private static MenuFindAllResponse from(final Menu menu) {
        return new MenuFindAllResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(),
                MenuProductFindAllResponse.from(menu.getMenuProducts()));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductFindAllResponse> getMenuProducts() {
        return menuProducts;
    }
}
