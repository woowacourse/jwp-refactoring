package kitchenpos.menu.presentation.dto;

public class MenuProductCreateRequest {

    private Long productId;
    private Long quantity;

    public MenuProductCreateRequest() {
    }

    public MenuProductCreateRequest(final Long productId,
                                    final Long quantity) {
        validate(productId);
        this.productId = productId;
        this.quantity = quantity;
    }

    private static void validate(final Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("productId는 null값이 될 수 없습니다.");
        }
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
