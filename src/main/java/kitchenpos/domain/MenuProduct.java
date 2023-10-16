package kitchenpos.domain;

import com.sun.istack.NotNull;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {

    private static final int MIN_QUANTITY = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @NotNull
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected MenuProduct() {
    }

    public MenuProduct(
            Menu menu,
            Long quantity,
            Product product
    ) {
        this.menu = menu;
        this.quantity = quantity;
        this.product = product;
    }

    public static MenuProduct of(
            Menu menu,
            Long quantity,
            Product product
    ) {
        validateQuantity(quantity);

        return new MenuProduct(menu, quantity, product);
    }

    private static void validateQuantity(Long quantity) {
        if (quantity < MIN_QUANTITY) {
            throw new IllegalArgumentException();
        }
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }
}
