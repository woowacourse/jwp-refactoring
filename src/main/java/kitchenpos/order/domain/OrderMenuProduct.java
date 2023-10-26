package kitchenpos.order.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.value.Quantity;

@Entity
public class OrderMenuProduct {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;

    @Column(nullable = false)
    private Long menuId;

    @JoinColumn(name = "product_id")
    private Long productId;

    @Embedded
    private Quantity quantity;

    public OrderMenuProduct() {
    }

    public OrderMenuProduct(Long menuId, Long productId, Quantity quantity) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static OrderMenuProduct of(final MenuProduct menuProduct) {
        return new OrderMenuProduct(
                menuProduct.getMenuId(),
                menuProduct.getProductId(),
                menuProduct.getQuantity()
        );
    }
}
