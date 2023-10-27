package kitchenpos.menu.request;

import java.math.BigDecimal;
import java.util.List;

public class MenuCreateRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuCreateRequest(String name, Long price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = toBigDecimal(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private BigDecimal toBigDecimal(Long price) {
        if (price == null) {
            return null;
        }
        return BigDecimal.valueOf(price);
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
