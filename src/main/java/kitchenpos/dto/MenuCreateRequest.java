package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.domain.MenuProductCreateInfo;

public class MenuCreateRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductCreateInfo> menuProducts;

    private MenuCreateRequest() {
    }

    public MenuCreateRequest(String name, BigDecimal price, Long menuGroupId,
        List<MenuProductCreateInfo> menuProducts) {
        this.name = name;
        this.price = price;
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

    public List<MenuProductCreateInfo> getMenuProducts() {
        return menuProducts;
    }
}
