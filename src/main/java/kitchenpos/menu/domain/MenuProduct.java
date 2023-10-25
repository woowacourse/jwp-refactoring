package kitchenpos.menu.domain;

import kitchenpos.menu.domain.vo.Price;
import kitchenpos.menu.domain.vo.Quantity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @JoinColumn(name = "menu_id", nullable = false, foreignKey = @ForeignKey(name = "fk_menu_product_to_menu"))
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_menu_product_to_product"))
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    protected MenuProduct(final Menu menu,
                          final Product product,
                          final Quantity quantity
    ) {
        this(null, menu, product, quantity);
    }

    protected MenuProduct(final Long seq,
                          final Menu menu,
                          final Product product,
                          final Quantity quantity
    ) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct withoutMenu(final Product product, final Quantity quantity) {
        return new MenuProduct(null, product, quantity);
    }

    public void assignMenu(final Menu requestMenu) {
        menu = requestMenu;
    }

    public Price getTotalPrice() {
        return product.getPrice().multiply(quantity);
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

    public Quantity getQuantity() {
        return quantity;
    }
}
