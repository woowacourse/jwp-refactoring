package kitchenpos.domain.menu.menu_product;

import static kitchenpos.exception.MenuException.NegativeQuantityException;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.product.Product;
import kitchenpos.support.AggregateReference;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long seq;
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "product_id"))
    private final AggregateReference<Product> productId;
    private final Long quantity;

    protected MenuProduct() {
        this.seq = null;
        this.productId = null;
        this.quantity = null;
    }

    public MenuProduct(
            final AggregateReference<Product> productId,
            final Long quantity,
            final MenuProductValidator menuProductValidator
    ) {
        this.seq = null;
        this.productId = productId;
        this.quantity = quantity;
        menuProductValidator.validate(productId);
        validateQuantity(quantity);
    }

    private void validateQuantity(final Long quantity) {
        if (quantity < 0) {
            throw new NegativeQuantityException(quantity);
        }
    }

    public Long getSeq() {
        return seq;
    }

    public AggregateReference<Product> getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
