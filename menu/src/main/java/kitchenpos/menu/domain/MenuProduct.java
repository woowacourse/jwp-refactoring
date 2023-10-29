package kitchenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.menu.vo.Price;
import kitchenpos.menu.vo.ProductSpecification;
import kitchenpos.menu.vo.Quantity;

@Entity
public class MenuProduct {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;

    private Long productId;

    @Embedded
    private Quantity quantity;

    @Embedded
    private ProductSpecification productSpecification;

    public MenuProduct(Long seq, Long productId, Quantity quantity, ProductSpecification productSpecification) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
        this.productSpecification = productSpecification;
    }

    public MenuProduct(Long productId, Quantity quantity, ProductSpecification productSpecification) {
        this(null, productId, quantity, productSpecification);
    }

    protected MenuProduct() {
    }

    public static MenuProduct of(Long productId, long quantity, ProductSpecification productSpecification) {
        return new MenuProduct(productId, Quantity.valueOf(quantity), productSpecification);
    }

    public Price calculateTotalPrice() {
        return productSpecification.calculateTotalPrice(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public long getQuantityValue() {
        return quantity.getValue();
    }
}
