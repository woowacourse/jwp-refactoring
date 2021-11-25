package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;

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

    public Menu toEntity(MenuGroup menuGroup, BigDecimal price) {
        if (!this.price.equals(price)) {
            throw new IllegalArgumentException("요청 가격과 저장된 가격이 다릅니다.");
        }
        return new Menu(null, name, price, menuGroup, new ArrayList<>());
    }
}
