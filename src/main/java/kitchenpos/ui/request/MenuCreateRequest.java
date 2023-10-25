package kitchenpos.ui.request;

import java.util.List;

public class MenuCreateRequest {

    private String name;
    private Long price;
    private Long menuGroupId;
    private List<MenuProductCreateRequest> menuProducts;

    public MenuCreateRequest() {
    }

    public MenuCreateRequest(
            String name,
            Long price,
            Long menuGroupId,
            List<MenuProductCreateRequest> menuProductCreateRequests
    ) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProductCreateRequests;
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
