package kitchenpos.domain.menu;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.product.Product;
import kitchenpos.vo.Money;
import kitchenpos.vo.Quantity;

@Entity
public class MenuProduct {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long seq;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = LAZY)
    private Product product;

    @Embedded
    private Quantity quantity;

    MenuProduct(Long seq, Product product, Quantity quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    MenuProduct(Product product, Quantity quantity) {
        this(null, product, quantity);
    }

    protected MenuProduct() {
    }

    public static MenuProduct of(Product product, long quantity) {
        return new MenuProduct(product, Quantity.valueOf(quantity));
    }

    public Money calculateTotalPrice() {
        return product.calculateTotalPrice(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return product.getId();
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public long getQuantityValue() {
        return quantity.getValue();
    }
}
