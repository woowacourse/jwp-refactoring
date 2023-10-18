package kitchenpos.dto.request;

import java.math.BigDecimal;
import java.util.List;

public class MenuCreateRequest {

    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductCreateRequest> menuProductCreateRequests;

    public MenuCreateRequest(
            String name,
            BigDecimal price,
            Long menuGroupId,
            List<MenuProductCreateRequest> menuProductCreateRequests
    ) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductCreateRequests = menuProductCreateRequests;
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

    public List<MenuProductCreateRequest> getMenuProductCreateRequests() {
        return menuProductCreateRequests;
    }
}
