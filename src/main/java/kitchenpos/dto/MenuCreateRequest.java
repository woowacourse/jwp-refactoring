package kitchenpos.dto;

import java.util.List;

public class MenuCreateRequest {

    private final String name;
    private final Long price;
    private final Long menuGroupId;
    private final List<MenuProductsRequest> menuProducts;

    public MenuCreateRequest(String name, Long price, Long menuGroupId, List<MenuProductsRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public List<MenuProductsRequest> getMenuProducts() {
        return menuProducts;
    }
}
