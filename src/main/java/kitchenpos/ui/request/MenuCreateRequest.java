package kitchenpos.ui.request;

import java.util.List;

public class MenuCreateRequest {

    private String name;
    private Integer price;
    private Long menuGroupId;
    private List<MenuProductCreateRequest> menuProductCreateRequests;

    public MenuCreateRequest(
            String name,
            Integer price,
            Long menuGroupId,
            List<MenuProductCreateRequest> menuProductCreateRequests
    ) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductCreateRequests = menuProductCreateRequests;
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

    public List<MenuProductCreateRequest> getMenuProductCreateRequests() {
        return menuProductCreateRequests;
    }

}
