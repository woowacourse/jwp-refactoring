package kitchenpos.menu.dto.request;

import com.sun.istack.NotNull;

public class MenuProductRequest {

    @NotNull
    private Long productId;
    @NotNull
    private Long quantity;

    public MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
