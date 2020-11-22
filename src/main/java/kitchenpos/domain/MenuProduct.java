package kitchenpos.domain;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.product.Product;
import kitchenpos.exception.InvalidMenuProductQuantityException;
import kitchenpos.util.ValidateUtil;

import javax.persistence.*;

@Entity
public class MenuProduct {
    private static final long MIN_QUANTITY = 1L;

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

    protected MenuProduct() {
    }

    public MenuProduct(Menu menu, Product product, long quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct of(Menu menu, Product product, long quantity) {
        ValidateUtil.validateNonNull(menu, product);
        if (quantity < MIN_QUANTITY) {
            throw new InvalidMenuProductQuantityException("메뉴 상품의 수량은 " + MIN_QUANTITY + "개 이상이어야 합니다!");
        }

        return new MenuProduct(menu, product, quantity);
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

    public long getQuantity() {
        return quantity;
    }
}
