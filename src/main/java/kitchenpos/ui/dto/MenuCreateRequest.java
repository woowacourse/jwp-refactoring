package kitchenpos.ui.dto;

import java.util.List;

public class MenuCreateRequest {

    private long price;
    private Long menuGroupId;
    private String name;
    private List<MenuProductCreateRequest> menuProducts;

    private MenuCreateRequest() {
    }

    public MenuCreateRequest(long price, Long menuGroupId, String name, List<MenuProductCreateRequest> menuProducts) {
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.name = name;
        this.menuProducts = menuProducts;
    }

    public long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public String getName() {
        return name;
    }

    public List<MenuProductCreateRequest> getMenuProducts() {
        return menuProducts;
    }
}
