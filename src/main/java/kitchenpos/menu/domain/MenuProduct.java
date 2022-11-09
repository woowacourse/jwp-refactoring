package kitchenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.Quantity;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private Long productId;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    private MenuProduct(Menu menu, Long productId, Quantity quantity) {
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(Long productId, Quantity quantity) {
        this(null, productId, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity.getValue();
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
