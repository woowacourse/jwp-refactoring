package kitchenpos.application.dto.request;

import java.math.BigDecimal;
import java.util.List;

public class MenuCreateRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductCreateRequest> productRequests;

    private MenuCreateRequest() {
    }

    public MenuCreateRequest(final String name, final BigDecimal price, final Long menuGroupId,
                             final List<MenuProductCreateRequest> productRequests) {
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
