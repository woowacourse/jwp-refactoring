package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;

public class MenuRequest {

    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductRequest> menuProductData;

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductData) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductData = menuProductData;
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

    public List<MenuProductRequest> getMenuProductRequests() {
        return menuProductData;
    }
}
