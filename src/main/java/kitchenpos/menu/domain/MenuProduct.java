package kitchenpos.menu.domain;

import javax.persistence.*;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column
    private Long productId;

    @Column
    private Long quantity;

    protected MenuProduct() {
    }

    private MenuProduct(Long productId, Long quantity) {
        this(null, null, productId, quantity);
    }

    private MenuProduct(Menu menu, Long productId, Long quantity) {
        this(null, menu, productId, quantity);
    }

    private MenuProduct(Long seq, Menu menu, Long productId, Long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProduct create(Long productId, Long quantity) {
        return new MenuProduct(productId, quantity);
    }

    public static MenuProduct create(Menu menu, Long productId, Long quantity) {
        return new MenuProduct(menu, productId, quantity);
    }

    public static MenuProduct create(Long seq, Menu menu, Long productId, Long quantity) {
        return new MenuProduct(seq, menu, productId, quantity);
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

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
