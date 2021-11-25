package kitchenpos.menu.ui.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class MenuProductRequest {

    @NotNull(message = "상품의 아이디가 null입니다.")
    private Long productId;

    @Min(value = 0, message = "메뉴 상품의 개수는 음수일 수 없습니다.")
    private Long quantity;

    protected MenuProductRequest() {
    }

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
