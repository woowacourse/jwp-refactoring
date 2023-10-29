package kitchenpos.menu.presentation.dto.request;

import java.util.List;

public class CreateMenuRequest {

    private String name;

    private long price;

    private Long menuGroupId;

    private List<MenuProductRequest> menuProducts;

    private CreateMenuRequest() {
    }

    public CreateMenuRequest(final String name,
                             final long price,
                             final Long menuGroupId,
                             final List<MenuProductRequest> menuProducts) {
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
