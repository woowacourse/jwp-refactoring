package kitchenpos.dto.request;

import com.sun.istack.NotNull;

public class MenuProductCreateRequest {

    @NotNull
    private Long productId;

    @NotNull
    private Integer quantity;

    public MenuProductCreateRequest() {
    }

    public MenuProductCreateRequest(final Long productId, final Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return this.productId;
    }

    public Integer getQuantity() {
        return this.quantity;
    }
}
