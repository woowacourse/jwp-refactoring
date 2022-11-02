package kitchenpos.domain.menu;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    private Menu menu;
    private Long productId;
    private long quantity;
    @Transient
    private Price price;

    public MenuProduct(final Long seq,
                        final Menu menu,
                        final Long productId,
                        final long quantity,
                        final Price price) {
        this.seq = seq;
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public static MenuProduct ofUnsaved(final Menu menu, final Product product, final long quantity) {
        return new MenuProduct(null, menu, product.getId(), quantity, product.calculatePrice(quantity));
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menu.getId();
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

    protected MenuProduct() {
    }
}
