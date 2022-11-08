package kitchenpos.menu.ui.dto.menu;

import java.util.List;
import kitchenpos.menu.domain.entity.MenuProduct;

public class MenuListResponse {

    private Long id;
    private String name;
    private long price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public MenuListResponse() {
    }

    public MenuListResponse(Long id, String name, long price, Long menuGroupId,
                              List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Long getId() {
        return id;
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
