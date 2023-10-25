package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.vo.Quantity;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(nullable = false)
    private Long productId;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    private MenuProduct(Long productId, Quantity quantity) {
        this(null, null, productId, quantity);
    }

    private MenuProduct(
            Long seq,
            Menu menu,
            Long productId,
            Quantity quantity
    ) {
        this.seq = seq;
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProduct of(
            Long productId,
            long quantity
    ) {
        return new MenuProduct(
                productId,
                Quantity.from(quantity)
        );
    }

    public static MenuProduct of(
            Long productId,
            Quantity quantity
    ) {
        return new MenuProduct(productId, quantity);
    }

    public void registerMenu(Menu menu) {
        this.menu = menu;
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

    public long getQuantity() {
        return quantity.getValue();
    }

}
