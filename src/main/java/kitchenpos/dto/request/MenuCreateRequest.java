package kitchenpos.dto.request;

import java.util.List;

public class MenuCreateRequest {

    private final String name;
    private final long price;
    private final long menuGroupId;
    private final List<MenuProductCreateRequest> menuProducts;

    public MenuCreateRequest(final String name, final long price, final long menuGroupId, final List<MenuProductCreateRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductCreateRequest> getMenuProducts() {
        return menuProducts;
    }
}
