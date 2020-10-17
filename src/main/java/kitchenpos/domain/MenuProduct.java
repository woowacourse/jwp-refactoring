package kitchenpos.domain;

import kitchenpos.config.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@AttributeOverride(name = "id", column = @Column(name = "menu_product_id"))
@Entity
public class MenuProduct extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "FK_MENU_PRODUCT_MENU"))
    private Menu menu;
    private Long productId;
    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(Long id, Menu menu, Long productId, Long quantity) {
        this.id = id;
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(Long productId, Long quantity) {
        this(null, null, productId, quantity);
    }

    public MenuProduct(Menu menu, Long productId, long quantity) {
        this(null, menu, productId, quantity);
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
}
