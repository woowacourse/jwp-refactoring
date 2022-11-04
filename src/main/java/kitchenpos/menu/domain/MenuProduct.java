package kitchenpos.menu.domain;

import java.math.BigDecimal;
import kitchenpos.product.domain.Price;

public class MenuProduct {
    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final Price price;
    private final long quantity;

    public MenuProduct(Long menuId, Long productId, BigDecimal price, long quantity) {
        this(null, menuId, productId, new Price(price), quantity);
    }

    public MenuProduct(Long seq, Long menuId, Long productId, Price price, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    public BigDecimal getMenuPrice() {
        return this.price.multiply(quantity);
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

    public Price getPrice() {
        return price;
    }
}
