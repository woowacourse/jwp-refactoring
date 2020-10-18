package kitchenpos.ui.dto.menu;

import com.sun.istack.NotNull;

import javax.validation.constraints.Min;

public class MenuProductRequest {

    @NotNull
    private Long seq;

    @NotNull
    private Long menuId;

    @NotNull
    private Long productId;

    @NotNull
    @Min(0)
    private Long quantity;

    protected MenuProductRequest() { }

    public MenuProductRequest(Long seq, Long menuId, Long productId, Long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
