package kitchenpos.ui.dto.response;

import kitchenpos.application.dto.ReadMenuDto;
import kitchenpos.application.dto.ReadMenuProductDto;

public class ReadMenuProductResponse {

    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final long quantity;

    public ReadMenuProductResponse(final ReadMenuDto readMenuDto, final ReadMenuProductDto readMenuProductDto) {
        this.seq = readMenuProductDto.getSeq();
        this.menuId = readMenuDto.getId();
        this.productId = readMenuProductDto.getProductId();
        this.quantity = readMenuProductDto.getQuantity();
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
