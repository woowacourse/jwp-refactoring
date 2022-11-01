package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 메뉴에 속하는 수량이 있는 상품
 */
@Entity
@Table(name = "menu_product")
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Column(name = "menu_id")
    private Long menuId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Long seq, final Long menuId, final Long productId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
