package kitchenpos.dto.menu.request;

import java.math.BigDecimal;
import java.util.List;

public class MenuRequest {

    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductRequest> menuProducts;

    public MenuRequest(
        String name,
        BigDecimal price,
        Long menuGroupId,
        List<MenuProductRequest> menuProductRequests
    ) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProductRequests;
    }

    public MenuRequest() {
        this(null, null, null, null);
    }

    public MenuRequest(String name, int price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        this(name, BigDecimal.valueOf(price), menuGroupId, menuProductRequests);
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
