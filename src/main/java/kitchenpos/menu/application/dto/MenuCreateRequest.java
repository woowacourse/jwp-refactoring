package kitchenpos.menu.application.dto;

import kitchenpos.menu.domain.MenuProduct;

import java.util.List;
import java.util.stream.Collectors;

public class MenuCreateRequest {

    private String name;
    private Long price;
    private Long menuGroupId;
    private List<MenuProductCreateRequest> menuProducts;

    private MenuCreateRequest() {
    }

    public MenuCreateRequest(final String name,
                             final Long price,
                             final Long menuGroupId,
                             final List<MenuProductCreateRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public List<MenuProduct> toMenuProducts() {
        return menuProducts.stream()
                .map(it -> new MenuProduct(it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductCreateRequest> getMenuProducts() {
        return menuProducts;
    }
}
