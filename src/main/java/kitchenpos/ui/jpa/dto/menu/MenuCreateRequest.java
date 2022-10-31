package kitchenpos.ui.jpa.dto.menu;

import java.util.List;
import kitchenpos.domain.entity.MenuProduct;

public class MenuCreateRequest {

    private String name;
    private long price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public MenuCreateRequest() {
    }

    public MenuCreateRequest(String name, long price, Long menuGroupId, List<MenuProduct> menuProducts) {
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
