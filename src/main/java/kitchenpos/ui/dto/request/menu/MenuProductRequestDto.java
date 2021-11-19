package kitchenpos.ui.dto.request.menu;

public class MenuProductRequestDto {

    private Long productId;
    private Long quantity;

    private MenuProductRequestDto() {
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
