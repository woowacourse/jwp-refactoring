package kitchenpos.application.dto.request;

public class MenuProductRequestDto {

    private Long productId;
    private Long quantity;

    private MenuProductRequestDto() {
    }

    public MenuProductRequestDto(Long productId, Long quantity) {
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
