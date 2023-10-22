package kitchenpos.application.dto.request;

import java.math.BigDecimal;
import java.util.List;

public class MenuCreateRequest {

    String name;
    BigDecimal price;
    Long menuGroupId;
    List<MenuProductRequest> menuProducts;

    public MenuCreateRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
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
