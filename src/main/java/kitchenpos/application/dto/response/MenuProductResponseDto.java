package kitchenpos.application.dto.response;

public class MenuProductResponseDto {

    private final Long productId;
    private final long quantity;

    public MenuProductResponseDto(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
