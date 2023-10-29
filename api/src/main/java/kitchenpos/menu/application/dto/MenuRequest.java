package kitchenpos.menu.application.dto;

import java.math.BigDecimal;
import java.util.List;

public class MenuRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<ProductQuantityDto> menuProducts;

    private MenuRequest() {
    }

    public MenuRequest(final String name,
                       final BigDecimal price,
                       final Long menuGroupId,
                       final List<ProductQuantityDto> menuProducts) {
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

    public List<ProductQuantityDto> getMenuProducts() {
        return menuProducts;
    }
}
