package kitchenpos.menu.ui;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private MenuRequest() {
    }

    public Menu toMenu() {
        return Menu.of(name, price, menuGroupId, toMenuProduct(menuProducts));
    }

    private List<MenuProduct> toMenuProduct(List<MenuProductRequest> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductRequest::toMenuProduct)
                .collect(Collectors.toList());
    }
}
