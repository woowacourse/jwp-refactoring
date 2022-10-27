package kitchenpos.ui.dto.request;

import java.math.BigDecimal;
import java.util.List;

public class MenuCreationRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    private MenuCreationRequest() {
    }

    public MenuCreationRequest(final String name,
                               final BigDecimal price,
                               final Long menuGroupId,
                               final List<MenuProductRequest> menuProducts) {
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

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
