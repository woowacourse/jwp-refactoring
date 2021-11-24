package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column
    private Long quantity;

    protected MenuProduct() {
    }

    private MenuProduct(Product product, Long quantity) {
        this(null, null, product, quantity);
    }

    private MenuProduct(Long seq, Menu menu, Product product, Long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct create(Product product, Long quantity) {
        return new MenuProduct(product, quantity);
    }

    public static MenuProduct create(Long seq, Menu menu, Product product, Long quantity) {
        return new MenuProduct(seq, menu, product, quantity);
    }

    public BigDecimal totalPrice() {
        return product.calculatePrice(quantity);
    }

    public void addMenu(Menu menu) {
        this.menu = menu;
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }

    public Long getQuantity() {
        return quantity;
    }
}
