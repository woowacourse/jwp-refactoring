package kitchenpos.application.dto;

import java.math.BigDecimal;
import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {

    private final long seq;
    private final long menuId;
    private final long productId;
    private final long quantity;
    private final BigDecimal price;

    private MenuProductResponse(final long seq, final long menuId, final long productId, final long quantity,
                                final BigDecimal price) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public static MenuProductResponse from(final MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(),
                menuProduct.getMenuId(),
                menuProduct.getProductId(),
                menuProduct.getQuantity(),
                menuProduct.getPrice());
    }

    public long getSeq() {
        return seq;
    }

    public long getMenuId() {
        return menuId;
    }

    public long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
