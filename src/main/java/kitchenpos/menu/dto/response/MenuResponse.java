package kitchenpos.menu.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    private MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId,
        List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(Menu menu) {

        MenuGroup menuGroup = menu.getMenuGroup();
        List<MenuProductResponse> menuProducts = menu.getMenuProducts().stream()
            .map(MenuProductResponse::from)
            .collect(Collectors.toUnmodifiableList());

        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menuGroup.getId(), menuProducts);
    }
}
