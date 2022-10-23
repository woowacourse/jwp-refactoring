package kitchenpos.domain;

import lombok.Getter;

@Getter
public class MenuProduct {

    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final Integer quantity;

    public MenuProduct(Long seq, Long menuId, Long productId, Integer quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(Long menuId, Long productId, Integer quantity) {
        this(null, menuId, productId, quantity);
    }
}
