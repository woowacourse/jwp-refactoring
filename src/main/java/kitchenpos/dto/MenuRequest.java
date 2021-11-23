package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;

public class MenuRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {}

    public MenuRequest(String name, double price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = BigDecimal.valueOf(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public Menu toEntity(MenuGroup menuGroup) {
        return new Menu(null, name, price, menuGroup, new ArrayList<>());
    }
}
