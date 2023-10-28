package kitchenpos.domain.menuproduct;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.product.Product;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @NotNull
    @Embedded
    private MenuProductQuantity quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    protected MenuProduct() {
    }

    public MenuProduct(final MenuProductQuantity quantity, final Product product) {
        this.menu = null;
        this.quantity = quantity;
        this.product = product;
    }

    public MenuProduct(final MenuProductQuantity quantity, final Menu menu, final Product product) {
        this.quantity = quantity;
        this.menu = menu;
        this.product = product;
    }

    public BigDecimal calculatePrice() {
        return product.getPrice()
                .multiply(BigDecimal.valueOf(getQuantity()));
    }

    public void changeMenu(final Menu menu) {
        this.menu = menu;
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity.getQuantity();
    }
}
