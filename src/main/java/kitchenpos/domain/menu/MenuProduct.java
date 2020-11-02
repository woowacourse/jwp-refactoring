package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.base.BaseSeqEntity;
import kitchenpos.domain.product.Product;

@Entity
public class MenuProduct extends BaseSeqEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private MenuProductQuantity quantity;

    protected MenuProduct() {
    }

    private MenuProduct(Long seq, Menu menu, Product product, MenuProductQuantity quantity) {
        super(seq);
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct of(Long seq, Menu menu, Product product, long quantity) {
        return new MenuProduct(seq, menu, product, new MenuProductQuantity(quantity));
    }

    public static MenuProduct entityOf(Product product, long quantity) {
        return of(null, null, product, quantity);
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        if (Objects.isNull(this.menu)) {
            this.menu = menu;
        }
    }

    public Product getProduct() {
        return product;
    }

    public BigDecimal getProductPrice() {
        return this.product.getPrice();
    }

    public long getQuantity() {
        return quantity.getQuantity();
    }

    @Override
    public String toString() {
        return "MenuProduct{" +
            "seq=" + getSeq() +
            ", quantity=" + quantity +
            '}';
    }
}
