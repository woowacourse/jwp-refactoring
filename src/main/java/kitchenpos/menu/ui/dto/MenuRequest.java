package kitchenpos.menu.ui.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuRequest {

    private String name;
    private Long price;
    private Long menuGroupId;
    private List<MenuProductDto> menuProducts;

    private MenuRequest() {
    }

    public MenuRequest(final String name, final Long price, final Long menuGroupId,
                       final List<MenuProductDto> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toEntity() {
        return new Menu(name, BigDecimal.valueOf(price), menuGroupId, toMenuProducts());
    }

    private List<MenuProduct> toMenuProducts() {
        return menuProducts.stream()
                .map(MenuProductDto::toEntity)
                .collect(Collectors.toList());
    }

    public Long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public String getName() {
        return name;
    }

    public List<MenuProductDto> getMenuProducts() {
        return menuProducts;
    }
}
