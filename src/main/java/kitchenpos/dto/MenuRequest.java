package kitchenpos.dto;

import java.util.List;
import kitchenpos.domain.MenuProduct;

public class MenuRequest {

    private String name;
    private double price;
    private Long menuGroupId;
    private List<Long> menuProductIds;

    public MenuRequest(String name, double price, Long menuGroupId, List<Long> menuProductIds) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductIds = menuProductIds;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<Long> getMenuProductIds() {
        return menuProductIds;
    }
}
