package kitchenpos.dto;

import java.util.List;

public class MenuRequest {

    private final String name;
    private final double price;
    private final Long menuGroupId;
    private final List<Long> menuProductIds;

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
