package kitchenpos.menu.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class MenuProductCreateRequest {

    private final Long productId;
    private final Long quantity;

    @JsonCreator
    public MenuProductCreateRequest(Long productId, Long quantity) {
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
