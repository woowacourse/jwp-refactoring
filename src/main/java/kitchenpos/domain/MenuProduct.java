package kitchenpos.domain;

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

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
