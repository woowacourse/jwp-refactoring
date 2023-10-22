package kitchenpos.domain.menu;

import static kitchenpos.exception.MenuException.NegativeQuantityException;
import static kitchenpos.exception.MenuException.NoMenuProductException;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.vo.Price;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    private final Product product;
    private final Long quantity;

    public MenuProduct() {
        this.seq = null;
        this.product = null;
        this.quantity = null;
    }

    public MenuProduct(final Product product, final Long quantity) {
        validateProduct(product);
        validateQuantity(quantity);
        this.seq = null;
        this.product = product;
        this.quantity = quantity;
    }

    private void validateProduct(final Product product) {
        if (Objects.isNull(product)) {
            throw new NoMenuProductException();
        }
    }

    private void validateQuantity(final Long quantity) {
        if (quantity < 0) {
            throw new NegativeQuantityException(quantity);
        }
    }

    public Price getPrice() {
        return new Price(product.getProductPrice().multiply(quantity).getPrice());
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
