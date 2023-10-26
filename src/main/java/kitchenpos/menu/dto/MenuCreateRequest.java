package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuCreateRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductCreateRequest> menuProducts;

    public MenuCreateRequest() {
    }

    public MenuCreateRequest(
            String name,
            BigDecimal price,
            Long menuGroupId,
            List<MenuProductCreateRequest> menuProducts
    ) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toMenu(List<MenuProduct> menuProducts) {
        return Menu.of(name, price, menuGroupId, menuProducts);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductCreateRequest> getMenuProducts() {
        return menuProducts;
    }
}
