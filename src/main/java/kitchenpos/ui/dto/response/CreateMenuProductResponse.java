package kitchenpos.ui.dto.response;

import kitchenpos.application.dto.CreateMenuDto;
import kitchenpos.application.dto.CreateMenuProductDto;

public class CreateMenuProductResponse {

    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final long quantity;

    public CreateMenuProductResponse(
            final CreateMenuDto createMenuDto,
            final CreateMenuProductDto createMenuProductDto
    ) {
        this.seq = createMenuProductDto.getSeq();
        this.menuId = createMenuDto.getId();
        this.productId = createMenuProductDto.getProductId();
        this.quantity = createMenuProductDto.getQuantity();
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
