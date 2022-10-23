package kitchenpos.domain;

import lombok.Getter;

@Getter
public class MenuProduct {

    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final long quantity;

    public MenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(Long menuId, Long productId, long quantity) {
        this(null, menuId, productId, quantity);
    }
}
