package kitchenpos.domain.menu;

import javax.persistence.*;

@Table(name = "menu_product")
@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(Menu menu, Product product, long quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getContainMenuId() {
        return menu.getId();
    }

    public Long getContainProductId() {
        return product.getId();
    }

    public boolean isSameMenuId(Menu menu) {
        return this.menu.getId().equals(menu.getId());
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getSeq() {
        return seq;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
