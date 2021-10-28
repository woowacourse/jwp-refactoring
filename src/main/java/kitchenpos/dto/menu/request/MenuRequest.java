package kitchenpos.dto.menu.request;

import java.util.List;

public class MenuRequest {

    private final String name;
    private final Integer price;
    private final Long menuGroupId;
    private final List<MenuProductRequest> menuProducts;

    public MenuRequest(
        String name,
        Integer price,
        Long menuGroupId,
        List<MenuProductRequest> menuProductRequests
    ) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProductRequests;
    }

    public MenuRequest() {
        this(null, null, null, null);
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
