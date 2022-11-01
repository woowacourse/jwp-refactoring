package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;

public class MenuSaveRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductSaveRequest> menuProducts;

    private MenuSaveRequest() {
    }

    public MenuSaveRequest(final String name,
                           final BigDecimal price,
                           final Long menuGroupId,
                           final List<MenuProductSaveRequest> menuProducts) {
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

    public List<MenuProductSaveRequest> getMenuProducts() {
        return menuProducts;
    }
}
