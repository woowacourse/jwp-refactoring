package kitchenpos.Menu.domain.dto.response;

import kitchenpos.Menu.domain.MenuProduct;

public class MenuProductResponse {

    private Long seq;
    private long quantity;

    private MenuProductResponse(Long seq, long quantity) {
        this.seq = seq;
        this.quantity = quantity;
    }

    protected MenuProductResponse() {
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public static MenuProductResponse toDTO(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getQuantity());
    }
}
