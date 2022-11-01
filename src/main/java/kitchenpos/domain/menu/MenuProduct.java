package kitchenpos.domain.menu;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.domain.common.Price;

@Entity
@Table(name = "menu_product")
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;
    @Column(name = "product_id", nullable = false)
    private Long productId;
    @Column(name = "quantity", nullable = false)
    private long quantity;
    @Embedded
    private Price price;

    protected MenuProduct() {
    }

    public MenuProduct(final Long productId, final long quantity, final Price price) {
        this(null, null, productId, quantity, price);
    }

    public MenuProduct(final Long seq, final Menu menu, final Long productId, final long quantity, final Price price) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        designateMenu(menu);
    }

    public void designateMenu(final Menu menu) {
        if (this.menu != null) {
            this.menu.getMenuProducts()
                    .remove(this);
        }
        this.menu = menu;
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

    public Price getPrice() {
        return price;
    }

    public Price getAmount() {
        return price.multiply(quantity);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MenuProduct)) {
            return false;
        }
        MenuProduct that = (MenuProduct) o;
        return Objects.equals(seq, that.getSeq());
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
