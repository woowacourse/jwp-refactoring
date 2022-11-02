package kitchenpos.domain.menu;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.Price;
import kitchenpos.domain.Quantity;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long productId;
    private Price productPrice;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long productId, Price productPrice, Quantity quantity) {
        this.productId = productId;
        this.quantity = quantity;
        this.productPrice = productPrice;
    }

    public Price calculateTotalPrice() {
        return productPrice.multiply(quantity);
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

    public Price getProductPrice() {
        return productPrice;
    }
}
