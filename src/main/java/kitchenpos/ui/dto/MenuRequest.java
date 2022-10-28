package kitchenpos.ui.dto;

import java.util.List;

public class MenuRequest {

    private String name;
    private long price;
    private long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {
    }

    public MenuRequest(String name, long price, long menuGroupId, List<MenuProductRequest> menuProducts) {
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

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
