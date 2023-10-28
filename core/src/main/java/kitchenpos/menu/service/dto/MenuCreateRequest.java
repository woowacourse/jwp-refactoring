package kitchenpos.menu.service.dto;

import java.util.List;

public class MenuCreateRequest {

    private final String name;

    private final Long price;

    private final Long menuGroupId;

    private final List<Long> menuProductIds;

    public MenuCreateRequest(final String name, final Long price, final Long menuGroupId, final List<Long> menuProductIds) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductIds = menuProductIds;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<Long> getMenuProductIds() {
        return menuProductIds;
    }
}
