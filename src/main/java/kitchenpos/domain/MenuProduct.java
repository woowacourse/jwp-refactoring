package kitchenpos.domain;

import java.math.BigDecimal;

public class MenuProduct {

    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final long quantity;
    private final Price price;

    /**
     * DB 에 저장되지 않은 객체
     * Service 로직을 단순화하기 위해 Price 정보 추가
     */
    public MenuProduct(final Long productId, final long quantity, final BigDecimal price) {
        this(null, null, productId, quantity, new Price(price));
    }

    /**
     * DB 에 저장하기 위한 객체
     */
    public MenuProduct(final Long menuId, final Long productId, final long quantity) {
        this(null, menuId, productId, quantity, null);
    }

    /**
     * DB 에 저장된 객체
     */
    public MenuProduct(final Long seq, final Long menuId, final Long productId, final long quantity) {
        this(seq, menuId, productId, quantity, null);
    }

    public MenuProduct(final Long seq, final Long menuId, final Long productId, final long quantity, final Price price) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public BigDecimal getAmount() {
        return price.multiply(quantity);
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

    public BigDecimal getPrice() {
        return price.getPrice();
    }
}
