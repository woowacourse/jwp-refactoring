package kitchenpos.domain;

import javax.persistence.*;

@Entity
public class MenuProduct extends BaseEntity {

    @ManyToOne
    private Menu menu;

    @ManyToOne
    private Product product;

    private long quantity;

    public MenuProduct() {}

    public MenuProduct(Long seq, Menu menu, Product product, long quantity) {
        super(seq);
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
