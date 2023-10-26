package kitchenpos.domain.menu;

import kitchenpos.domain.common.Quantity;
import kitchenpos.domain.product.Product;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @NotNull
    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    private MenuProduct(final Long seq, final Product product, final Quantity quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct of(final Product product, final Quantity quantity) {
        return new MenuProduct(null, product, quantity);
    }

    protected void setMenu(final Menu menu) {
        this.menu = menu;
    }

    public Long getSeq() {
        return seq;
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
