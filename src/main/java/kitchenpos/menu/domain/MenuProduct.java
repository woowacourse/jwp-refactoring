package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.common.vo.Price;
import kitchenpos.product.domain.Product;

@Entity
@Table(name = "menu_product")
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(name = "product_id")
    private Long productId;

    @Column(nullable = false)
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(
            final Long productId,
            final long quantity
    ) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setMenu(final Menu menu) {
        this.menu = menu;
    }
}
