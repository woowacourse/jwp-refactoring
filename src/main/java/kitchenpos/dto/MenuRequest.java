package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.domain.Menu;

public class MenuRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<ProductQuantityRequest> productQuantities;

    protected MenuRequest() {
    }

    public MenuRequest(final String name, final BigDecimal price, final Long menuGroupId,
            final List<ProductQuantityRequest> productQuantities) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.productQuantities = productQuantities;
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

    public List<ProductQuantityRequest> getProductQuantities() {
        return productQuantities;
    }

    public Menu toEntity() {
        return new Menu(this.name, this.price, this.menuGroupId);
    }
}
