package kitchenpos.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MenuProductRequest {

    @JsonProperty("productId")
    private Long productId;
    @JsonProperty("quantity")
    private long quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long productId() {
        return productId;
    }

    public long quantity() {
        return quantity;
    }
}
