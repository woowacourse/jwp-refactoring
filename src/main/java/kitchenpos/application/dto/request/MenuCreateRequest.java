package kitchenpos.application.dto.request;

import java.math.BigDecimal;
import java.util.List;

public class MenuCreateRequest {

    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductCreateRequest> productRequests;

    private MenuCreateRequest() {
        this(null, null, null, null);
    }

    public MenuCreateRequest(
            String name,
            BigDecimal price,
            Long menuGroupId,
            List<MenuProductCreateRequest> productRequests
    ) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.productRequests = productRequests;
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

    public List<MenuProductCreateRequest> getProductRequests() {
        return productRequests;
    }
}
