package kitchenpos.menu;

import kitchenpos.common.vo.Price;
import kitchenpos.product.Product;

import javax.persistence.Column;
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

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;

    @Column
    private long quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_to_menu"))
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_to_product"))
    private Product product;

    protected MenuProduct() {
    }

    private MenuProduct(final Long seq, final long quantity, final Menu menu, final Product product) {
        this.seq = seq;
        this.quantity = quantity;
        this.menu = menu;
        this.product = product;
    }

    public MenuProduct(final long quantity, final Menu menu, final Product product) {
        this(null, quantity, menu, product);
    }

    public Price getPrice() {
        return product.getPrice().multiply(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }
}
