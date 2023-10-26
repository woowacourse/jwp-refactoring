package kitchenpos.dto.request;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class MenuProductCreateRequest {

    @NotNull(message = "상품 Id를 입력해 주세요.")
    private final Long productId;

    @NotNull(message = "상품 수량을 입력해 주세요.")
    @Min(1)
    private final long quantity;

    public MenuProductCreateRequest(final Long productId, final long quantity) {
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
