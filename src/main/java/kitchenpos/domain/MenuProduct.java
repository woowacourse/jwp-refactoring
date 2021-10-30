package kitchenpos.domain;

import kitchenpos.exception.menuproduct.AlreadyAssignedMenuProductException;

import javax.persistence.*;

@Entity
@Table(name = "menu_product")
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"), nullable = false)
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"), nullable = false)
    private Product product;

    @Embedded
    private Quantity quantity;

    public MenuProduct() {
    }

    public MenuProduct(Product product, Long quantity) {
        this(null, null, product, quantity);
    }

    private MenuProduct(Long seq, Menu menu, Product product, Long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = Quantity.from(quantity);

        if (menu != null) {
            menu.getMenuProducts().add(this);
        }
    }

    public void belongsTo(Menu menu) {
        if (this.menu != null) {
            throw new AlreadyAssignedMenuProductException();
        }
        this.menu = menu;
    }

    public Long getTotalPrice() {
        return this.product.getPrice() * quantity.longValue();
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
        return quantity.longValue();
    }
}
