package kitchenpos.menu.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @JoinColumn(name = "product_id")
    private Long productId;
    private long quantity;

    public MenuProduct() {
    }

    private MenuProduct(final Long id, final Long productId, final long quantity) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
    }

    private MenuProduct(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProduct of(final Long productId, final long quantity) {
        return new MenuProduct(productId, quantity);
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
