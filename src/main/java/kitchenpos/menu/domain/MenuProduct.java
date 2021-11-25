package kitchenpos.menu.domain;

import javax.persistence.*;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Menu menu;

    private Long productId;

    private long quantity;

    public MenuProduct() {}

    public MenuProduct(Long seq, Menu menu, Long productId, long quantity) {
        this.id = seq;
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
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
