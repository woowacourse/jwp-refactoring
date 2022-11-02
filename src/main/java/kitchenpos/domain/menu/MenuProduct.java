package kitchenpos.domain.menu;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.Quantity;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long productId;

    @Embedded
    private Quantity quantity;

    private Price price;

    protected MenuProduct() {
    }

    public MenuProduct(Product product, long quantity) {
        this(product, new Quantity(quantity));
    }

    public MenuProduct(Product product, Quantity quantity) {
        this(product.getId(), quantity, product.getPrice().multiply(quantity));
    }

    public MenuProduct(Long productId, Quantity quantity, Price price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
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

    public Price getPrice() {
        return price;
    }
}
