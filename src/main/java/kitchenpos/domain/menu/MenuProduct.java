package kitchenpos.domain.menu;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.domain.generic.Price;

@Entity
@Table(name = "menu_product")
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(length = 20, nullable = false)
    private Long productId;

    @Column(length = 20, nullable = false)
    private long quantity;

    @Embedded
    private Price price;

    protected MenuProduct() {
    }

    public MenuProduct(Long seq, Menu menu, Long productId, long quantity, Price price) {
        this.seq = seq;
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public MenuProduct(Long productId, long quantity, Price price) {
        this(null, null, productId, quantity, price);
    }

    public Price calculateAmount() {
        return price.multiply(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    void setMenu(Menu menu) {
        if (this.menu != null) {
            this.menu.getMenuProducts().remove(this);
        }
        this.menu = menu;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    @Override
    public String toString() {
        return "MenuProduct{" +
                "seq=" + seq +
                ", menuId=" + menu.getId() +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
