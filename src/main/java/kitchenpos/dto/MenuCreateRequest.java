package kitchenpos.dto;

import java.util.List;

public class MenuCreateRequest {

    private String name;
    private Long price;
    private Long menuGroupId;
    private List<MenuProductsRequest> menuProducts;

    public MenuCreateRequest(String name, Long price, Long menuGroupId, List<MenuProductsRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public MenuCreateRequest() {
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
