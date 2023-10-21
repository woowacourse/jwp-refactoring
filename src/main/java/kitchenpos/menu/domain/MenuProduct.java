package kitchenpos.menu.domain;

import kitchenpos.common.domain.Quantity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "menu_product")
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "product_id")
    private Long productId;

    @Embedded
    private Quantity quantity;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    protected MenuProduct() {
    }

    public MenuProduct(final Long productId, final Long quantity) {
        this.productId = productId;
        this.quantity = Quantity.from(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity.getQuantity();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuProduct)) return false;
        MenuProduct that = (MenuProduct) o;
        return Objects.equals(seq, that.seq) && Objects.equals(productId, that.productId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, productId, quantity);
    }
}
