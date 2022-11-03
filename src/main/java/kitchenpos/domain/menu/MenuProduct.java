package kitchenpos.domain.menu;

import java.math.BigDecimal;
import kitchenpos.domain.Price;

public class MenuProduct {
    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final Price productPrice;
    private final long quantity;

    public MenuProduct(Long seq, Long menuId, Long productId, Price price, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.productPrice = price;
        this.quantity = quantity;
    }

    public static MenuProduct createWithPrice(Long menuId, Long productId, BigDecimal productPrice, long quantity) {
        return new MenuProduct(null, menuId, productId, new Price(productPrice), quantity);
    }

    public static MenuProduct createEntity(Long id, Long menuId, Long productId, long quantity) {
        return new MenuProduct(id, menuId, productId, null, quantity);
    }

    public BigDecimal calculateTotalPrice() {
        return productPrice.calculateTotalAmount(quantity);
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
