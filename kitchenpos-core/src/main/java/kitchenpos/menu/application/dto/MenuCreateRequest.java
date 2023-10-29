package kitchenpos.menu.application.dto;

import java.util.List;
import javax.validation.constraints.NotNull;

public class MenuCreateRequest {

    @NotNull
    private String name;

    @NotNull
    private Long price;

    @NotNull
    private Long menuGroupId;

    @NotNull
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
