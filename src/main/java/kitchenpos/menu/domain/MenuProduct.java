package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull
    private Long quantity;

    protected MenuProduct() {
    }

    private MenuProduct(final Long seq, final Menu menu, final Product product, final long quantity) {
        validateQuantity(quantity);
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct create(final Menu menu, final Product product, final long quantity) {
        return new MenuProduct(null, menu, product, quantity);
    }

    private void validateQuantity(final Long quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("메뉴 상품의 수량은 1개 이상이어야 합니다.");
        }
    }

    public double calculateMenuProductPrice() {
        return product.getPrice().doubleValue() * quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Product getProduct() {
        return product;
    }

    public Long getQuantity() {
        return quantity;
    }
}
