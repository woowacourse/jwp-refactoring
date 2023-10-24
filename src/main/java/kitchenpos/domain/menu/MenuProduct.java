package kitchenpos.domain.menu;

import kitchenpos.domain.product.Product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class MenuProduct {
    public static final String PRODUCT_QUANTITY_IS_BELOW_ZERO_ERROR_MESSAGE = "수량은 0보다 커야 합니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
    @ManyToOne
    @JoinColumn(name = "product_id")
    @NotNull
    private Product product;
    @NotNull
    private long quantity;

    protected MenuProduct() {
    }

    private MenuProduct(final Long seq, final Product product, final long quantity) {
        this.seq = seq;
        this.product = product;
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    private static void validateQuantity(final long quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(PRODUCT_QUANTITY_IS_BELOW_ZERO_ERROR_MESSAGE);
        }
    }

    public static MenuProduct of(final Product product, final long quantity) {
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

    public long getQuantity() {
        return quantity;
    }
}
