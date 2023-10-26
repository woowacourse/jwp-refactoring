package kitchenpos.menu.service.dto;

public class MenuProductCreateCommand {

    private final Long productId;
    private final Long quantity;

    public MenuProductCreateCommand(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long productId() {
        return productId;
    }

    public Long quantity() {
        return quantity;
    }
}
