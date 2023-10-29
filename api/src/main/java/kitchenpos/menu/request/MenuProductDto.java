package kitchenpos.menu.request;

import javax.validation.constraints.NotNull;

public class MenuProductDto {

    @NotNull
    private final Long productId;
    @NotNull
    private final long quantity;

    public MenuProductDto(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
