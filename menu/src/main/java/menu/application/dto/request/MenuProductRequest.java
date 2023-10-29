package menu.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class MenuProductRequest {

    private final long productId;
    private final long quantity;

    @JsonCreator
    public MenuProductRequest(final long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
