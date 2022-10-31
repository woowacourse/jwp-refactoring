package kitchenpos.ui.jpa.dto.menu;

import java.util.List;
import kitchenpos.domain.entity.Menu;
import kitchenpos.domain.entity.MenuProduct;
import kitchenpos.domain.entity.Price;

public class MenuCreateResponse {

    private Long id;
    private String name;
    private long price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public MenuCreateResponse() {
    }

    public MenuCreateResponse(Long id, String name, long price, Long menuGroupId,
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
