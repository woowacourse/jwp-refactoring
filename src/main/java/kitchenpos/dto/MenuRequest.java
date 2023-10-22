package kitchenpos.dto;

import java.util.List;

public class MenuRequest {

    private String name;
    private Integer price;
    private Long menuGroupId;
    private List<MenuProductDto> menuProducts;

    public MenuRequest(final String name,
                       final Integer price,
                       final Long menuGroupId,
                       final List<MenuProductDto> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public List<MenuProductDto> getMenuProducts() {
        return menuProducts;
    }
}
