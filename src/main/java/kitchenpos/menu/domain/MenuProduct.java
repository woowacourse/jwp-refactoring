package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "menu_product")
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long productId;
    private long quantity;
    @Column(name = "menu_id", insertable = false, updatable = false)
    private Long menuId;
    @Embedded
    private Price price;

    protected MenuProduct() {
    }

    public MenuProduct(final Long seq, final Long productId, final Long quantity, final Long menuId, final Price price) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
        this.menuId = menuId;
        this.price = price;
    }

    public MenuProduct(final Long productId, final long quantity, final Long menuId, final Price price) {
        this(null, productId, quantity, menuId, price);
    }

    public Price calculateAmount() {
       return this.price.multiply(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Price getPrice() {
        return price;
    }
}
