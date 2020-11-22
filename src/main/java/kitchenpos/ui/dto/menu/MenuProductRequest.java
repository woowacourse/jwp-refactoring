package kitchenpos.ui.dto.menu;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class MenuProductRequest {

    @NotNull
    private Long menuId;

    @NotNull
    private Long productId;

    @NotNull
    @Min(0)
    private long quantity;

    protected MenuProductRequest() {
    }

    public MenuProductRequest(Long menuId, Long productId, long quantity) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
