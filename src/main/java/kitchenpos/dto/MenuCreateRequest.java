package kitchenpos.dto;

import java.util.List;

public class MenuCreateRequest {

    private final String name;
    private final Long price;
    private final Long menuGroupId;
    private final List<MenuProductCreateRequest> menuProducts;

    public MenuCreateRequest(final String name,
                             final Long price,
                             final Long menuGroupId,
                             final List<MenuProductCreateRequest> menuProducts) {
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

    public List<MenuProductCreateRequest> getMenuProducts() {
        return menuProducts;
    }
}
